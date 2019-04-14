package rocks.myddns.isaacaflores2.SpringBootMqttHttpsBridge;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class MainApplication implements ApplicationRunner
{         
        public static ConfigService config;         
        public static ApplicationContext applicationContext;                
        public static MqttBridge mqttBridgeClient;
        public static boolean debugFlag = false; 
        public static Logger logger = LoggerFactory.getLogger(MainApplication.class);
        
        public static void main(String[] args) 
        {                             
            //Get bean instances from application context
            applicationContext = SpringApplication.run(MainApplication.class, args);
            mqttBridgeClient = applicationContext.getBean(MqttBridge.class);            
            config = applicationContext.getBean(ConfigService.class);                
            
            //Start Mqtt Bridge
            logger.info("Running MqttBridgeClient....");
            mqttBridgeClient.run();           
	}
        
        @Override
        public void run(ApplicationArguments arg0) throws Exception 
        {
                     
        }               
}