package garagedoor.SpringBootMqttHttpsBridge;

import garagedoor.config.Config;
import garagedoor.iot.device.DeviceManager;
import garagedoor.mqtt.MqttClientSetup;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractBridge<T> implements Bridge, MqttClientSetup {

    protected DeviceManager<T> deviceManager;
    protected Config config;

    //MQTT Client members
    protected MqttClient mqttClient;
    protected boolean isClientSetup = false;
    protected boolean connected = false;

    //MQTT Broker
    protected int qos=2;
    protected String broker;
    protected String clientId;
    protected String mqttUsername;
    protected String mqttPassword;
    protected MemoryPersistence persistence;
    protected boolean sensorUpdate = false;

    //MqttDevice and client managers
    protected String[] topics;

    protected Logger logger;
    protected String lineSeperator;

    public AbstractBridge(){
        mqttClient = null;
        isClientSetup = false;
        connected = false;

        qos = 2;
        broker = null;
        clientId = null;
        mqttUsername = null;
        mqttPassword = null;
        persistence = new MemoryPersistence();
        sensorUpdate = false;

        topics = null;
        deviceManager = null;
        config = null;

        logger = LoggerFactory.getLogger(MqttBridge.class);
        lineSeperator = System.lineSeparator();
    }

    @Autowired
    public AbstractBridge(Config config, DeviceManager deviceManager){
        this();
        this.config = config;
        this.deviceManager = deviceManager;
    }

    public abstract void run();

    protected void printMqttException(MqttException e, String msg)
    {
        logger.error("Mqtt Exception: " + msg + "MqttException Details: "
                +  e.getMessage() +  " Reason: "+ e.getReasonCode() + " loc " + e.getLocalizedMessage()
                + " cause "+ e.getCause());

        e.printStackTrace();
    }

    public abstract void subscribe();
    public abstract String publish(String deviceId, String content);

    public boolean isConnected() {
        return connected;
    }

    public void loadConfig(){
        logger.info("Loading config files...");

        if(config == null)
        {
            logger.error("Config is null! " + lineSeperator
                    + "Check your application properties files for requried MqttBridgeClient configuration parameters:" + lineSeperator
                    + "1) mqttTopics 2) mqttClientId 3) mqttBrokerUsername 4) MqttBrokerPassword 5) mqttBrokerAddress");
            return;
        }

        logger.info("Config broker value: " + config.mqttBrokerAddress);
        topics = config.mqttTopics;
        clientId = config.mqttClientId;
        mqttUsername = config.mqttBrokerUsername;
        mqttPassword = config.mqttBrokerPassword;
        broker = config.mqttBrokerAddress;

        try
        {
            //Create MqttClient Instance
            mqttClient = new MqttClient(broker, generateClientId(), persistence);
        }
        catch (MqttException e)
        {
            printMqttException(e, "Mqtt Client Setup Exeception! " );
        }
    }

    public abstract void setup();

    //Generates unique client getId to prevent client already connected exceptin with Broker
    public String generateClientId()
    {
        return clientId + System.nanoTime();
    }

}
