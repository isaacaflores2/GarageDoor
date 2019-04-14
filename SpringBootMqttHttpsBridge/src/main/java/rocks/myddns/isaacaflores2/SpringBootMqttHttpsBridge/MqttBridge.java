package rocks.myddns.isaacaflores2.SpringBootMqttHttpsBridge;

import jdk.nashorn.internal.runtime.regexp.joni.encoding.CharacterType;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author iflores
 */
public class MqttBridge extends Thread implements MqttCallback
{
    
    //MQTT Client constant
    private static final String READ = "read";
    private static final String SENSOR = "sensor";
    
    //MQTT Client members
    private MqttClient mqttClient; 
    private boolean isClientSetup = false; 
    private boolean connected = false; 
        
    //MQTT Broker
    private int qos=2;
    private String broker;
    private String clientId;
    private String mqttUsername;
    private String mqttPassword;
    private MemoryPersistence persistence;
    private boolean sensorUpdate = false; 
    
    //Device and client managers
    private String[] topics; 
    
    @Autowired
    private MqttDeviceManagerService mqttDeviceManager; 

    //Config member 
    @Autowired
    private ConfigService config; 
    
    private Logger logger; 
    private String lineSeperator; 
    
    public MqttBridge()
    {
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
        mqttDeviceManager = null;
        config = null;
        
        logger = LoggerFactory.getLogger(MqttBridge.class);
        lineSeperator = System.lineSeparator();
    }
       
    //Create instance from ConfigService class
    public MqttBridge(ConfigService config)
    {
        this.config = config;
        
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
        mqttDeviceManager = null;     
        
        logger = LoggerFactory.getLogger(MqttBridge.class);
        lineSeperator = System.lineSeparator();
        
    }
    
    public void loadConfig()
    {
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
                     
        mqttDeviceManager.loadDevices(config);
        
        try 
        {   
            //Create MqttClient Instance            
            mqttClient = new MqttClient(broker, generateClientId(), persistence);
        }         
        catch (MqttException e) 
        {
            printException(e, "Mqtt Client Setup Exeception! " );                               
        } 
    }
    
    public void mqttClientSetup() 
    {        
        logger.info("MqttClient is setting up...");
        
        try 
        {                        
            MqttConnectOptions connectionOptions = new MqttConnectOptions();
            connectionOptions.setCleanSession(true);
            connectionOptions.setUserName(mqttUsername);
            connectionOptions.setPassword(mqttPassword.toCharArray());
            mqttClient.setCallback(this);
            mqttClient.connect(connectionOptions);                 
            
            
            if(!mqttClient.isConnected())
            {                
                logger.warn("mqttClient failed to connecte to broker.");
            }
        }
        catch(MqttException e)
        {
            printException(e, "Mqtt Client Setup Exeception." );                                           
        }
    }
    
    public void subscribe()
    {       
        logger.info("MqttClient is subscribing...");
         try
         {
            //Check client connection with broker before subscribing 
            if(!mqttClient.isConnected())
                mqttClientSetup(); 
            
            if(!connected)
            {
                if(topics.length > 0 )
                {
                    for(int i = 0; i < topics.length ; i++ )
                    {
                        mqttClient.subscribe(topics[i]);                        
                    }
                }
                connected = true; 
            }                        
        }
        catch(MqttException e)
        {                
            printException(e,"Mqtt Client Subcscribe Exeception." );                
            
        }
    }
    
    public String publish(String topic, String content)
    {       
        logger.info("MqttClient is publishing...");
        if(!mqttClient.isConnected())
            mqttClientSetup();  
        
        try
        {
            MqttMessage mqttMsg = new MqttMessage(content.getBytes());
            mqttMsg.setQos(qos);
            mqttClient.publish(topic, mqttMsg);           
            logger.info("Mqtt published on: " + topic + " with the message: " + mqttMsg );            
            return mqttMsg.toString();            
        }
        catch(MqttException e)
        {
            printException(e, "Mqtt Client Public Exception." );        
            return e.toString();
        }
    }
         
    @Override
    public void connectionLost(Throwable thrwbl) 
    {
        logger.info("MqttClient connection lost...");        
        
        isClientSetup = false;
        connected  = false;
        mqttClientSetup(); 
        
        while(!mqttClient.isConnected()) 
        {
            logger.warn("Failed to reconnect...waiting 5 seconds to try again..."); 
            mqttSleep(5000);
            mqttClientSetup(); 
        }
        subscribe();
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception 
    {
        logger.info("Message arrived! Topic: "+ topic + " with message: "+ message.toString());
        
        //If message arrived is for a sensor and is not read request, then update device status
        if( topic.contains(SENSOR) && !message.toString().equals(READ) )
        {
            logger.info("Updating devices that subscribed to: " + topic);
            
            //Update devices that subscribe to the topic
            mqttDeviceManager.updateDeviceWithTopic(topic, message.toString());      
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) 
    {
        logger.debug("Message recieved by the broker!");         
    }
      
    @Override
    public void run() 
    {
        boolean onStartup = true;
       logger.info("MqttClient is starting!");
        
        //Load config parameters
        loadConfig();
        
        //Start mqtt client setup 
        mqttClientSetup();
        
        while(true){                       
            //Check if client is not setup
            while(!mqttClient.isConnected())
            {
                logger.warn("MqttClient is not setup!");
                mqttClientSetup(); 
                mqttSleep(5000);
            }
            
            if(!connected)
            {
                subscribe();                                
            }
            
            if(connected && onStartup)
            {
                //Request status from all sensors
                for(int i = 0; i < mqttDeviceManager.numDevices(); i++)
                {
                    publish(mqttDeviceManager.topic(i), READ);
                }
                onStartup = false; 
            }
            
        }               
    }
       
    private void printException(MqttException e, String msg)
    {        
        logger.error("Mqtt Exception: " + msg + "MqttException Details: " 
                +  e.getMessage() +  " Reason: "+ e.getReasonCode() + " loc " + e.getLocalizedMessage()
                + " cause "+ e.getCause());
                                      
        e.printStackTrace();
    }
    
    public void mqttSleep(int mSec)
    {
        try
            {
                Thread.sleep(mSec);                                     
            }
            catch (InterruptedException ex) 
            {
                //Logger.getLogger(HTTPSServer.class.getName()).log(Level.SEVERE, null, ex);
                logger.error("MqttSleep interrup execption.");
                logger.error(ex.getStackTrace().toString());                                
            }
    }
        
    public String getDeviceData(String id)
    {        
        String data = (String) mqttDeviceManager.getDeviceData(id);       
        return data; 
    }
    
    public String getDeviceStatus(String id)
    {        
        String status = mqttDeviceManager.getDeviceStatus(id);       
        return status; 
    }
    
    public String getDeviceTopic(String id)
    {        
        String topic = mqttDeviceManager.getDevice(id).topic();       
        return topic; 
    }
    
    
    public int getNumDevices()
    {
        return mqttDeviceManager.numDevices();
    }
    
    public MqttClient getMqttClient()
    {
        return mqttClient;
    }
    
    public boolean isConnected()
    {
        return connected;
    }
    
    public void setClientId(String clientId)
    {
        this.clientId = clientId;
    }
    
    public void setUsername(String username)
    {
        this.mqttUsername = username;
    }
    
    public void setPassword(String password)
    {
        this.mqttPassword  = password;
    }
    
    public void setBroker(String broker)
    {
        this.broker = broker; 
    }
    
    //Generates unique client id to prevent client already connected exceptin with Broker
    public String generateClientId() 
    {
        return clientId + System.nanoTime();
    }
    
    public void test()
    {
        logger.debug("Your subscribed topics are: ");
        for(int i = 0; i < topics.length; i++)
        {
            System.out.println( topics[i]);
            
        }                              
    }
}