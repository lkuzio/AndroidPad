#include <Servo.h>
int incomingByte = 0; 
int motorpin1 = 3;            
int motorpin2 = 4;

Servo myservo;
int pos =0;

void setup() {
  Serial.begin(9600);
  myservo.attach(9);
  myservo.write(80);
  pinMode(motorpin1,OUTPUT);        
  pinMode(motorpin2,OUTPUT);
}

void loop() {
   if (Serial.available() > 0) {
                // read the incoming byte:
                incomingByte = Serial.read();

                if((char)incomingByte == 'L'){
                  myservo.write(30);
                }
                if((char)incomingByte == 'R'){
                  myservo.write(150);
                }
                if((char)incomingByte == 'C'){
                  myservo.write(80);
                }

                if((char)incomingByte == 'F'){
                 digitalWrite(motorpin1,LOW);
                 digitalWrite(motorpin2,HIGH);
                }

                if((char)incomingByte == 'B'){
                 digitalWrite(motorpin1,HIGH);
                 digitalWrite(motorpin2,LOW);
                }

                if((char)incomingByte == 'S'){
                 digitalWrite(motorpin1,LOW);
                 digitalWrite(motorpin2,LOW);
                }
                
                // say what you got:
                Serial.print("I received: ");
                Serial.println((char)incomingByte, DEC);
  }

}

