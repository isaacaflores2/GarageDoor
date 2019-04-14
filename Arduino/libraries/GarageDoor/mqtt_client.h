/*
 ESP8266 MQTT Client 

 1) This class used the PubSubClient library in combination with the ESP8266 board/library to provide 
 functionality for a MQTT client that stores the state of a sensor. 
 
 2) This class also uses the WifiManager library to create an Access point for you to provide your wifi credentials. 
 
 3) Lastly the class uses the ArduinoOTA library to provide the ability for OTA updates. 
 
*/

#ifndef MQTT_CLIENT_H
#define MQTT_CLIENT_H

#include <PubSubClient.h>
#include <WiFiClientSecure.h>


class MqttClient
{

  //MQTT Broker information
  String mqtt_broker_address; 
  String mqtt_broker_username; 
  String mqtt_broker_password;
  int mqtt_broker_port; 

  //MQTT Client information
  String mqtt_client_id;
  String mqtt_topic;

  //Sensor state
  uint8_t state; 

  //Board IO pins
  uint8_t mqtt_connection_output; //gpio1 or D8
  uint8_t device_output; //gpio4 or D2
  uint8_t device_input; //GPIO5 or D1
  
  //Wifi and MQTT Clients
  WiFiClientSecure espClient;
  PubSubClient client;
  
  public: 
  
  //Constructors
  MqttClient();
  MqttClient(String mqtt_broker_address, String mqtt_broker_username, String mqtt_broker_pass, int mqtt_broker_port, String mqtt_client_id, String mqtt_topic);

  //Setup network connections  
  void setup(MQTT_CALLBACK_SIGNATURE);

  //Assign pin values to board IO
  void initBoardIO(uint8_t mqtt_connection_output, uint8_t device_input, uint8_t device_output);
   
  ///Mqtt functions 
  boolean publish(const char* topic, const char* message); 
  boolean subscribe(const char* topic);
  boolean loop(); 
  void reconnect();
  
  //Sensor methods
  uint8_t getState();
  void setState(uint8_t state);

};


#endif


