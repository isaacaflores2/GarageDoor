/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

/**
 *
 * @author iflores
 */
public class GarageMqttClient extends Thread implements MqttCallback{
    
    
    //MQTT Client 
    private MqttClient mqttClient; 
    private boolean isClientSetup = false; 
    private boolean connected = false; 
    private String topic        = "garage/toggle";
    private String content      = "Message from MqttPublishSample";
    private int qos             = 2;
    private String broker       = "ssl://isaacaflores2.myddns.rocks:8883";
    private String clientId     = "httpsBridge";
    private String mqttUsername = "iflores";
    private String mqttPassword = "smarthomeoptimis13!";
    private MemoryPersistence persistence = new MemoryPersistence();

    
    public GarageMqttClient(){
        mqttClient = null;
        isClientSetup = false; 
        
    }
    
    public GarageMqttClient(String topic, String clientId, String username, String password, String broker){
        this.topic = topic;
        this.clientId = clientId;
        this.mqttUsername = username;
        this.mqttPassword = password;        
        this.broker = broker;
    }
    
    //Create instance from Config class
    public GarageMqttClient(Config config){
        
        if(config == null)
        {
            System.out.println("Config is null. Check your command line arguments or config file" );
        }
        this.topic = config.mqttTopic;
        this.clientId = config.mqttClientId;
        this.mqttUsername = config.mqttUsername;
        this.mqttPassword = config.mqttPassword;        
        this.broker = config.mqttBrokerIPAddress;
    }
    
     
    public void mqttClientSetup() {
        
        try {
            
            mqttClient = new MqttClient(broker,generateClientId(), persistence);
            MqttConnectOptions connectionOptions = new MqttConnectOptions();
            connectionOptions.setCleanSession(true);
            connectionOptions.setUserName(mqttUsername);
            connectionOptions.setPassword(mqttPassword.toCharArray());
            mqttClient.setCallback(this);
            mqttClient.connect(connectionOptions);
            if(Main.debugFlag)
                System.out.println("Client is connected to broker:" +broker );
            
            isClientSetup = true; 
            if(Main.debugFlag)
                System.out.println("Client is subscribed to " + topic );
        }
        catch(MqttException e){
            if(Main.debugFlag) {
            System.out.println("Mqtt Client Setup Exeception! " );
            System.out.println("reason: "+e.getReasonCode());
            System.out.println("msg: "+e.getMessage());
            System.out.println("loc: "+e.getLocalizedMessage());
            System.out.println("cause: "+e.getCause());
            System.out.println("excep: "+e);
            e.printStackTrace();                        
            }
        }
    }
    
    public void subscribe(){
         try{
                if(!isClientSetup)
                    mqttClientSetup(); 
                if(!connected){
                    mqttClient.subscribe(topic);
                    connected = true; 
                }
            }
            catch(MqttException e){
                if(Main.debugFlag){
                System.out.println("Mqtt Client Subcscribe Exeception! " );
                System.out.println("reason "+e.getReasonCode());
                System.out.println("msg "+e.getMessage());
                System.out.println("loc "+e.getLocalizedMessage());
                System.out.println("cause "+e.getCause());
                System.out.println("excep "+e);
                e.printStackTrace();
                }
            }
    }
    
    public void publish(String topic, String content){
       
        if(!isClientSetup)
            mqttClientSetup(); 
        
        try{
            MqttMessage mqttMsg = new MqttMessage(content.getBytes());
            mqttMsg.setQos(qos);
            mqttClient.publish(topic, mqttMsg);
            if(Main.debugFlag)
                System.out.println("Mqtt published: " + mqttMsg );
        }
        catch(MqttException e){
            if(Main.debugFlag){
            System.out.println("Mqtt Client Publish Exeception! " );
            System.out.println("reason "+e.getReasonCode());
            System.out.println("msg "+e.getMessage());
            System.out.println("loc "+e.getLocalizedMessage());
            System.out.println("cause "+e.getCause());
            System.out.println("excep "+e);
            e.printStackTrace();
            }
        }
        
        
    }
    
    
    //Mqtt Client Callbacks
    @Override
    public void connectionLost(Throwable thrwbl) {
        if(Main.debugFlag)
            System.out.println("Connection lost...reconnecting to broker now");        
        isClientSetup = false;
        connected  = false;
        mqttClientSetup(); 
        subscribe();
    }

    @Override
    public void messageArrived(String string, MqttMessage mm) throws Exception {
        if(Main.debugFlag)
            System.out.println("Topic: "+ string + " with message: "+ mm.toString());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) {
        if(Main.debugFlag) 
            System.out.println("Message recieved by the broker!");
         
    }
    
    
    
    
    @Override
    public void run() {
        if(Main.debugFlag)
            System.out.println("MqttClient is starting!");
        mqttClientSetup();
        while(true){
           if(!isClientSetup)
               mqttClientSetup();
           
           if(!connected){
            subscribe();
            try {                                    
                Thread.sleep(5000);                     
                } 
            catch (InterruptedException ex) {
                if(Main.debugFlag){
                Logger.getLogger(HTTPSServer.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
                }
                }           
           }
        }
        
    }
    
    
    public void setTopic( String topic ){
        this.topic = topic; 
    }
    
    public void setClientId(String clientId){
        this.clientId = clientId;
    }
    
    public void setUsername(String username){
        this.mqttUsername = username;
    }
    
    public void setPassword(String password){
        this.mqttPassword  = password;
    }
    
    public void setBroker(String broker){
        this.broker = broker; 
    }
    
    public String generateClientId() {
		// length of nanoTime = 15, so total length = 19 < 65535(defined in
		// spec)
		return clientId + System.nanoTime();
	}
}
