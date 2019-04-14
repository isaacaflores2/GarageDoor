package rocks.myddns.isaacaflores2.SpringBootMqttHttpsBridge;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Isaac Flores
 */

@Configuration
public class AppConfig 
{
    @Bean
    public Device device()
    {
        return new Device(); 
    }
    
    @Bean 
    public MqttBridge mqttBridgeClient()
    {
        return new MqttBridge();
    }          
}
