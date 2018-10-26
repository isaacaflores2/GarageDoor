/*
 Basic ESP8266 MQTT Client 

 This sketch demonstrates the capabilities of the pubsub library in combination
 with the ESP8266 board/library.

 It connects to an MQTT server then:
  - publishes "hello world" to the topic "outTopic" every two seconds
  - subscribes to the topic "inTopic", printing out any messages
    it receives. NB - it assumes the received payloads are strings not binary
  - If the first character of the topic "inTopic" is an 1, switch ON the ESP Led,
    else switch it off

 It will reconnect to the server if the connection is lost using a blocking
 reconnect function. See the 'mqtt_reconnect_nonblocking' example for how to
 achieve the same result without blocking the main loop.

 To install the ESP8266 board, (using Arduino 1.6.4+):
  - Add the following 3rd party board manager under "File -> Preferences -> Additional Boards Manager URLs":
       http://arduino.esp8266.com/stable/package_esp8266com_index.json
  - Open the "Tools -> Board -> Board Manager" and click install for the ESP8266"
  - Select your ESP8266 in "Tools -> Board"

*/

#include <ESP8266WiFi.h>
#include <WiFiClientSecure.h>
#include <PubSubClient.h>

// Network information
const char* ssid = "";
const char* password = "";

//MQTT broker information
//const char* mqtt_server = "192.168.1.18";
const char* mqtt_server = "";
const char* mqtt_username = "";
const char* mqtt_password = "";

//MQTT Client (this device) information
const char* mqtt_client_id = "garage_magenetic_sensor";
const char* mqtt_topic = "garage/sensor/door";

//Constants
const char* doorStatus[2] = {"open", "closed"};
const char* request[1] =  {"read"};
const int BUFFER_LEN = 8; 
char message[BUFFER_LEN];

//Wifi and MQTT Clients
WiFiClientSecure espClient;
PubSubClient client(espClient);



int value = 0;
int output_val = 0; 


int mqtt_connection_indicator_output = 15; //gpio1 or D8
int magnetic_sensor_led_indicator_output = 5; //gpio5 or D1
int magnetic_sensor_input = 16; //GPIO16 or D0

void setup() {

  
  pinMode(LED_BUILTIN, OUTPUT);  
  pinMode(mqtt_connection_indicator_output, OUTPUT); 
  pinMode(magnetic_sensor_led_indicator_output, OUTPUT); 
  pinMode(magnetic_sensor_input, INPUT); 
  
  output_val = 0; 
  digitalWrite(LED_BUILTIN, 0);  
  digitalWrite(magnetic_sensor_led_indicator_output, 0);
  digitalWrite(mqtt_connection_indicator_output, 0);
  
  Serial.begin(115200);

  //Connect to wifi network
  setup_wifi();

  //Start MQTT server and set the callback function
  client.setServer(mqtt_server, 8883);
  client.setCallback(callback);
}

int getSensorStatus()
{
  if(digitalRead(magnetic_sensor_input) == 0)
  {
    //Serial.println("Magnetic sensor senses a magnet");
    digitalWrite(magnetic_sensor_led_indicator_output, 1);
    return 1; 
  }
  else
  {
    //Serial.println("Magnetic sensor does not sense a magnet");
    digitalWrite(magnetic_sensor_led_indicator_output, 0);
    return 0; 
  }
}

void setup_wifi() 
{

  
  delay(10);
  
  // We start by connecting to a WiFi network
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);
  WiFi.mode(WIFI_STA); 
  WiFi.begin(ssid, password);

  //Toogle builtin led on board while there is no connection
  while (WiFi.status() != WL_CONNECTED) 
  {
    delay(500);
    Serial.print(".");
    output_val = !output_val; 
    digitalWrite(LED_BUILTIN, output_val);
  }
 
  digitalWrite(LED_BUILTIN, 1);
  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
  
}


///Callback function for MQTT client
void callback(char* topic, byte* payload, unsigned int length) 
{
  byte* payload_copy = (byte*) malloc(length);
  memcpy(payload_copy,payload,length);  
  strncpy(message, "\0", BUFFER_LEN);
  strncpy(message, (char*) payload_copy, length);
  
  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("] ");
  Serial.println(message);
  
  if( (strcmp(topic, mqtt_topic) == 0 ) && (strcmp(message, request[0]) == 0 ) )
  {
    if( getSensorStatus() )
      client.publish( mqtt_topic, doorStatus[1] );    
    else
      client.publish( mqtt_topic , doorStatus[0]);    
  }
  
  free(payload_copy);  
}


///Function attempts to connect to MQTT Broker
void reconnect() 
{
  // Loop until we're reconnected
  while (!client.connected()) 
  {
    digitalWrite(mqtt_connection_indicator_output, 0); 
    Serial.print("Attempting MQTT connection to ");
    Serial.print(mqtt_server);
    Serial.print("....");
    
    // Attempt to connect
    if (client.connect(mqtt_client_id, mqtt_username, mqtt_password)) 
    {
      Serial.println("connected");
      // Once connected, publish an announcement...
      // client.publish("outTopic", "hello world");
      // ... and resubscribe
      client.subscribe(mqtt_topic);
      digitalWrite(mqtt_connection_indicator_output, 1); 
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


///Main loop for for esp8266 board 
void loop() 
{
 //If MQTT client is not connected, attempt to reconnect
  if (!client.connected()) 
  {   
    reconnect();
  } 
  getSensorStatus();
  client.loop();     
}
