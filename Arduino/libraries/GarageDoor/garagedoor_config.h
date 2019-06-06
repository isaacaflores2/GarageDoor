#ifndef GARAGEDOOR_CONFIG_H
#define GARAGEDOOR_CONFIG_H


//IFTTT HTTPS request constants
const char* host = "maker.ifttt.com";
const char* url = "/trigger/garage_door_opened/with/key/cU6bPnIglMLCtMhCbb12Wu";
const char* event = "garage_door_opened";
const char* key = "cU6bPnIglMLCtMhCbb12Wu";
const int port = 443;


//MQTT broker information
String mqtt_server = "isaacaflores2.myddns.rocks";
String mqtt_username = "iflores";
String mqtt_password = "smarthomeoptimis13!";
int mqtt_port = 8883 ; 

#endif
