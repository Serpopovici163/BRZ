#include <SPI.h>
#include <mcp2515.h>
#include <Adafruit_NeoPixel.h>

#define BRAKE_FLASH_MINIMUM_LENGTH 1500
#define BRAKE_FLASH_TIMER 100
#define LIGHT_CYCLE_TIME_STEP 100
#define STARTUP_FLASH_COUNT 3

#define CANBUS_TIMEOUT 500 //TODO: integrate

#define MOSFET_OFF_STATE HIGH //might change but I believe P-channel MOSFETS are off when HIGH
#define MOSFET_ON_STATE LOW
#define MIN_MOSFET_PIN 2
#define MAX_MOSFET_PIN 14 //actually pin 13 but using 14 since this is used in lots of for loops

//CAN I/O
struct can_frame canMsg;
MCP2515 mcp2515(53);

//addressable channels (appended "NP" denotes a 'NeoPixel' channel) (GPIO pins 14 thru 21)
Adafruit_NeoPixel FR_BUMP_NP(4, 14, NEO_GRB + NEO_KHZ800); //might have 6 pixels if I add extra bumper lights, should not affect cycle code much tho
Adafruit_NeoPixel HEADLIGHTS_NP(2, 15, NEO_GRB + NEO_KHZ800); //gotta change LED cound when strips are in headlights
Adafruit_NeoPixel TRUNK_NP(7, 16, NEO_GRB + NEO_KHZ800);
Adafruit_NeoPixel R_BUMP_NP(3, 17, NEO_GRB + NEO_KHZ800);

//MOSFET channels (appended "M" denotes a MOSFET channel) (GPIO pins 2 thru 13) (11-13 unused)
int R_HL_DRL_M = 2; //front DRLs (leave actual head light control to steering wheel stalk)
int L_HL_DRL_M = 3;
int R_HL_IND_M = 4; //front indicators
int L_HL_IND_M = 5;
int R_BL_BRK_M = 6; //rear brake lights (do not touch third brake light wiring)
int L_BL_BRK_M = 7;
int R_BL_IND_M = 8; //rear indicators
int L_BL_IND_M = 9;
int BL_RUN_M = 10; //rear running lights

//status vars
int lightCycleState = 0; //-1 is blackout, 0 is off/normal operation, 1 is police, 2 is fast hazard, 3 is normal hazard
bool isBrakeLightFlashing = false; //kept separate since lightCycleState can have a value while brake lights are flashing
bool runningLights = false;
bool M_states[MAX_MOSFET_PIN];

//timers
long brakeFlashCycleStart = 0;
long lightCycleStart = 0;
long canBusTimeoutTimer = 0;

void setup() {
  mcp2515.reset();
  mcp2515.setBitrate(CAN_500KBPS, MCP_8MHZ);
  mcp2515.setNormalMode();

  //kept these separate in case they change in the future
  //set NP pins to OUTPUT
  for (int i = 14; i < 22; i++) {
    pinMode(i, OUTPUT);
  }

  //set MOSFET pins to OUTPUT and set M_states array to false
  for (int i = MIN_MOSFET_PIN; i < MAX_MOSFET_PIN; i++) {
    pinMode(i, OUTPUT);
    M_states[i] = false;
  }

  //clear NP channels
  FR_BUMP_NP.clear();
  FR_BUMP_NP.show();
  HEADLIGHTS_NP.clear();
  HEADLIGHTS_NP.show();
  TRUNK_NP.clear();
  TRUNK_NP.show();
  R_BUMP_NP.clear();
  R_BUMP_NP.show();

  //startup sequence here
  for (int i = 0; i < STARTUP_FLASH_COUNT; i++) {
    R_BUMP_NP.setPixelColor(2, 255, 255, 255);
    R_BUMP_NP.show();
    delay(BRAKE_FLASH_TIMER);
    R_BUMP_NP.setPixelColor(2, 0, 0, 0);
    R_BUMP_NP.show();
    delay(BRAKE_FLASH_TIMER);
  }
}

void loop() {
  //handle CAN messages
  if (mcp2515.readMessage(&canMsg) == MCP2515::ERROR_OK) {
  canBusTimeoutTimer = millis(); //refresh canbus timer
  //assign isBrakeLightFlashing
  //assign runningLights
    /*if (canMsg.can_id == 209) {
      brakePressure = canMsg.data[2];
    } else if (canMsg.can_id == 212) {
      vehicleSpeed = ((canMsg.data[0] + canMsg.data[1] + canMsg.data[2] + canMsg.data[3]) / 4) * 0.05747; //double check multiplier and INDEX, maybe replace with average of wheel speed sensors
    }*/
  }

  //execute light functions
  //clear lights
  clearLights();

  //handle running lights
  if (runningLights) {
    //set fourth brake light to dim
    R_BUMP_NP.setPixelColor(2, 50, 50, 50);
    M_states[BL_RUN_M] = MOSFET_ON_STATE;
  }

  //handle light cycles

  //handle turn signal states

  //handle brake light states

  //handle brake light flashing

  //execute states
  showLights();
}

//clears light states
void clearLights() {
  //set all MOSFET channels to off state
  for (int i = MIN_MOSFET_PIN; i < MAX_MOSFET_PIN; i++) {
    M_states[i] = false;
  }

  //clear NP channels but do not show to avoid flickering
  FR_BUMP_NP.clear();
  HEADLIGHTS_NP.clear();
  TRUNK_NP.clear();
  R_BUMP_NP.clear();
}

//turns lights off
void lightsOff() {
  for (int i = MIN_MOSFET_PIN; i < MAX_MOSFET_PIN; i++) {
    digitalWrite(i, MOSFET_OFF_STATE);
  }
  //clear NP channels
  FR_BUMP_NP.clear();
  FR_BUMP_NP.show();
  HEADLIGHTS_NP.clear();
  HEADLIGHTS_NP.show();
  TRUNK_NP.clear();
  TRUNK_NP.show();
  R_BUMP_NP.clear();
  R_BUMP_NP.show();
}

void showLights() {
  //show MOSFET channels
  for (int i = MIN_MOSFET_PIN; i < MAX_MOSFET_PIN; i++) {
    digitalWrite(i, M_states[i]);
  }

  //show NP channels
  FR_BUMP_NP.show();
  HEADLIGHTS_NP.show();
  TRUNK_NP.show();
  R_BUMP_NP.show();  
}