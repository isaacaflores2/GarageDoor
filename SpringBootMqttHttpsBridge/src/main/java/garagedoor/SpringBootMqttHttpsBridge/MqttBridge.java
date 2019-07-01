package garagedoor.SpringBootMqttHttpsBridge;

import garagedoor.config.Config;
import garagedoor.iot.device.Device;
import garagedoor.iot.device.DeviceManager;
import garagedoor.iot.device.DeviceMap;
import garagedoor.mqtt.MqttClientSetup;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.List;

@Component
public class MqttBridge extends AbstractBridge<String> implements MqttCallback, Bridge, MqttClientSetup
{
    
    //MQTT Client constant
    public static final String READ = "read";
    public static final String SENSOR = "sensor";

    private DeviceMap<String, String> deviceMap;

    public MqttBridge() {
        super();
        this.deviceMap = null;
    }

    @Autowired
    public MqttBridge(Config config, DeviceManager deviceManager, DeviceMap deviceMap) {
        super(config, deviceManager);
        this.deviceMap = deviceMap;
    }


    public void subscribe() {
        logger.info("MqttClient is subscribing...");
         try {

            //Check client connection with broker before subscribing 
            if(!mqttClient.isConnected())
                setup();
            
            if(!connected) {
                if(topics.length > 0 ) {
                    for(int i = 0; i < topics.length ; i++ ) {
                        mqttClient.subscribe(topics[i]);                        
                    }
                }
                connected = true; 
            }                        
        }
        catch(MqttException e) {
            printMqttException(e,"Mqtt Client Subcscribe Exeception." );
        }
    }


    public String publish(String deviceId, String content) {
        logger.info("MqttClient is publishing...");
        if(!mqttClient.isConnected())
            setup();

        try {
            MqttMessage mqttMsg = new MqttMessage(content.getBytes());
            mqttMsg.setQos(qos);
            String topic = deviceMap.getDeviceMapValue(deviceId);
            mqttClient.publish(topic, mqttMsg);
            logger.info("Mqtt published on: " + deviceId + " with the message: " + mqttMsg );
            return mqttMsg.toString();
        }
        catch(MqttException e){
            printMqttException(e, "Mqtt Client Public Exception." );
            return e.toString();
        }
    }
         
    @Override
    public void connectionLost(Throwable thrwbl) {
        logger.info("MqttClient connection lost...");

        connected  = false;
        setup();
        
        while(!mqttClient.isConnected()) {
            logger.warn("Failed to reconnect...waiting 5 seconds to try again..."); 
            mqttSleep(5000);
            setup();
        }
        subscribe();
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception 
    {
        logger.info("Message arrived! Topic: "+ topic + " with message: "+ message.toString());
        
        //If message arrived is for a sensor and is not read request, then update device getStatus
        if( topic.contains(SENSOR) && !message.toString().equals(READ) )
        {
            logger.info("Updating devices that subscribed to: " + topic);
            
            //Update devices that subscribe to the topic
            String id = deviceMap.getDeviceMapInverseValue(topic);



            deviceManager.updateDeviceData(id, message.toString());
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) 
    {
        logger.debug("Message recieved by the broker!");         
    }
      

    public void run() 
    {
        boolean onStartup = true;
       logger.info("MqttClient is starting!");
        
        //Load config parameters
        loadConfig();
        
        //Start mqtt client setup 
        setup();
        
        while(true){                       
            //Check if client is not setup
            while(!mqttClient.isConnected())
            {
                logger.warn("MqttClient is not setup!");
                setup();
                mqttSleep(5000);
            }
            
            if(!connected)
            {
                subscribe();                                
            }
            
            if(connected && onStartup)
            {
                //Request getStatus from all sensors
                List<Device<String>> deviceList = deviceManager.devices();
                for(Device<String> device: deviceList)
                {
                    String id = device.getId();
                    publish(id, READ);
                }
                onStartup = false; 
            }
            
        }               
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

    public String getDeviceStatus(String id)
    {        
        String status = deviceManager.getDeviceStatus(id);
        return status; 
    }

    @Override
    public void setup(){
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
            printMqttException(e, "Mqtt Client Setup Exeception." );
        }
    }


    public int getNumDevices()
    {
        return deviceManager.numDevices();
    }
    
    public MqttClient getMqttClient()
    {
        return mqttClient;
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