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

//relay pins
int left_brake_light = 5;
int left_signal = 6;
int right_brake_light = 7;
int right_signal = 8;
int blackout_relays = 9; //blacks out both reverse light and license plate light

//timer values
int selectedLightCycle = 0; //same as values in Android application
long policeCycleStartTime;
long hazardCycleStartTime;

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
  
}

void fastPoliceCycle() {
  
}

void slowPoliceCycle() {
  
}

void fastHazardCycle() {
  
}

void slowHazardCycle() {
  
}
