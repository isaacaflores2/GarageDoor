#ifndef IFTTT_TRIGGER_H
#define IFTTT_TRIGGER_H

#include <ESP8266WiFi.h>
#include <WiFiClientSecure.h>
#include <garagedoor_config.h>

WiFiClientSecure iftttClient;

boolean IFTTT_Post()
{
  //Send HTTTPS Post Request to trigger IFTTT
  if(iftttClient.connect(host, port))
  {
    iftttClient.print(String("POST ") + url + " HTTP/1.1\r\n" +
           "Host: " + host + "\r\n" +
           "User-Agent: mqtt_magnetic_sensor\r\n" +
           "Connection: close\r\n\r\n");
     return true;                               
  }
  else
  {
     Serial.print("Could not connect to host: ");
     Serial.println(host);          
     return false; 
  }      
}

#endif
