#include <dht.h>
#include <IRremote.h>
dht DHT;


#define DHT11_PIN 7
int RECV_PIN = 11;
IRrecv irrecv(RECV_PIN);
decode_results results;
int cpt =0;
int nbC =0;
void setup(){
  Serial.begin(9600);
  irrecv.enableIRIn(); // Start the receiver
}

void loop()
{
  int chk = DHT.read11(DHT11_PIN);
  Serial.print("Temperature = ");
  Serial.println(DHT.temperature);
  Serial.print("Humidity = ");
  Serial.println(DHT.humidity);
  
  if (irrecv.decode(&results))
    { 
     //Serial.println(results.value, HEX);
     for ( int i= 0;i<15;i++)
     {
      //Serial.println(results.value != 0xA90);
      if (results.value != 0xA90)
      {
        cpt++;
        if (cpt == 15)
         {
          if (results.value != 0xFFFFFFFF)
          {
            nbC++;
            Serial.print("nb = ");
            Serial.println(nbC);
          }          
         }       
      }
     }
  irrecv.resume(); // Receive the next value    
     cpt=0;
        
    }  
 delay(2000);
}

