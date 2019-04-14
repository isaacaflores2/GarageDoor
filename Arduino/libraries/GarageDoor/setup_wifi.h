#ifndef SETUP_WIFI_H
#define SETUP_WIFI_H

#include <FS.h>
#include <Esp.h>
#include <ESP8266WiFi.h>
#include <WiFiClientSecure.h>
#include <DNSServer.h>
#include <ESP8266WebServer.h>
#include <WiFiManager.h> 
#include <ArduinoJson.h>

String chip_id = String( "AutoConnect" + String(ESP.getChipId()) );      

void setup_wifi() 
{ 
  WiFiManager wifiManager;
  wifiManager.autoConnect();
  
  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());

  digitalWrite(LED_BUILTIN, 1);
}
#endif
