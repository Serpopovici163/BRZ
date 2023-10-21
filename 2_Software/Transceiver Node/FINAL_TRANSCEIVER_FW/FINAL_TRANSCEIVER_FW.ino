#include <SPI.h>
#include <mcp2515.h>

/*
 * This code pulls data from the car's CAN network and forwards it to my CAN network with all of the added hardware. 
 * Certain values such as reverse gear state, dimmer, illumination, right/left signal, and hazard button states are
 * pulled using analog reads from the car's wiring and translated to CAN by this code. This is to elimintate the 
 * task of identifying their potentially non-existent CAN IDs within the car's CAN network and make my life easier.
 * 
 * This computer generates two CAN packets as follows:
 * 
 * ID: 1
 * DATA: [reverse (bool 0-1)] [dimmer (int 1-5)] [illumination (bool 0-1)] [key state (bool? 0-1)] //I am currently unsure about the key state but this will be pulled from CAN. This will provide computers with insight about how close my key is to the car so they can shut down when I get far from the car.
 * 
 * ID: 2
 * DATA: [left sig (bool 0-1)] [right sig (bool 0-1)] [hazard sig (bool 0-1)] [unknown] //Haven't decided on the last one yet
 * 
 * https://github.com/autowp/arduino-mcp2515
 * 
 */

#define KILL_PIN 3
#define CANBUS_TIMEOUT 500  //currently using a CANBUS_TIMEOUT but need to replace this with proper key-related shut down
#define CANBUS_MSG_DELAY 50 //dispatch custom CAN messages every 50 ms

MCP2515 carCAN(2);
MCP2515 serCAN(4);

struct can_frame canMsg;
unsigned long canBusTimeoutTimer = 0; //shuts the board down if CAN communications stop for more than CANBUS_TIMEOUT milliseconds
unsigned long canMsgTimer = 0; //this is used to ensure CAN messages based on analogread values are dispatched regularly

int canIDsWeCareAbout[2] = {209, 212};

//this computer keeps track of turn signal state
int turnSignalState = 0; //0 is none, 1 is left, 2 is right, 3 is hazard
 
void setup() {
  pinMode(KILL_PIN, OUTPUT);

  //set pinMode for turn signals
  pinMode(A3, INPUT_PULLUP);
  pinMode(A4, INPUT_PULLUP);
  pinMode(A5, INPUT_PULLUP);

  carCAN.reset();
  carCAN.setBitrate(CAN_500KBPS, MCP_8MHZ);
  carCAN.setNormalMode();

  serCAN.reset();
  serCAN.setBitrate(CAN_500KBPS, MCP_8MHZ);
  serCAN.setNormalMode();
}

void loop() {
  //handle CAN messages and forward select messages
  if (carCAN.readMessage(&canMsg) == MCP2515::ERROR_OK) {
    canBusTimeoutTimer = millis();  //refresh canbus timer

    //check if this is a CAN message we care for and forward it if so
    for (int i = 0; i < sizeof(canIDsWeCareAbout); i++) {
      if (canMsg.can_id == canIDsWeCareAbout[i])
        serCAN.sendMessage(&canMsg);
    }
  }

  //NOTES
  //A1 is reverse
  //A0 and A2 are running light related but one is dimmer 
  //just gonna use A0 for now. Might drop running light signal from BCU in exchange for ACC PWR signal from head unit. Could be useful to decide when turn signals should register in a non-CAN-way (hopefully less processing and therefore faster but also slightly jankier IG)

  //update turn signal state
  if (digitalRead(A5) == LOW) { //hazard
    //right
    turnSignalState = 3;
  } else if (digitalRead(A3) == LOW) { //right
    //hazard
    turnSignalState = 2;
  } else if (digitalRead(A4) == LOW) { //left
    //left
    turnSignalState = 1;
  } else {
    turnSignalState = 0;
  }

  //send analogread based CAN frames
  if (millis() - canMsgTimer > CANBUS_MSG_DELAY) { //modify turn signals to be on first byte of can msg 0 off, 1 haz, 2 left, 3 right
    struct can_frame frame;
    frame.can_id = 1;
    frame.can_dlc = 7;
    frame.data[0] = (analogRead(A0) > 500) ? 0 : 1;
    frame.data[1] = (analogRead(A1) > 500) ? 1 : 0; //reverse gear state
    frame.data[2] = (analogRead(A2) > 500) ? 1 : 0; //running light state
    frame.data[3] = turnSignalState;

    serCAN.sendMessage(&frame);
  }

  //shut down if we've exceeded the CANBUS timeout timer
  if (millis() - canBusTimeoutTimer > CANBUS_TIMEOUT) {
    digitalWrite(KILL_PIN, HIGH);
  }
}
