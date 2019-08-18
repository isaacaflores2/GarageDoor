package garagedoor.mqtt;

public interface MqttClientSetup {

    public void loadConfigurationParameters();

    public void connectToBroker();

    public String generateClientId();

}
