package rocks.myddns.isaacaflores2.SpringBootMqttHttpsBridge;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ConfigService 
{
    @Value("${mqtthttpsbridge.mqtttopics:empty}")
    public String[] mqttTopics;
    
    @Value("${mqtthttpsbridge.deviceids:empty}")
    public String[] deviceIds;    
    
    @Value("${mqtthttpsbridge.numdevices:empty}")
    public String numDevices;
    
    //MQTT Broker Arguments
    @Value("${mqtthttpsbridge.mqttbrokeraddress:empty}")
    public  String mqttBrokerAddress; // "ssl://ipaddres:port or "ssl://url:port "
    
    @Value("${mqtthttpsbridge.mqttclientid:empty}")
    public String mqttClientId;
    
    @Value("${mqtthttpsbridge.mqttusername:empty}")
    public String mqttBrokerUsername;
    
    @Value("${mqtthttpsbridge.mqttpassword:empty}")
    public String mqttBrokerPassword;       
}
