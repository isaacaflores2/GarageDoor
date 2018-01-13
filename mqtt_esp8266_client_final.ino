/*
 Basic ESP8266 MQTT example

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



// Update these with values suitable for your network.

const char* ssid = "AMERICA";
const char* password = "pokemon1";
const char* mqtt_client_id = "garage_opener_esp8266";
const char* mqtt_topic1 = "garage/toggle";
const char* mqtt_topic2 = "garage/open";
const char* mqtt_server = "isaacaflores2.myddns.rocks";
const char* mqtt_username = "iflores";
const char* mqtt_password = "smarthomeoptimis13!";

WiFiClientSecure espClient;
PubSubClient client(espClient);
long lastMsg = 0;
char msg[50];
int value = 0;
int output_val = 0; 
int D7_out = 13; 

void setup() {
  pinMode(LED_BUILTIN, OUTPUT);
  pinMode(D7_out, OUTPUT);
  output_val = 0; 
  digitalWrite(LED_BUILTIN, output_val);
  digitalWrite(D7_out, 0); 
  Serial.begin(115200);
  setup_wifi();
  client.setServer(mqtt_server, 8883);
  client.setCallback(callback);

}

void toggle(){
  output_val = !output_val; 
  digitalWrite(LED_BUILTIN, output_val); 
  //Toggle transistor base voltage with output at D7
  digitalWrite(D7_out, 1); 
  delay(1000); 
  digitalWrite(D7_out, 0); 
}

void setup_wifi() {

  delay(10);
  // We start by connecting to a WiFi network
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);

  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}

void callback(char* topic, byte* payload, unsigned int length) {
  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("] ");
  for (int i = 0; i < length; i++) {
    Serial.print((char)payload[i]);
  }
  Serial.println();
  client.publish("callback good", "ack");    

  if( strcmp(topic, mqtt_topic1) == 0 ){
    toggle();
    client.publish("garage/toggle ack", "ack");    
  }
  
}

void reconnect() {
  // Loop until we're reconnected
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    // Attempt to connect
    if (client.connect(mqtt_client_id, mqtt_username, mqtt_password)) {
      Serial.println("connected");
      // Once connected, publish an announcement...
      //client.publish("outTopic", "hello world");
      // ... and resubscribe
      client.subscribe(mqtt_topic1);
      client.subscribe(mqtt_topic2);
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      // Wait 5 seconds before retrying
      delay(5000);
    }
  }
}
void loop() {
  
  if (!client.connected()) {
    reconnect();
  }
  client.loop();
   
  
}
