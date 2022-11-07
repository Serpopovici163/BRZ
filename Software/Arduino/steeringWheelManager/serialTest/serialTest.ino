int leftArrows = A0;
int callSourceVoiceEnter = A1;
int rightArrows = A2;
int rightBackEnter = A3;
int LEDs = 2;

void setup() {
  pinMode(leftArrows, INPUT_PULLUP);
  pinMode(callSourceVoiceEnter, INPUT_PULLUP);
  pinMode(rightArrows, INPUT_PULLUP);
  pinMode(rightBackEnter, INPUT_PULLUP);
  pinMode(LEDs, OUTPUT);
  Serial.begin(9600);
}

void loop() {
  Serial.println("---------------");
  Serial.print(analogRead(A0));
  Serial.print("|");
  Serial.print(analogRead(A1));
  Serial.print("|");
  Serial.print(analogRead(A2));
  Serial.print("|");
  Serial.println(analogRead(A3));
  delay(500);
}
