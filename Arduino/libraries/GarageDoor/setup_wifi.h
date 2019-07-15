#ifndef SETUP_WIFI_H
#define SETUP_WIFI_H

#include <FS.h>
#include <Esp.h>
#include <ESP8266WiFi.h>
#include <DNSServer.h>
#include <ESP8266WebServer.h>
#include <WiFiManager.h> 
#include <ArduinoJson.h>
#include <time.h>
#include <Ticker.h>

String chip_id = String( "AutoConnect" + String(ESP.getChipId()) );      
Ticker ticker;

void tick()
{
  //toggle state
  int state = digitalRead(BUILTIN_LED);  // get the current state of GPIO1 pin
  digitalWrite(BUILTIN_LED, !state);     // set pin to the opposite state
}

//gets called when WiFiManager enters configuration mode
void configModeCallback (WiFiManager *myWiFiManager) {
  Serial.println("Entered config mode");
  Serial.println(WiFi.softAPIP());
  //if you used auto generated SSID, print it
  Serial.println(myWiFiManager->getConfigPortalSSID());
  //entered config mode, make led toggle faster
  ticker.attach(0.2, tick);
}

void setup_wifi() 
{ 
  WiFiManager wifiManager;
  wifiManager.setAPCallback(configModeCallback);
 if (!wifiManager.autoConnect()) {
    Serial.println("Wifi manager failed to connect and hit timeout. Reseting ESP...");
    //reset and try again, or maybe put it to deep sleep
    ESP.reset();
    delay(1000);
  }

  //if you get here you have connected to the WiFi
  Serial.println("");
  Serial.print("WiFi connected");
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());
  
  ticker.detach();
  //keep LED on
  digitalWrite(BUILTIN_LED, LOW);  

}

void setClock(){
    configTime(3 * 3600, 0, "pool.ntp.org", "time.nist.gov");

    Serial.print("Waiting for NTP time sync: ");
    time_t now = time(nullptr);
    while (now < 8 * 3600 * 2) {
        delay(500);
        Serial.print(".");
        now = time(nullptr);
    }
    Serial.println("");
    struct tm timeinfo;
    gmtime_r(&now, &timeinfo);
    Serial.print("Current time: ");
    Serial.print(asctime(&timeinfo));
}


#endif
