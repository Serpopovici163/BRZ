#include <DHT.h>

#define FAN_MOSFET 34
#define PWR_MOSFET 32

#define DHT_PIN 9
#define CAN_CS 52
#define PWR_SENSE A2

#define STANDBY_TIME_MILLIS 30000 //30 seconds
#define TEMP_HOT_THRESHOLD 30 //30 celsius

long pwrOnTime = 0; //this is a timer to turn off the head unit after STANDBY_TIME_MILLIS milliseconds
DHT dht(DHT_PIN, DHT11);

void setup() {
  pinMode(FAN_MOSFET, OUTPUT);
  pinMode(PWR_MOSFET, OUTPUT);

  dht.begin();

  //initialize CAN here
}

void loop() { 
  //check if power is on
  if (analogRead(PWR_SENSE) > 500) {
    //if so, update timer
    pwrOnTime = millis();
  }
  
  //check if we should turn off head unit
  if (millis() - pwrOnTime >= STANDBY_TIME_MILLIS) {
    digitalWrite(PWR_MOSFET, HIGH);
  } else {
    digitalWrite(PWR_MOSFET, LOW);
  } //*/


  //check if temperature is hot and turn on fans if so
  if (dht.readTemperature() > TEMP_HOT_THRESHOLD) {
    digitalWrite(FAN_MOSFET, HIGH);
  } else {
    digitalWrite(FAN_MOSFET, LOW);
  } //*/
}
