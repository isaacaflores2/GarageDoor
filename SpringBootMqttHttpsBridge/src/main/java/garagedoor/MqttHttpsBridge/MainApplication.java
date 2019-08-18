package garagedoor.MqttHttpsBridge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("garagedoor")
@SpringBootApplication
public class MainApplication
{
        public static ApplicationContext applicationContext;                
        public static MqttBridge mqttBridge;
        public static Logger logger = LoggerFactory.getLogger(MainApplication.class);
        
        public static void main(String[] args) 
        {
            applicationContext = SpringApplication.run(MainApplication.class, args);
            mqttBridge = applicationContext.getBean(MqttBridge.class);
            logger.info("Running MqttBridgeClient....");
            mqttBridge.start();
	    }
}