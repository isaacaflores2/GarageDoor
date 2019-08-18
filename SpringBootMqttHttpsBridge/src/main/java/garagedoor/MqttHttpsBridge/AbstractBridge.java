package garagedoor.MqttHttpsBridge;

import garagedoor.Configurations.Config;
import garagedoor.iot.device.DeviceManager;
import garagedoor.mqtt.MqttBroker;
import garagedoor.mqtt.MqttClientSetup;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractBridge<T> implements Bridge, MqttClientSetup {

    protected Config config;
    protected DeviceManager<T> deviceManager;
    protected MqttBroker mqttBroker;
    protected MqttClient mqttClient;
    protected boolean isClientSetup = false;
    protected boolean isSubscribed = false;
    protected String[] topics;
    protected Logger logger;
    protected String lineSeperator;

    public AbstractBridge() {
        mqttClient = null;
        isClientSetup = false;
        isSubscribed = false;
        mqttBroker = null;
        topics = null;
        deviceManager = null;
        config = null;
        logger = LoggerFactory.getLogger(MqttBridge.class);
        lineSeperator = System.lineSeparator();
    }

    @Autowired
    public AbstractBridge(Config config, DeviceManager deviceManager, MqttBroker mqttBroker) {
        this();
        this.config = config;
        this.deviceManager = deviceManager;
        this.mqttBroker = mqttBroker;
    }

    public abstract void start();

    protected void printMqttException(MqttException e, String msg) {
        logger.error("Mqtt Exception: " + msg + "MqttException Details: "
                + e.getMessage() + " Reason: " + e.getReasonCode() + " loc " + e.getLocalizedMessage()
                + " cause " + e.getCause());

        e.printStackTrace();
    }

    public abstract void subscribe();

    public abstract String publish(String deviceId, String content);

    public boolean isSubscribed() {
        return isSubscribed;
    }

    public void loadConfigurationParameters() {
        logger.info("Loading config files...");

        if (config == null) {
            logger.error("Config is null! " + lineSeperator
                    + "Check your application properties files for requried MqttBridgeClient configuration parameters:" + lineSeperator
                    + "1) mqttTopics 2) mqttClientId 3) mqttBrokerUsername 4) MqttBrokerPassword 5) mqttBrokerAddress");
            return;
        }

        logger.info("Config broker value: " + config.mqttBrokerAddress);
        topics = config.mqttTopics;

        try {
            mqttClient = new MqttClient(mqttBroker.broker, generateClientId(), mqttBroker.persistence);
        } catch (MqttException e) {
            printMqttException(e, "Mqtt Client Setup Exeception! ");
        }
    }

    public abstract void connectToBroker();

    //Generates unique client getId to prevent client already connected exceptin with Broker
    public String generateClientId() {
        return mqttBroker.clientId + System.nanoTime();
    }

}
