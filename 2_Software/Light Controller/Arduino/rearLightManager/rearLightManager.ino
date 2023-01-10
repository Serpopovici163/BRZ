#include <SPI.h>
#include <mcp2515.h>
#include <Adafruit_NeoPixel.h>

#define BRAKE_FLASH_MINIMUM_LENGTH 1500
<<<<<<< Updated upstream
<<<<<<< Updated upstream
#define BRAKE_FLASH_TIMER 100
#define LIGHT_CYCLE_TIME_STEP 100
=======
#define BRAKE_FLASH_TIMER 50
#define LIGHT_CYCLE_TIME_STEP 50
#define CANBUS_TIMEOUT 500
>>>>>>> Stashed changes
=======
#define BRAKE_FLASH_TIMER 50
#define LIGHT_CYCLE_TIME_STEP 50
#define CANBUS_TIMEOUT 500
>>>>>>> Stashed changes

struct can_frame canMsg;
MCP2515 mcp2515(53);

//sensory pins
int leftSignalSense = A1; //brake sense not needed since we can read data from CAN
int rightSignalSense = A2;

//output relays
int leftBrakeRelay = 17;
int rightBrakeRelay = 19;
int leftSignalRelay = 21;
int rightSignalRelay = 23;
<<<<<<< Updated upstream
<<<<<<< Updated upstream
int topBrakeRelay = 25;
int licensePlateRelay = 27; //for blackout use only, solid state

//led strips
Adafruit_NeoPixel fourthBrakeLight(3, 3, NEO_GRB + NEO_KHZ800); //may need to change pin definition, pixel 0 and 2 have R and B reversed here
Adafruit_NeoPixel rearSideLights(2, 5, NEO_GRB + NEO_KHZ800); //id 0 is left window, 1 is third brake light, 2 is right window
=======
int topBrakeRelay = 32;
int licensePlateRelay = 27; //for blackout use only, solid state

//led strips
Adafruit_NeoPixel fourthBrakeLight(3, 5, NEO_GRB + NEO_KHZ800); //may need to change pin definition, pixel 0 and 1 have R and B reversed here, 2 is 4th brake
Adafruit_NeoPixel rearSideLights(3, 3, NEO_GRB + NEO_KHZ800); //id 0 is left window, 1 is third brake light, 2 is right window
>>>>>>> Stashed changes
=======
int topBrakeRelay = 32;
int licensePlateRelay = 27; //for blackout use only, solid state

//led strips
Adafruit_NeoPixel fourthBrakeLight(3, 5, NEO_GRB + NEO_KHZ800); //may need to change pin definition, pixel 0 and 1 have R and B reversed here, 2 is 4th brake
Adafruit_NeoPixel rearSideLights(3, 3, NEO_GRB + NEO_KHZ800); //id 0 is left window, 1 is third brake light, 2 is right window
>>>>>>> Stashed changes

//timer/status variables
long brakeFlashCycleStart = 0; //keep this separate to ensure seamless flashing of brake lights
int brakeFlashInitiateTime = 0; //used to keep track of initial flash trigger. is tracked to make sure brake lights flash for a minimum amount of seconds
bool isBrakeLightFlashing = false;
long lightCycleStart = 0;
long canBusTimeoutTimer = 0;
int cycleState = 0; //0 none, 1 fast police, 2 slow police, 3 fast hazard, 4 slow hazard
int brakePressure = 0;
int vehicleSpeed = 0;

//relay statuses are written to variables such that they can be modified by a series of checks before being written
//this prevents erratic behaviour in case a higher priority check overwrites a light status mid clock cycle
bool leftBrakeState = false;
bool rightBrakeState = false;
bool leftSignalState = false;
bool rightSignalState = false;
bool topBrakeState = false;
bool licensePlateState = true;

void setup() { //TODO: add three flashes when car starts up
  mcp2515.reset();
  mcp2515.setBitrate(CAN_500KBPS, MCP_8MHZ); //TODO: FIGURE OUT
  mcp2515.setNormalMode();

  pinMode(leftBrakeRelay, OUTPUT);
  pinMode(rightBrakeRelay, OUTPUT);
  pinMode(leftSignalRelay, OUTPUT);
  pinMode(rightSignalRelay, OUTPUT);
  pinMode(topBrakeRelay, OUTPUT);
  pinMode(licensePlateRelay, OUTPUT);

  //What the fuck, over?
  pinMode(3, OUTPUT);
  pinMode(5, OUTPUT);
}

void loop() {
  //handle can message
 if (mcp2515.readMessage(&canMsg) == MCP2515::ERROR_OK) {
  canBusTimeoutTimer = millis();
    if (canMsg.can_id == 209) {
      brakePressure = canMsg.data[2];
    } else if (canMsg.can_id == 212) {
      vehicleSpeed = ((canMsg.data[0] + canMsg.data[1] + canMsg.data[2] + canMsg.data[3]) / 4) * 0.05747; //double check multiplier and INDEX, maybe replace with average of wheel speed sensors
    }  
  }

  //check if car is on based on canbus timer
  if (canBusTimeoutTimer < (millis() - CANBUS_TIMEOUT)) {
    lightsOff();
  } else {
    //clear lights
    clearLights();
  
    //handle light cycles first
  
    //forward critical signals
    forwardTurnSignalStates();
    setBrakeLightState();
    //show everything
    showLights();
  }
}

void lightsOff() {
  digitalWrite(leftSignalRelay, LOW);
  digitalWrite(rightSignalRelay, LOW);
  digitalWrite(leftBrakeRelay, LOW);
  digitalWrite(rightBrakeRelay, LOW);
  digitalWrite(topBrakeRelay, LOW);
  digitalWrite(licensePlateRelay, LOW);
  fourthBrakeLight.clear();
  fourthBrakeLight.show();
  rearSideLights.clear();
  rearSideLights.show();
}

void showLights() {

  if (isBrakeLightFlashing){
    flashBrakeLight();
  }

  digitalWrite(leftSignalRelay, leftSignalState);
  digitalWrite(rightSignalRelay, rightSignalState);
  digitalWrite(leftBrakeRelay, leftBrakeState);
  digitalWrite(rightBrakeRelay, rightBrakeState);
  digitalWrite(topBrakeRelay, topBrakeState);
<<<<<<< Updated upstream
<<<<<<< Updated upstream
  digitalWrite(licensePlateRelay, !licensePlateState); //low is active, this is to waste less power
=======
  digitalWrite(licensePlateRelay, !licensePlateState);
>>>>>>> Stashed changes
=======
  digitalWrite(licensePlateRelay, !licensePlateState);
>>>>>>> Stashed changes
  fourthBrakeLight.show();
  rearSideLights.show();
}

void clearLights() {
  leftSignalState = false;
  rightSignalState = false;
  leftBrakeState = false;
  rightBrakeState = false;
  topBrakeState = false;
  licensePlateState = true;
  fourthBrakeLight.clear();
  //set fourth brake light to dim by default
  fourthBrakeLight.setPixelColor(2, 50, 50, 50); //TODO: check brightness
  rearSideLights.clear();
}

void forwardTurnSignalStates() {
  if (analogRead(leftSignalSense) > 800) {
    leftSignalState = true;
  } 
  if (analogRead(rightSignalSense) > 800) {
    rightSignalState = true;
  }
}

void setBrakeLightState() {
  //check for speed vs brake pedal % stuff here
  //add boolean to track when flash is initiated. 
  //TODO: modify values from km/h to CANBUS readings or adjust vehicleSpeed to be km/h
  if (vehicleSpeed >= 60 && brakePressure >= 30) {
    brakeFlashInitiateTime = millis();
    isBrakeLightFlashing = true;
  } else if (vehicleSpeed >= 40 && brakePressure >= 25) {
    brakeFlashInitiateTime = millis();
    isBrakeLightFlashing = true;
  } else if (vehicleSpeed >= 20 && brakePressure >= 16) {
    brakeFlashInitiateTime = millis();
    isBrakeLightFlashing = true;
  } else if (brakePressure != 0) {
    //turn brake lights on but no flash
    leftBrakeState = true;
    rightBrakeState = true;
    topBrakeState = true;
    fourthBrakeLight.setPixelColor(2, 255, 255, 255);
  } 
  //clear brake timer if minimum time was elapsed
  if (brakeFlashInitiateTime < (millis() - BRAKE_FLASH_MINIMUM_LENGTH)) {
    isBrakeLightFlashing = false;
  }
}

void flashBrakeLight() {
  long currentTime = millis();
  if (brakeFlashCycleStart >= (currentTime - BRAKE_FLASH_TIMER)) {
    //turn lights on
    leftBrakeState = true;
    rightBrakeState = true;
    topBrakeState = true;
    fourthBrakeLight.setPixelColor(2, 255, 255, 255);
  } else if (brakeFlashCycleStart >= (currentTime - 2*BRAKE_FLASH_TIMER)) {
    //turn lights off
    leftBrakeState = false;
    rightBrakeState = false;
    topBrakeState = false;
    fourthBrakeLight.setPixelColor(2, 0, 0, 0);
  } else {
    //turn lights off and reset timer
    leftBrakeState = false;
    rightBrakeState = false;
    topBrakeState = false;
    fourthBrakeLight.setPixelColor(2, 0, 0, 0);
    brakeFlashCycleStart = millis();
  }
}

void fastPoliceCycle() {
  //TODO: integrate third brake light as addressable LED
  long currentTime = millis();
  licensePlateState = false; //disable license plate light
  if (lightCycleStart >= (currentTime - LIGHT_CYCLE_TIME_STEP)) {
    //timestep 1
    fourthBrakeLight.setPixelColor(0, 0, 255, 0);
    rearSideLights.setPixelColor(0, 255, 0, 0);
    rearSideLights.setPixelColor(1, 255, 0, 0);
    rightBrakeState = true;
  } else if (lightCycleStart >= (currentTime - 2*LIGHT_CYCLE_TIME_STEP)) {
    //timestep 2
    rearSideLights.setPixelColor(0, 255, 0, 0);
    rearSideLights.setPixelColor(1, 255, 0, 0);
  } else if (lightCycleStart >= (currentTime - 3*LIGHT_CYCLE_TIME_STEP)) {
    //timestep 3
    fourthBrakeLight.setPixelColor(0, 0, 255, 0);
    rearSideLights.setPixelColor(2, 0, 0, 255);
    rearSideLights.setPixelColor(1, 0, 0, 255);
    rightBrakeState = true;
  } else if (lightCycleStart >= (currentTime - 4*LIGHT_CYCLE_TIME_STEP)) {
    //timestep 4
    rearSideLights.setPixelColor(2, 0, 0, 255);
    rearSideLights.setPixelColor(1, 0, 0, 255);
  } else if (lightCycleStart >= (currentTime - 5*LIGHT_CYCLE_TIME_STEP)) {
    //timestep 5
    fourthBrakeLight.setPixelColor(1, 0, 0, 255);
    rearSideLights.setPixelColor(0, 255, 0, 0);
    rearSideLights.setPixelColor(1, 255, 0, 0);
    leftBrakeState = true;
  } else if (lightCycleStart >= (currentTime - 6*LIGHT_CYCLE_TIME_STEP)) {
    //timestep 6
    rearSideLights.setPixelColor(0, 255, 0, 0);
  } else if (lightCycleStart >= (currentTime - 7*LIGHT_CYCLE_TIME_STEP)) {
    //timestep 7
    fourthBrakeLight.setPixelColor(1, 0, 0, 255);
    rearSideLights.setPixelColor(2, 0, 0, 255);
    rearSideLights.setPixelColor(1, 255, 0, 0);
    leftBrakeState = true;
  } else if (lightCycleStart >= (currentTime - 8*LIGHT_CYCLE_TIME_STEP)) {
    //timestep 8
    rearSideLights.setPixelColor(2, 0, 0, 255);
  } else if (lightCycleStart >= (currentTime - 9*LIGHT_CYCLE_TIME_STEP)) {
    //timestep 9
    fourthBrakeLight.setPixelColor(0, 0, 255, 0);
    rearSideLights.setPixelColor(0, 255, 0, 0);
    rearSideLights.setPixelColor(1, 0, 0, 255);
    rightBrakeState = true;
  } else if (lightCycleStart >= (currentTime - 10*LIGHT_CYCLE_TIME_STEP)) {
    //timestep 10
    fourthBrakeLight.setPixelColor(0, 0, 255, 0);
  } else if (lightCycleStart >= (currentTime - 11*LIGHT_CYCLE_TIME_STEP)) {
    //timestep 11
    fourthBrakeLight.setPixelColor(1, 0, 0, 255);
    rearSideLights.setPixelColor(0, 255, 0, 0);
    rearSideLights.setPixelColor(1, 0, 0, 255);
    rightBrakeState = true;
  } else if (lightCycleStart >= (currentTime - 12*LIGHT_CYCLE_TIME_STEP)) {
    //timestep 12
    fourthBrakeLight.setPixelColor(1, 0, 0, 255);
  } else if (lightCycleStart >= (currentTime - 13*LIGHT_CYCLE_TIME_STEP)) {
    //timestep 13
    fourthBrakeLight.setPixelColor(0, 0, 255, 0);
    rearSideLights.setPixelColor(2, 0, 0, 255);
    rearSideLights.setPixelColor(1, 255, 0, 0);
    leftBrakeState = true;
  } else if (lightCycleStart >= (currentTime - 14*LIGHT_CYCLE_TIME_STEP)) {
    //timestep 14
    fourthBrakeLight.setPixelColor(0, 0, 255, 0);
    rearSideLights.setPixelColor(1, 255, 0, 0);
  } else if (lightCycleStart >= (currentTime - 15*LIGHT_CYCLE_TIME_STEP)) {
    //timestep 15
    fourthBrakeLight.setPixelColor(1, 0, 0, 255);
    rearSideLights.setPixelColor(2, 0, 0, 255);
    rearSideLights.setPixelColor(1, 0, 0, 255);
    leftBrakeState = true;
  } else if (lightCycleStart >= (currentTime - 16*LIGHT_CYCLE_TIME_STEP)) {
    //timestep 16
    fourthBrakeLight.setPixelColor(1, 0, 0, 255);
    rearSideLights.setPixelColor(1, 0, 0, 255);
  } else {
    lightCycleStart = millis();
  }
}

void fastHazardCycle() {
  long currentTime = millis();
  if (lightCycleStart >= (currentTime - LIGHT_CYCLE_TIME_STEP)) {
    //timestep 1
  } else if (lightCycleStart >= (currentTime - 2*LIGHT_CYCLE_TIME_STEP)) {
    //timestep 2
  } else if (lightCycleStart >= (currentTime - 3*LIGHT_CYCLE_TIME_STEP)) {
    //timestep 3
  } else if (lightCycleStart >= (currentTime - 4*LIGHT_CYCLE_TIME_STEP)) {
    //timestep 4
  } else if (lightCycleStart >= (currentTime - 5*LIGHT_CYCLE_TIME_STEP)) {
    //timestep 5
  } else if (lightCycleStart >= (currentTime - 6*LIGHT_CYCLE_TIME_STEP)) {
    //timestep 6
  } else if (lightCycleStart >= (currentTime - 7*LIGHT_CYCLE_TIME_STEP)) {
    //timestep 7
  } else if (lightCycleStart >= (currentTime - 8*LIGHT_CYCLE_TIME_STEP)) {
    //timestep 8
  } else if (lightCycleStart >= (currentTime - 9*LIGHT_CYCLE_TIME_STEP)) {
    //timestep 9
  } else if (lightCycleStart >= (currentTime - 10*LIGHT_CYCLE_TIME_STEP)) {
    //timestep 10
  } else if (lightCycleStart >= (currentTime - 11*LIGHT_CYCLE_TIME_STEP)) {
    //timestep 11
  } else if (lightCycleStart >= (currentTime - 12*LIGHT_CYCLE_TIME_STEP)) {
    //timestep 12
  } else if (lightCycleStart >= (currentTime - 13*LIGHT_CYCLE_TIME_STEP)) {
    //timestep 13
  } else if (lightCycleStart >= (currentTime - 14*LIGHT_CYCLE_TIME_STEP)) {
    //timestep 14
  } else if (lightCycleStart >= (currentTime - 15*LIGHT_CYCLE_TIME_STEP)) {
    //timestep 15
  } else if (lightCycleStart >= (currentTime - 16*LIGHT_CYCLE_TIME_STEP)) {
    //timestep 16
  } else {
    lightCycleStart = millis();
  }
}
