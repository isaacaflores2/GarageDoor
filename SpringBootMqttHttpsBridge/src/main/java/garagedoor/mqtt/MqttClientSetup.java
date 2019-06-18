package garagedoor.mqtt;

public interface MqttClientSetup {


    public void loadConfig();
    public void setup();
    public String generateClientId();

}
