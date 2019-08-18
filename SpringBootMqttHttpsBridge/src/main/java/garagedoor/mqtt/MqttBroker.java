package garagedoor.mqtt;

import garagedoor.Configurations.Config;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MqttBroker {
    public int qos;
    public String broker;
    public String clientId;
    public String mqttUsername;
    public String mqttPassword;
    public MemoryPersistence persistence;

    @Autowired
    public MqttBroker(Config config) {
        qos = 2;
        persistence = new MemoryPersistence();
        loadConfigurationParameters(config);
    }

    public void loadConfigurationParameters(Config config) {
        broker = config.mqttBrokerAddress;
        clientId = config.mqttClientId;
        mqttUsername = config.mqttBrokerUsername;
        mqttPassword = config.mqttBrokerPassword;
    }

}
