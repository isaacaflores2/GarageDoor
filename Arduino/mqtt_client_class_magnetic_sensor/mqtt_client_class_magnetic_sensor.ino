#include <mqtt_client.h>
#include <ifttt_trigger.h>
#include <garagedoor_config.h>

//MQTT Client information
String mqtt_client_id = "garage_magenetic_sensor";
String mqtt_topic = "garage/sensor/door";

//Constants
const uint8_t BUFFER_LEN = 6;
const char* door_state_names[2] = {"open", "closed"};
const char* sensor_request[1] =  {"read"}; 

//Board IO pins
uint8_t mqtt_connection_output = 15; //gpio1 or D8
uint8_t device_output = 4; //gpio4 or D2
uint8_t device_input = 5; //GPIO5 or D1


MqttClient mqttClient(mqtt_server, mqtt_username, mqtt_password, mqtt_port, mqtt_client_id, mqtt_topic); 

//Mqtt Callback function
void callback(char* topic, byte* payload, unsigned int length)
{
  //Create copy of tempory payload buffer
  byte* payload_copy = (byte*) malloc(length);
    
  //Add null bytes to message buffer (reset buffer)
  char message[BUFFER_LEN];
  strncpy(message, "\0", BUFFER_LEN);

  //Copy payload to message
  strncpy(message, (char*) payload, length);

  //If message was recieved for our topic and the message is a read then publish update
  if( (strcmp(topic, mqtt_topic.c_str()) == 0 ) && (strcmp(message, sensor_request[0]) == 0 ) )
  {
    if( readSensor() )
    {
      mqttClient.publish( topic, door_state_names[1] );    
    }
    else
    {
      mqttClient.publish( topic, door_state_names[0] );    
    }   
  }
  free(payload_copy);   
  return;
}

void onDeviceInputUpdate()
{
  uint8_t oldState = mqttClient.getState();
  uint8_t newState = readSensor();   
  
  if( newState != oldState)
  {
    mqttClient.publish( mqtt_topic.c_str(), door_state_names[newState] );
    
    //If garage door goes from closed state to open state
    if( oldState == 1 && newState == 0)
    {            
      IFTTT_Post();
    }    
  }  
}

uint8_t readSensor()
{
  if(digitalRead(device_input) == 0)
  {
    //Magnetic sensor detects magnet (door is closed)
    mqttClient.setState(1); 
    digitalWrite(device_output, 1);    
    return 1; 
  }
  else
  {
    mqttClient.setState(0); 
    digitalWrite(device_output, 0);
    return 0; 
  } 
}

void setup()
{
  mqttClient.initBoardIO(mqtt_connection_output, device_input, device_output);
  mqttClient.setup(callback);
}

void loop()
{ 
  mqttClient.loop();
  onDeviceInputUpdate(); 
}


