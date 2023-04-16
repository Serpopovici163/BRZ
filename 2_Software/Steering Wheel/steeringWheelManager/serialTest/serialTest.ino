#include <SPI.h>
#include <mcp2515.h>

#define MIN_CAN_MSG_DELAY 100
//update these
#define ILLUM_CAN_ID 1
#define ILLUM_CAN_INDEX 0
#define MY_CAN_ID 1

//pinout
int leftArrows = A0;
int callSourceVoiceEnter = A1;
int rightArrows = A2;
int rightBackEnter = A3;
int LEDs = 2;

//CAN breakdown --> moved to transmitCanBuffer but kept comment for reference
//int CAN_MSG[4] = {0,0,0,0}; //{source, pickup, and hangup}, {left D-pad}, {right D-pad}, {back and voice}
//CAN_MSG[0] --> 0 = nothing, 1 = source, 2 = pickup, 3 = hangup
//CAN_MSG[1] --> 0 = nothing, 1 = enter, 2 = vol+, 3 = vol-, 4 = right, 5 = left
//CAN_MSG[2] --> 0 = nothing, 1 = enter, 2 = up, 3 = down, 4 = right, 5 = left
//CAN_MSG[3] --> 0 = nothing, 1 = back, 2 = voice

//holds analog read values
int stat[4] = {1023,1023,1023,1023};

//timer to limit CAN packet frequency
unsigned long timer = 0;

//CAN stuff
struct can_frame receiveCanBuffer;
struct can_frame transmitCanBuffer;
MCP2515 mcp2515(3);

void setup() {
  pinMode(leftArrows, INPUT_PULLUP);
  pinMode(callSourceVoiceEnter, INPUT_PULLUP);
  pinMode(rightArrows, INPUT_PULLUP);
  pinMode(rightBackEnter, INPUT_PULLUP);
  pinMode(LEDs, OUTPUT);
  #ifdef DEBUG 
  Serial.begin(9600);
  #endif

  mcp2515.reset();
  mcp2515.setBitrate(CAN_500KBPS, MCP_8MHZ);
  mcp2515.setNormalMode();
}

void loop() {

  stat[0] = analogRead(A0);
  stat[1] = analogRead(A1);
  stat[2] = analogRead(A2);
  stat[3] = analogRead(A3);

  transmitCanBuffer.can_id = MY_CAN_ID;
  transmitCanBuffer.can_dlc = 8;
  transmitCanBuffer.data[0] = 0;
  transmitCanBuffer.data[1] = 0;
  transmitCanBuffer.data[2] = 0;
  transmitCanBuffer.data[3] = 0;

  //source, pickup, hangup, left D-pad enter, and voice
  if (stat[1] < 30) {
    if (stat[1] <= 13)
      transmitCanBuffer.data[1] = 1;
    else if (stat[1] <= 15)
      transmitCanBuffer.data[3] = 2;
    else if (stat[1] <= 17)
      transmitCanBuffer.data[0] = 1;
    else if (stat[1] <= 21)
      transmitCanBuffer.data[0] = 3;
    else
      transmitCanBuffer.data[0] = 2;
  }
  
  //left D-pad w/o enter
  if (stat[0] < 21) {
    if (stat[0] <= 13)
      transmitCanBuffer.data[1] = 2;
    else if (stat[0] <= 15)
      transmitCanBuffer.data[1] = 3;
    else if (stat[0] <= 17)
      transmitCanBuffer.data[1] = 4;
    else
      transmitCanBuffer.data[1] = 5;
  }

  //right D-pad and back
  if (stat[3] < 960) {
    if (stat[2] <= 14)
      transmitCanBuffer.data[2] = 5;
    else if (stat[2] <= 27)
      transmitCanBuffer.data[2] = 2;
    else if (stat[2] <= 52)
      transmitCanBuffer.data[2] = 3;
    else if (stat[2] <= 120)
      transmitCanBuffer.data[2] = 4;
    else if (stat[2] <= 877)
      transmitCanBuffer.data[3] = 1;
    else
      transmitCanBuffer.data[2] = 1;
  }

  //broadcast message if we have any
  if (transmitCanBuffer.data[0] + transmitCanBuffer.data[1] + transmitCanBuffer.data[2] + transmitCanBuffer.data[3] != 0 && millis() > timer + MIN_CAN_MSG_DELAY) {
    //we have a message
    //set timer
    timer = millis();
    //broadcast update
    mcp2515.sendMessage(&transmitCanBuffer);

    #ifdef DEBUG
    Serial.println(String(transmitCanBuffer.data[0]) + String(transmitCanBuffer.data[1]) + String(transmitCanBuffer.data[2]) + String(transmitCanBuffer.data[3]));
    #endif
  }

  //check if illumination should be on
  if (mcp2515.readMessage(&receiveCanBuffer) == MCP2515::ERROR_OK && receiveCanBuffer.can_id == ILLUM_CAN_ID) {
    if (receiveCanBuffer.data[ILLUM_CAN_INDEX] != 0)
      digitalWrite(LEDs, HIGH);
    else
      digitalWrite(LEDs, LOW);
  }
  
  /* //old diagnostic stuff
  Serial.println("---------------");
  Serial.print(analogRead(A0));
  Serial.print("|");
  Serial.print(analogRead(A1));
  Serial.print("|");
  Serial.print(analogRead(A2));
  Serial.print("|");
  Serial.println(analogRead(A3));
  delay(500); //*/
}
