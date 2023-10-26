#include <SPI.h>
#include <mcp2515.h>

/*
 * This code simply turns the CANSW PCB off after CANBUS_TIMEOUT
 * 
 */

#define KILL_PIN 3
#define CANBUS_TIMEOUT 500  //currently using a CANBUS_TIMEOUT but need to replace this with proper key-related shut down

MCP2515 CAN(4);

struct can_frame canMsg;
unsigned long canBusTimeoutTimer = -1; //shuts the board down if CAN communications stop for more than CANBUS_TIMEOUT milliseconds
 
void setup() {
  pinMode(KILL_PIN, OUTPUT);

  CAN.reset();
  CAN.setBitrate(CAN_500KBPS, MCP_8MHZ);
  CAN.setNormalMode();
}

void loop() {
  //handle CAN messages and forward select messages
  if (CAN.readMessage(&canMsg) == MCP2515::ERROR_OK) {
    canBusTimeoutTimer = millis();  //refresh canbus timer
  }

  //shut down if we've exceeded the CANBUS timeout timer
  if (millis() - canBusTimeoutTimer > CANBUS_TIMEOUT) {
    digitalWrite(KILL_PIN, HIGH);
  }
}
