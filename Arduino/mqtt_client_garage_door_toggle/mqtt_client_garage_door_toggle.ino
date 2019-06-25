
#include <mqtt_client.h>
#include <ifttt_trigger.h>
#include <garagedoor_config.h>

//MQTT Client information
String mqtt_client_id = "garage_toggle";
String mqtt_topic = "garage/door/toggle";

//Constants
const uint8_t BUFFER_LEN = 6;
const char* door_state_names[2] = {"open", "closed"};
const char* device_request[1] =  {"toggle"}; 

//BoardIO Pins
uint8_t mqtt_connection_output = 5; //D1
uint8_t device_input = 0; //Not used
uint8_t device_output = 13; //D7


BearSSL::WiFiClientSecure wifiClient; 
BearSSL::X509List x509CaCert(CA_CERT_PROG);
MqttClient mqttClient(mqtt_server, mqtt_username, mqtt_password, mqtt_port, mqtt_client_id, mqtt_topic); 


//Mqtt Callback function
void callback(char* topic, byte* payload, unsigned int length)
{
  //Create copy of tempory payload buffer since this buffer is used for following mqtt messages
  byte* payload_copy = (byte*) malloc(length);
    
  //Add null bytes to message buffer (reset buffer)
  char message[BUFFER_LEN];
  strncpy(message, "\0", BUFFER_LEN);

  //Copy payload to message
  strncpy(message, (char*) payload, length);

  if( ( strcmp(topic, mqtt_topic.c_str()) == 0 ) && (strcmp(message, device_request[0]) == 0 ) )
  {    
    digitalWrite(device_output, 1); 
    delay(500); 
    digitalWrite(device_output, 0); 
  }
  
  free(payload_copy);   
  return;
}


void setup()
{
  wifiClient.setTrustAnchors(&x509CaCert);
  mqttClient.initBoardIO(mqtt_connection_output, device_input, device_output);
  mqttClient.setWifiClient(wifiClient); 
  mqttClient.setup(callback);
}

void loop()
{ 
  mqttClient.loop();
}
