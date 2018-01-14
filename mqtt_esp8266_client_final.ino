/*
 ESP8266 MQTT Code using using TLS to subscribe to "toggle"
*/

#include <ESP8266WiFi.h>
#include <WiFiClientSecure.h>
#include <PubSubClient.h>



// Update these with values suitable for your network.

const char* ssid = "";
const char* password = "";
const char* mqtt_client_id = "garage_opener_esp8266";
const char* mqtt_topic1 = "garage/toggle";
const char* mqtt_topic2 = "garage/open";
const char* mqtt_server = "";
const char* mqtt_username = "";
const char* mqtt_password = "";

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
