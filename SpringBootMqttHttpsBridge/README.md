# GarageDoor

This project implements a MQTT Client and HTTPS Rest controller to serve as a MQTT/HTTPS Bridge server for my WiFi connected garage door opener. The MQTT/HTTPS Bridge takes web request and communicates with a ESP8266 WiFi module via MQTT to enable the door to be opened in a browser, Android app, or via a Google Assistant voice command. This project uses the following technologies: 
- Java 
- Sprint Boot
- Eclipse Paho MQTT Software
- Maven 

---

## Detailed Project Description 
### Components
1. MqttBridge
   - This class implements the Eclipse Paho MQTT MqttCallback method to communicate with the Mosquitto Broker and update our Mqtt devices. 
2. MqttDeviceManager
   - This class encapsulates the MQTT devices to handle all read and update requests from the MqttBrigde class. 
3. MqttRestController
   - This class maps web requests to MqttBridge functions to provide the status of the MqttBridge and read/write to the MQTT devices.
4. ConfigService
   - This class reads the values from the application.properties file and provides them to the MqttBridge and MqttDeviceManager classes. 

## Dependencies 
1. [Spring Boot](https://start.spring.io/)
   - Using [Spring Initializr][https://start.spring.io/] you can easily setup all requried spring boot dependencies for your SpringBoot project. This project utilized the SpringBoot starter Web and Security dependencies. 
2. [Eclipse Paho MQTT Software](https://www.eclipse.org/paho/clients/java/)
   - Follow the instructions to add the repository definition and dependency definition to the project pom.xml. 
3. [Java JDK 1.8 ](https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html)
4. [Maven](https://maven.apache.org/install.html)

## Build and Run
### 1. Application Properties File
   - Set your application properites for the Web server, login, and the MqttHttpsBridge application. 
   - All required fields can be found in the [example_application_properties file](https://github.com/isaacaflores2/GarageDoor/blob/master/example_%20application.properties). 
### 2. Build
      - mvn clean install -Dspring.config.location=/full/path/to/application.properties
### 3. Test 
      - mvn test -Dspring.config.location=/full/path/to/application.properties
### 2. Run       
      - java -jar SpringBootMqttHttpsBridge-0.0.1.jar --spring.config.location=/full/path/to/application.properties
 
 ## To Do
 - [ ] Refactor and upload Android app files
 - [ ] Refactor and upload Arduino files for ESP8266 files
 - [ ] Add diagram for GarageDoor functionality (interaction between MqttBridge, Android, and ESP8266 modules)
