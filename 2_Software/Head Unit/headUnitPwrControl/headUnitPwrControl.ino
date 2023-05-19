#include <DHT.h>

#define FAN_MOSFET 34
#define PWR_MOSFET 32

#define DHT_PIN 9
#define CAN_CS 52
#define PWR_SENSE A2

#define STANDBY_TIME_MILLIS 5000 //5 seconds (time after power off until head unit cuts out)
#define ON_THRESHOLD 500 //analogRead value at which the head unit turns on
#define TEMP_HOT_THRESHOLD 30 //30 celsius (temp at which fans turn on)
#define THRESHOLD_DELTA 2 //2 celsius (difference between TEMP_HOT_THRESHOLD and when the fans turn off, this delta prevents fans turning on and off repeatedly --> oscillatory behaviour)

long pwrOnTime = 0; //this is a timer to turn off the head unit after STANDBY_TIME_MILLIS milliseconds
bool isFanOn = false; //used to prevent oscillatory fan behaviour
DHT dht(DHT_PIN, DHT11);

void setup() {
  pinMode(FAN_MOSFET, OUTPUT);
  pinMode(PWR_MOSFET, OUTPUT);

  dht.begin();

  //initialize CAN here
}

void loop() { 
  //check if power is on
  if (analogRead(PWR_SENSE) > ON_THRESHOLD) {
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
  if ((dht.readTemperature() > TEMP_HOT_THRESHOLD) && (analogRead(PWR_SENSE) > ON_THRESHOLD)) {
    digitalWrite(FAN_MOSFET, HIGH);
    isFanOn = true;
  } else {
    if (isFanOn) {
      if (!(dht.readTemperature() > TEMP_HOT_THRESHOLD - THRESHOLD_DELTA)) {
        //do nothing within delta region to prevent oscillatory behaviour
        digitalWrite(FAN_MOSFET, LOW);
        isFanOn = false;
      }
    }
  } //*/  
}
