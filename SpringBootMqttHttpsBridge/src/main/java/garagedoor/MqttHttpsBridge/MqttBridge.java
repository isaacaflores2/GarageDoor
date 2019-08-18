package garagedoor.MqttHttpsBridge;

import garagedoor.Configurations.Config;
import garagedoor.iot.device.Device;
import garagedoor.iot.device.DeviceManager;
import garagedoor.mqtt.MqttBroker;
import garagedoor.mqtt.MqttClientSetup;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Set;

@Component
public class MqttBridge extends AbstractBridge<String> implements MqttCallback, Bridge, MqttClientSetup {

    //MQTT Client constant
    public static final String READ_COMMAND = "read";
    public static final String SENSOR_MQTT_TOPIC = "sensor";


    @Autowired
    public MqttBridge(Config config, DeviceManager deviceManager, MqttBroker mqttBroker) {
        super(config, deviceManager, mqttBroker);
    }

    public void subscribe() {
        logger.info("MqttClient is subscribing...");

        if (!mqttClient.isConnected())
            connectToBroker();

        if (isSubscribed == false) {
            subscribeToAllTopics();
        }

        logger.info("cliend has subscribed to all topics.");
    }

    private void subscribeToAllTopics() {
        try {

            if (topics.length > 0) {
                for (int i = 0; i < topics.length; i++) {
                    mqttClient.subscribe(topics[i]);
                }
            }
            isSubscribed = true;
        } catch (MqttException e) {
            printMqttException(e, "Mqtt Client Subcscribe Exeception.");
        }
    }

    public String publish(String deviceId, String content) {
        logger.info("MqttClient is publishing");

        if (!mqttClient.isConnected())
            connectToBroker();

        try {
            MqttMessage mqttMsg = new MqttMessage(content.getBytes());
            mqttMsg.setQos(mqttBroker.qos);
            String topic = deviceManager.getDevice(deviceId).getTopic();
            mqttClient.publish(topic, mqttMsg);

            logger.info(" to device id : " + deviceId + " with the message: " + mqttMsg);
            return mqttMsg.toString();
        } catch (MqttException e) {
            printMqttException(e, "Mqtt Client Public Exception.");
            return e.toString();
        }
    }

    @Override
    public void connectionLost(Throwable thrwbl) {
        logger.info("MqttClient connection lost...");

        isSubscribed = false;
        connectToBroker();

        while (!mqttClient.isConnected()) {
            logger.warn("Failed to reconnect...waiting 5 seconds to try again...");
            mqttSleep(5000);
            connectToBroker();
        }
        subscribe();
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        logger.info("Message arrived! Topic: " + topic + " with message: " + message.toString());

        //If message arrived is for a sensor and is not a read request, then update device status
        if (topic.contains(SENSOR_MQTT_TOPIC) && !message.toString().equals(READ_COMMAND)) {
            logger.info("Updating devices that subscribed to: " + topic);

            String id = deviceManager.getDeviceFromTopic(topic).getId();
            deviceManager.updateDeviceData(id, message.toString());
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) {
        logger.debug("Message recieved by the broker!");
    }


    public void start() {
        boolean onStartup = true;
        logger.info("MqttClient is starting!");

        loadConfigurationParameters();
        connectToBroker();

        while (true) {
            //Check if client is not setup
            while (!mqttClient.isConnected()) {
                logger.warn("MqttClient is not setup!");
                connectToBroker();
                mqttSleep(5000);
            }

            if (!isSubscribed) {
                subscribe();
            }

            if (isSubscribed && onStartup) {
                requestStatusFromAllDevices();
                onStartup = false;
            }
        }
    }

    private void requestStatusFromAllDevices() {
        Set<Device<String>> deviceSet = deviceManager.devices();
        for (Device<String> device : deviceSet) {
            String id = device.getId();
            publish(id, READ_COMMAND);
        }
    }

    public void mqttSleep(int mSec) {
        try {
            Thread.sleep(mSec);
        } catch (InterruptedException ex) {
            logger.error("MqttSleep interrup execption.");
            logger.error(ex.getStackTrace().toString());
        }
    }

    @Override
    public void connectToBroker() {
        logger.info("MqttClient is setting up...");

        try {
            MqttConnectOptions connectionOptions = new MqttConnectOptions();
            connectionOptions.setCleanSession(true);
            connectionOptions.setUserName(mqttBroker.mqttUsername);
            connectionOptions.setPassword(mqttBroker.mqttPassword.toCharArray());
            mqttClient.setCallback(this);
            mqttClient.connect(connectionOptions);

            if (!mqttClient.isConnected()) {
                logger.warn("mqttClient failed to connecte to broker.");
            }
        } catch (MqttException e) {
            printMqttException(e, "Mqtt Client Setup Exeception.");
        }
    }

    public MqttClient getMqttClient() {
        return mqttClient;
    }

}