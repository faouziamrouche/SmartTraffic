#include <IRremote.h>
IRsend irsend;
void setup() {
  Serial.begin(9600);
}

void loop() {
 
 //for (int i = 0; i < 15;i++) {
   irsend.sendSony(0xa90, 12);
   //delay(40);
  //}
 //delay(1000);

}
