//int M_PINS[12] = { 12, 45, 29, 31, 46, 44, 30, 28, 25, 27, 24 };
int M_PINS[12] = { 29, 31, 30, 28, 25, 27, 26, 24, 12, 45, 46, 44 };

void setup() {
  //set MOSFET pins to OUTPUT
  for (int i = 0; i < sizeof(M_PINS); i++) {
    pinMode(M_PINS[i], OUTPUT);
  }

  pinMode(32, OUTPUT);
}

void loop() {
  //set MOSFET pins on
  for (int i = 0; i < sizeof(M_PINS); i++) {
    digitalWrite(M_PINS[i], HIGH);
  }
  delay(500);
  //set MOSFET pins off
  for (int i = 0; i < sizeof(M_PINS); i++) {
    digitalWrite(M_PINS[i], LOW);
  }
  delay(500);

  //kill after 10s
  if (millis() > 10000) {
    digitalWrite(32, HIGH);
  }
}
