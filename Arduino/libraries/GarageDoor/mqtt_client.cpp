#include "mqtt_client.h"
#include "my_ota.h"
#include "setup_wifi.h"

MqttClient::MqttClient()
{
  mqtt_broker_address = ""; 
  mqtt_broker_username = ""; 
  mqtt_broker_password = ""; 
  mqtt_broker_port = 0; 
  
  mqtt_client_id = ""; 
  mqtt_topic = "";     
}

MqttClient::MqttClient(String mqtt_broker_address, String mqtt_broker_username, String mqtt_broker_pass, int mqtt_broker_port, String mqtt_client_id, String mqtt_topic)
{
  this->mqtt_broker_address = mqtt_broker_address; 
  this->mqtt_broker_username = mqtt_broker_username; 
  this->mqtt_broker_password = mqtt_broker_pass; 
  this->mqtt_broker_port = mqtt_broker_port; 
  
  this->mqtt_client_id = mqtt_client_id; 
  this->mqtt_topic = mqtt_topic;     
}

void MqttClient::setup(MQTT_CALLBACK_SIGNATURE)
{
  
  Serial.begin(115200);
  Serial.println("MqttClient Setup");
    
  //Define input and output pins
  pinMode(LED_BUILTIN, OUTPUT);
  pinMode(mqtt_connection_output, OUTPUT); 
  pinMode(device_output, OUTPUT); 
  pinMode(device_input, INPUT); 
  
  //Initialize output pins
  digitalWrite(LED_BUILTIN, 0); 
  digitalWrite(device_output, 0);
  digitalWrite(mqtt_connection_output, 0);
  
  //Connect to wifi network
  setup_wifi();

  //Setup Over the Air update
  setup_ota(); 
  
  //Start MQTT Client and set the callback function
  client.setClient(espClient); 
  client.setServer(mqtt_broker_address.c_str(), mqtt_broker_port);
  client.setCallback(callback);
}

void MqttClient::initBoardIO(uint8_t mqtt_connection_output, uint8_t device_input, uint8_t device_output)
{
  this->mqtt_connection_output = mqtt_connection_output;
  this->device_input = device_input;
  this->device_output = device_output;
}

boolean MqttClient::publish(const char* topic, const char* message)
{
 return client.publish(topic, message);  
}

boolean MqttClient::subscribe(const char* topic)
{
  return client.subscribe(topic); 
}

boolean MqttClient::loop()
{

  if (!client.connected()) 
  {   
    reconnect();
  }   
  
  //onSensorUpdate();   
  ArduinoOTA.handle();  
  return client.loop();   
}

//Function attempts to connect to MQTT Broker
void MqttClient::reconnect()
{
  Serial.println("MqttClient Reconnect");  
  // Loop until we're reconnected
  
  while (!client.connected()) 
  {
    digitalWrite(mqtt_connection_output, 0); 
    Serial.print("Attempting MQTT connection to ");
    Serial.print(mqtt_broker_address);
    Serial.print("....");
    
    // Attempt to connect
    if (client.connect(mqtt_client_id.c_str(), mqtt_broker_username.c_str(), mqtt_broker_password.c_str() )) 
    {
      Serial.println("connected");
      client.subscribe(mqtt_topic.c_str());
      digitalWrite(mqtt_connection_output, 1); 
    } 
    else 
    {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      
      // Wait 5 seconds before retrying
      delay(5000);
    }
  } 
}

uint8_t MqttClient::getState()
{
  return state; 
}

void MqttClient::setState(uint8_t state)
{
	this->state = state;  
}



