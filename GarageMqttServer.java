/*
 * @author iflores
 * File Description: The GarageMqttServer acts as Mqtt proxy between mqtt nodes and the HTTPS server. 
 */
package bb_garagedooropener;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


public class GarageMqttServer extends Thread implements MqttCallback{
    
    //MQTT Client constant
    private static final String READ = "read";
    private static final String SENSOR = "sensor";
    
    //MQTT Client members
    private MqttClient mqttClient; 
    private boolean isClientSetup = false; 
    private boolean connected = false; 
    private String topic;
    private String[] topics; 
    private MqttDevice[] devices;
    private int qos             = 2;
    private String broker;
    private String clientId;
    private String mqttUsername;
    private String mqttPassword;
    private MemoryPersistence persistence = new MemoryPersistence();
    private boolean sensorUpdate = false; 

    
    public GarageMqttServer(){
        mqttClient = null;
        isClientSetup = false; 
        connected = false;                 
    }
    
    public GarageMqttServer(String topic, String clientId, String username, String password, String broker){
        mqttClient = null;
        isClientSetup = false; 
        connected = false; 
        
        this.topic = topic;
        this.clientId = clientId;
        this.mqttUsername = username;
        this.mqttPassword = password;        
        this.broker = broker;
        
    }
    
    //Create instance from Config class
    public GarageMqttServer(Config config){
        
        if(config == null)
        {
            System.out.println("Config is null. Check your command line arguments or config file" );
        }
        
        mqttClient = null;
        isClientSetup = false; 
        connected = false; 
                
        this.topic = config.mqttTopic;
        this.topics = config.mqttTopic.split(",");        
        this.devices = new MqttDevice[topics.length];
        this.clientId = config.mqttClientId;
        this.mqttUsername = config.mqttUsername;
        this.mqttPassword = config.mqttPassword;        
        this.broker = config.mqttBrokerIPAddress;
        
        //Create list of MqttDevices
        for(int i = 0; i < topics.length; i++)
            devices[i] = new MqttDevice(topics[i], getIdFromTopic(topics[i]) ,null, null);
    }
         
    private void mqttClientSetup() 
    {        
        try 
        {            
            //Create new mqtt client instance 
            mqttClient = new MqttClient(broker, generateClientId(), persistence);
            MqttConnectOptions connectionOptions = new MqttConnectOptions();
            connectionOptions.setCleanSession(true);
            connectionOptions.setUserName(mqttUsername);
            connectionOptions.setPassword(mqttPassword.toCharArray());
            mqttClient.setCallback(this);
            mqttClient.connect(connectionOptions);
            
            if(Main.debugFlag)
                System.out.println("Client is connected to broker: " +broker );
            
            isClientSetup = true; 
                        
        }
        catch(MqttException e)
        {
            if(Main.debugFlag) 
            {
                printException(e, "Mqtt Client Setup Exeception! " );                               
            }
        }
    }
    
    private void subscribe()
    {
         try
         {
            if(!isClientSetup)
                mqttClientSetup(); 
            
            if(!connected)
            {
                if(topics.length > 1 )
                {
                    for(int i = 0; i < topics.length ; i++ )
                    {
                        mqttClient.subscribe(topics[i]);
                        if(Main.debugFlag)
                            System.out.println("Client is subscribed to " + topics[i] );
                    }
                }
                else
                    mqttClient.subscribe(topic);
                
                connected = true; 
            }                        
        }
        catch(MqttException e)
        {                
            if(Main.debugFlag)
            {
                printException(e,"Mqtt Client Subcscribe Exeception! " );                
            }
        }
    }
    
    public void publish(String topic, String content)
    {
       
        if(!isClientSetup)
            mqttClientSetup(); 
        
        try
        {
            MqttMessage mqttMsg = new MqttMessage(content.getBytes());
            mqttMsg.setQos(qos);
            mqttClient.publish(topic, mqttMsg);
            
            if(Main.debugFlag)
                System.out.println("Mqtt published on:" + topic + "with the message: " + mqttMsg );
        }
        catch(MqttException e)
        {
            if(Main.debugFlag)
            {
                printException(e,"Mqtt Client Publish Exeception! " );
            }
        }
    }
       
    //Mqtt Client Callbacks
    @Override
    public void connectionLost(Throwable thrwbl) 
    {
        if(Main.debugFlag)
            System.out.println("Connection lost...");        
        
        isClientSetup = false;
        connected  = false;
        mqttClientSetup(); 
        
        while(!isClientSetup) 
        {
            if(Main.debugFlag)
                System.out.println("Failed to reconnect...waiting 5 seconds to try again..."); 
            
            mqttSleep(5000);
            mqttClientSetup(); 
        }
        
        System.out.println("Client connected...subscribing to all topics now"); 
        subscribe();
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception 
    {
        if(Main.debugFlag)
            System.out.println("Message arrived! Topic: "+ topic + " with message: "+ message.toString());
        
        if( topic.contains(SENSOR) && !message.toString().equals(READ) )
        {
            if(Main.debugFlag)
                System.out.println("Updating sensor status!");
            
            //Update sensor state
            for(int i = 0; i < devices.length; i++)
            {
                if(topic.equals(devices[i].topic()))
                    devices[i].updateStatus(message.toString());
            }
                        
            //Set sensor update flag to true
            devices[1].setUpdateFlag(true); 
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) 
    {
        if(Main.debugFlag) 
            System.out.println("Message recieved by the broker!");         
    }
      
    @Override
    public void run() 
    {
        boolean onStartup = true;
        if(Main.debugFlag)
            System.out.println("MqttClient is starting!");
        
        //Start mqtt client setup 
        mqttClientSetup();
        
        while(true){                       
            //Check if client is not setup
            while(!isClientSetup)
            {
                if(Main.debugFlag)
                    System.out.println("MqttClient is not setup!");
                mqttClientSetup(); 
                mqttSleep(5000);
            }
            
            if(!connected)
            {
                subscribe();                                        
                test();
            }
            
            if(connected && onStartup)
            {
                //Request status from all sensors
                for(int i = 0; i < devices.length; i++)
                {
                    publish(devices[i].topic(), READ);
                }
                onStartup = false; 
            }
            
        }               
    }
    
    private String getIdFromTopic(String topic)
    {
        String[] topics_split = topic.split("/"); 
        return topics_split[topics_split.length-1]; 
    }
   
    private void printException(MqttException e, String msg)
    {        
        System.out.println(msg);
        System.out.println("reason "+e.getReasonCode());
        System.out.println("msg "+e.getMessage());
        System.out.println("loc "+e.getLocalizedMessage());
        System.out.println("cause "+e.getCause());
        System.out.println("excep "+e);
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
                if(Main.debugFlag){
                    Logger.getLogger(HTTPSServer.class.getName()).log(Level.SEVERE, null, ex);
                    ex.printStackTrace();
                }
            }
    }
    
    public String getSensorStatus(String topic, String id)
    {
        String status; 
        MqttDevice device = null;
        
        for(int i = 0; i < devices.length; i++)
        {
            if( devices[i].id().equals(id))
                device = devices[i];
        }
        if(device == null)
            return "Device with id: " + id + " was not found.";
               
        if( device.status() == null)
            status = "device has no current status";
        else
        {    
            //Request status from device -- This call ass the device for a status and waits for a response
            status = devices[1].status();
            if(Main.debugFlag)
                System.out.println("New status: " + status);
        }
        
        return status; 
    }
    
    public boolean getClientStatus()
    {
        return isClientSetup && connected;
    }
    
    public void setTopic( String topic )
    {
        this.topic = topic; 
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
    
    public String generateClientId() 
    {
        // length of nanoTime = 15, so total length = 19 < 65535(defined in
        // spec)
        return clientId + System.nanoTime();
    }
    
    public void test()
    {
        System.out.println("Your subscribed topics are: ");
        for(int i = 0; i < topics.length; i++)
        {
            System.out.println( topics[i]);
            
        }
                      
        System.out.println("Below is the status of each device: ");
        for(int i = 0; i < devices.length; i++)
        {
            System.out.println( devices[i].topic() + "with name: " + devices[i].id() + " : " + devices[i].status() + " is updated: " + devices[i].isUpdated() );
        }

    }
    
}
