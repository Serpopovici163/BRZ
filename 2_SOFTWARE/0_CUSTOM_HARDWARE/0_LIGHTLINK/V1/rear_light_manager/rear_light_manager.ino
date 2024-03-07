/* Rear light manager
 *  
 * Controls:  
 * --> fourth brake light
 * --> relays for actual brake lights
 * --> indicator relays
 * --> reverse and license plate blackout relays
 * --> rear windshield brake light
 * --> rear windshield LED strip
 * 
 * Listens to:
 * --> brake light voltage
 * --> accelerometer
 */

#include <Adafruit_NeoPixel.h>

//addressable LED pins
Adafruit_NeoPixel fourth_brake_light(3, 2, NEO_GRB + NEO_KHZ800); //3 LEDs, pin 2
Adafruit_NeoPixel top_strip_light(60, 3, NEO_GRB + NEO_KHZ800); //60 LEDs, pin 3
Adafruit_NeoPixel windshield_brake_light(1, 4, NEO_GRB + NEO_KHZ800); //1 LED, pin 4

//solid state relay pins
int left_brake_light = 5;
int left_signal = 6;
int right_brake_light = 7;
int right_signal = 8;

//mechanical relay pins
int blackout_relays = 9; //blacks out both reverse light and license plate light
int left_brake_power_pin = 10;
int left_signal_power_pin = 11;
int right_brake_power_pin = 12;
int right_signal_power_pin = 13;

//keep track of mechanical relay states
//used since they may change states multiple times in loop() and I want to avoid calling digitalWrite multiple times in a short time span
bool leftBrakePowerStatus = false;
bool leftSignalPowerStatus = false;
bool rightBrakePowerStatus = false;
bool rightSignalPowerStatus = false;

//analog pins
int brake_line_sense = A0

//status values
int selectedLightCycle = 0; //0 fhc, 1 shc, 2 fpc, 3 spc
bool cycleStatus = false;
bool blackoutStatus = false;
long policeCycleStartTime = 0;
long hazardCycleStartTime = 0;
long brakeFlashStartTime = 0;

void setup() {
  //initialize addressable LEDs
  fourth_brake_light.begin();
  fourth_brake_light.setBrightness(255);
  fourth_brake_light.clear();
  fourth_brake_light.show();

  top_strip_light.begin();
  top_strip_light.setBrightness(255);
  top_strip_light.clear();
  top_strip_light.show();

  windshield_brake_light.begin();
  windshield_brake_light.setBrightness(255);
  windshield_brake_light.clear();
  windshield_brake_light.show();

  //initialize relays
  pinMode(left_brake_light, OUTPUT);
  pinMode(left_signal, OUTPUT);
  pinMode(right_brake_light, OUTPUT);
  pinMode(right_signal, OUTPUT);
  pinMode(blackout_relays, OUTPUT);
}

void loop() {
  //clear all lights
  fourth_brake_light.clear();
  top_strip_light.clear();
  windshield_brake_light.clear();

  leftBrakePowerStatus = false;
  leftSignalPowerStatus = false;
  rightBrakePowerStatus = false;
  rightSignalPowerStatus = false;

  //only handle light cycle stuff if not blacked out
  if (!blackout) {
    //handle light cycle
    if (cycleStatus) {
      if (selectedLightCycle == 0) { //fhc
        fastHazardCycle();
      } else if (selectedLightCycle == 1) { //shc
        slowHazardCycle();
      } else if (selectedLightCycle == 2) { //fpc 
        fastPoliceCycle();
      } else if (selectedLightCycle == 3) { //spc, not just else statement to avoid accidental trigger
        slowPoliceCycle();
      }
    }
  
    //flashing brake lights takes prevalence over cycles
    if (analogRead(brake_line_sense) > 800) {
      flashBrakeLight(); 
    }
  } else {
    //if blacked out, turn off all lights
    digitalWrite(blackout_relays, LOW);
    digitalWrite(left_brake_light, LOW);
    digitalWrite(left_signal, LOW);
    digitalWrite(right_brake_light, LOW);
    digitalWrite(right_signal, LOW);
  }

  //call show at end of loop
  fourth_brake_light.show();
  top_strip_light.show();
  windshield_brake_light.show();
}

void fastPoliceCycle() {
  
}

void slowPoliceCycle() {
  
}

void fastHazardCycle() {
  
}

void slowHazardCycle() {
  
}

void flashBrakeLight() {
  //delay is 100ms declared in if statement conditions
  flashes all brake lights
  if (brakeFlashStartTime >= (millis() - 100)) {
    fourth_brake_light.setPixelColor(2, fourth_brake_light.Color(255, 255, 255));
    windshield_brake_light.setPixelColor(2, fourth_brake_light.Color(255, 255, 255));
    digitalWrite(left_brake_light, HIGH);
    digitalWrite(right_brake_light, HIGH);
  } else if (brakeFlashStartTime >= (millis() - 200)) {
    fourth_brake_light.setPixelColor(2, fourth_brake_light.Color(0,0,0));
    windshield_brake_light.setPixelColor(2, fourth_brake_light.Color(0,0,0));
    digitalWrite(left_brake_light, LOW);
    digitalWrite(right_brake_light, LOW);
  } else {
    brakeFlashStartTime = millis();
  }
}
