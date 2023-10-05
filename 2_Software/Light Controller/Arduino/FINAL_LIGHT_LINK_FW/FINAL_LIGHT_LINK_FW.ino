#include <SPI.h>
#include <mcp2515.h>
#include <Adafruit_NeoPixel.h>

#define KILL_PIN 32

#define BRAKE_FLASH_MINIMUM_LENGTH 1500 //minimum amount of time that the brake lights should flash for
#define BRAKE_FLASH_TIME_STEP 100 //general time step for brake light flashing animation 
#define LIGHT_CYCLE_TIME_STEP 100 //general time step for programming animations
#define TURN_SIGNAL_TIME_STEP 500 //general time step for turn signal flashing animation
#define MIN_TURN_SIG_FLASH 3 //minimum number of times turn signals will flash when triggered
#define STARTUP_FLASH_COUNT 3 //number of times the fourth brake light will flash when board boots up

#define CANBUS_TIMEOUT 500000  //TODO: integrate

#define MOSFET_OFF_STATE 0  //included in case we want to swap which pin state turns a MOSFET on
#define MOSFET_ON_STATE 1

//CAN I/O
struct can_frame canMsg;
MCP2515 mcp2515(42); //TODO: I think CS is 42 but double check

//addressable channels (appended "NP" denotes a 'NeoPixel' channel)
Adafruit_NeoPixel FR_BUMP_NP(4, 33, NEO_GRB + NEO_KHZ800);   
/*   0 --> left wheel well
 *   1 --> left front bumper
 *   2 --> right front bumper
 *   3 --> right wheel well
 */
 
Adafruit_NeoPixel LIGHTBARS_NP(8, 35, NEO_GRB + NEO_KHZ800);  
/*   0 --> front left
 *   1 --> front left-mid
 *   2 --> front right-mid
 *   3 --> front right
 *   4 --> rear right
 *   5 --> rear right-mid
 *   6 --> rear left-mid
 *   7 --> rear left
 */
 
Adafruit_NeoPixel TRUNK_NP(2, 34, NEO_GRB + NEO_KHZ800);
/*   0 --> left license plate
 *   1 --> right license plate
 */

Adafruit_NeoPixel R_BUMP_NP(3, 37, NEO_GRB + NEO_KHZ800); //TODO: confirm LED IDs
/*   0 --> left backup light 
 *   1 --> fourth brake light
 *   2 --> right backup light
 */

//these are in ascending order on the PCB (addr chan 1-6)
int NP_PINS[6] = { 33, 35, 34, 37, 36, 39 };

//these variables link specific lights to the indexes within M_PINS and M_STATES

int R_HL_DRL = 0;  //front DRLs (leave actual head light control to steering wheel stalk)
int L_HL_DRL = 1;
int R_HL_IND = 2;  //front indicators
int L_HL_IND = 3;
int R_BL_BRK = 4;  //rear brake lights (do not touch third brake light wiring)
int L_BL_BRK = 5;
int R_BL_IND = 6;  //rear indicators
int L_BL_IND = 7;
int BL_RUN = 8;  //rear running lights

//the pinout was chosen such that the front DRLs and brake lights are on the hardware PWM channels, the remaining assignments are arbitrary

int M_PINS[12] = { 29, 31, 30, 28, 25, 27, 26, 24, 12, 45, 46, 44 };

//status vars (no NP state vars since the library kinda has inherent states)
int lightCycleState = 0;  //denotes animation states: -1 is blackout, 0 is off/normal operation, 1 is police, 2 is fast hazard, 3 is normal hazard
int turnSignalState = 0;  //-2 is hazard, -1 is left, 0 is none, 1 is right
bool runningLightState = false;
bool brakeLightFlashing = false;  //kept separate since lightCycleState can have a value while brake lights are flashing
bool M_STATES[12] = { false, false, false, false, false, false, false, false, false, false, false, false };

//car status vars
int brakePressure = 0;
int vehicleSpeed = 0;

//request vars --> these are assigned based on CAN data and reset as soon as the request is fulfilled
bool brakeRequest = false;  //should I keep this a bool or make it an int so it can also represent brake light flashing
bool warningFlashRequest = false;  //used to flash lights deliberately to warn other drivers

//timers used to keep track of where we are within a light animaion cycle
unsigned long brakeFlashTimer = 0;
unsigned long lightCycleTimer = 0;
unsigned long canBusTimeoutTimer = millis(); //shuts the board down if CAN communications stop for more than CANBUS_TIMEOUT milliseconds
unsigned long turnSignalTimer = 0;

void setup() {
  mcp2515.reset();
  mcp2515.setBitrate(CAN_500KBPS, MCP_8MHZ);
  mcp2515.setNormalMode();

  //set NP pins to OUTPUT
  for (int i = 0; i < 6; i++) {
    pinMode(NP_PINS[i], OUTPUT);
  }

  FR_BUMP_NP.begin();
  LIGHTBARS_NP.begin();
  TRUNK_NP.begin();
  R_BUMP_NP.begin();

  //set MOSFET pins to OUTPUT
  for (int i = 0; i < 12; i++) {
    pinMode(M_PINS[i], OUTPUT);
  }

  //set KILL pin to OUTPUT
  pinMode(KILL_PIN, OUTPUT);

  lightsOff();

  //startup sequence here
  for (int i = 0; i < STARTUP_FLASH_COUNT; i++) {
    R_BUMP_NP.setPixelColor(2, 255, 255, 255);
    R_BUMP_NP.show();
    delay(BRAKE_FLASH_TIME_STEP);
    R_BUMP_NP.setPixelColor(2, 0, 0, 0);
    R_BUMP_NP.show();
    delay(BRAKE_FLASH_TIME_STEP);
  }
}

void loop() {
  //handle CAN messages and set state variables
  if (mcp2515.readMessage(&canMsg) == MCP2515::ERROR_OK) {
    canBusTimeoutTimer = millis();  //refresh canbus timer

    // :::brake lights:::

    if (canMsg.can_id == 209) {
      //save brake pressure to check if we should flash brake lights
      brakePressure = canMsg.data[2];

      //set brake request to true
      if (brakePressure > 0) {
        brakeRequest = true;
      } else {
        brakeRequest = false;
      }
    } else if (canMsg.can_id == 212) {
      //save vehicleSpeed
      vehicleSpeed = ((canMsg.data[0] + canMsg.data[1] + canMsg.data[2] + canMsg.data[3]) / 4) * 0.05747;  //double check multiplier and INDEX, maybe replace with average of wheel speed sensors

      //check if we should flash brakelights
      if (false) {  //TODO
        brakeLightFlashing = true;
        brakeFlashTimer = millis();
      }
    } else if (canMsg.can_id == 1) { //from transceiver module --> TODO: figure out what data means what
      //assign turn signals
      turnSignalState = canMsg.data[0];
      //assign runningLights
      runningLightState = (canMsg.data[0] == 1) ? true : false;
      //assign flash request
    } else if (canMsg.can_id == 2) {
      //assign running light state runningLightState
      //this CAN msg should persist but doesn't need to be high frequency
    } else if (canMsg.can_id == 3) {
      //assign warning flash STATE (don't use requests)
      //these CAN packets should persist
    }
  }

  //  :::execute light functions:::

  //clear light states
  clearLightStates();

  //handle running lights
  handleRunningLights();

  //handle light cycles

  //handle turn signal states
  handleTurnSignals();

  //handle brake lights
  handleBrakeLights();

  //execute states
  showLights();

  //  :::CAN timeout shutdown:::

  //shut down if we've exceeded the CANBUS timeout timer
  if (millis() - canBusTimeoutTimer > CANBUS_TIMEOUT) {    
    digitalWrite(KILL_PIN, HIGH);
  }
}

//solely clears light states
void clearLightStates() {
  //set all MOSFET channels to off state
  for (int i = 0; i < sizeof(M_PINS); i++) {
    M_STATES[i] = false;
  }

  //clear NP channels but do not show to avoid flickering
  FR_BUMP_NP.clear();
  LIGHTBARS_NP.clear();
  TRUNK_NP.clear();
  R_BUMP_NP.clear();
}

//turns lights off and clears states
void lightsOff() {
  clearLightStates();

  //clear NP channels
  FR_BUMP_NP.show();
  LIGHTBARS_NP.show();
  TRUNK_NP.show();
  R_BUMP_NP.show();
}

//displays all stored light states
void showLights() {
  //show MOSFET channels
  for (int i = 0; i < sizeof(M_PINS); i++) {
    digitalWrite(i, M_STATES[i]);
  }

  //show NP channels
  FR_BUMP_NP.show();
  LIGHTBARS_NP.show();
  TRUNK_NP.show();
  R_BUMP_NP.show();
}

//handles running light behaviour
void handleRunningLights() {
  if (runningLightState) {
    M_STATES[BL_RUN] = MOSFET_ON_STATE;

    //set fourth brake light to dim
    R_BUMP_NP.setPixelColor(2, 50, 50, 50);
  }
}

//handles turn signal behaviour
void handleTurnSignals() {

  //we should not be flashing a turn signal if we've exceeded the minimum number of flashes and the turn signal state is 0
  if ((millis() - turnSignalTimer) > (2*MIN_TURN_SIG_FLASH*TURN_SIGNAL_TIME_STEP) && turnSignalState == 0) {
    return;
  }

  //nullify all turn signal states to ensure other animations can't affect the turn signals when we're indicating
  M_STATES[L_HL_IND] = false;
  M_STATES[L_BL_IND] = false;
  M_STATES[R_HL_IND] = false;
  M_STATES[R_BL_IND] = false;

  //now we flash
  int turnSignalTimerMOD = turnSignalTimer % (2*TURN_SIGNAL_TIME_STEP); //this should result in a number between 0 and 999 which allows us to figure out where we should be within the flash state
  if (turnSignalTimerMOD < TURN_SIGNAL_TIME_STEP) {
    if (turnSignalState == -1) { //we goin left
      M_STATES[L_HL_IND] = true;
      M_STATES[L_BL_IND] = true;
    } else if (turnSignalState == 1) { //we goin right
      M_STATES[R_HL_IND] = true;
      M_STATES[R_BL_IND] = true;
    }
  } else {
    if (turnSignalState == -1) { //we goin left
      M_STATES[L_HL_IND] = false;
      M_STATES[L_BL_IND] = false;
    } else if (turnSignalState == 1) { //we goin right
      M_STATES[R_HL_IND] = false;
      M_STATES[R_BL_IND] = false;
    }
  }
}

//handle brake light behaviour
void handleBrakeLights() {
  if (brakeRequest) {
    //check if we should be flashing brake lights
    if (brakeLightFlashing) { //handle brake flashing animation   
      //we should not be flashing brake lights if we've exceeded the minimum number of flashes
      if (millis() - brakeFlashTimer > BRAKE_FLASH_MINIMUM_LENGTH) {
        brakeLightFlashing = false;
      }

      //if we haven't exceeded the max flash number, flash brake lights
      if ((((millis() - brakeFlashTimer) / BRAKE_FLASH_TIME_STEP)) % 2 != 0) { //check if we are in first 'time step' of cycle (this cycle has 2 steps)
        //set MOSFETs on
        M_STATES[L_BL_BRK] = MOSFET_ON_STATE;
        M_STATES[R_BL_BRK] = MOSFET_ON_STATE;

        //set fourth brake light on
        R_BUMP_NP.setPixelColor(2, 255, 255, 255);
      } else {
        //set MOSFETs off
        M_STATES[L_BL_BRK] = MOSFET_ON_STATE;
        M_STATES[R_BL_BRK] = MOSFET_ON_STATE;

        //set fourth brake light off
        R_BUMP_NP.setPixelColor(2, 0, 0, 0);
      }
    } 

    //this allows the above if to drop out and proceed with the code below if we've exceeded the minimum flash time. This prevents a brake light drop out
    if (brakeLightFlashing == false) {
      M_STATES[L_BL_BRK] = MOSFET_ON_STATE;
      M_STATES[R_BL_BRK] = MOSFET_ON_STATE;

      //set fourth brake light on
      R_BUMP_NP.setPixelColor(2, 255, 255, 255);
    }
  }
}
