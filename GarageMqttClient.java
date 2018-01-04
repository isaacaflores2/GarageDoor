/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package garagedooropener;
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
    private String topic        = "garage/toggle";
    private String content      = "Message from MqttPublishSample";
    private int qos             = 2;
    private String broker       = "tcp://localhost:1883";
    private String clientId     = "httpsBridge";
    private String mqttUsername = "iflores";
    private String mqttPassword = "smarthomeoptimis13!";
    private MemoryPersistence persistence = new MemoryPersistence();

    
     
    public void mqttClientSetup() {
        
        try {
            
            MqttClient mqttClient = new MqttClient(broker,clientId, persistence);
            MqttConnectOptions connectionOptions = new MqttConnectOptions();
            connectionOptions.setCleanSession(true);
            connectionOptions.setUserName(mqttUsername);
            connectionOptions.setPassword(mqttPassword.toCharArray());
            mqttClient.setCallback(this);
            mqttClient.connect(connectionOptions);
            System.out.println("Client is connected to broker:" +broker );
            mqttClient.subscribe(topic);
            System.out.println("Client is subscribed to " + topic );
        }
        catch(MqttException e){
            System.out.println("reason "+e.getReasonCode());
            System.out.println("msg "+e.getMessage());
            System.out.println("loc "+e.getLocalizedMessage());
            System.out.println("cause "+e.getCause());
            System.out.println("excep "+e);
            e.printStackTrace();
        }
    }
    
    //Mqtt Client Callbacks
    @Override
    public void connectionLost(Throwable thrwbl) {
         System.out.println("Connection lost...reconnecting to broker now");
         mqttClientSetup(); 
    }

    @Override
    public void messageArrived(String string, MqttMessage mm) throws Exception {
         System.out.println("Topic: "+ string + " with message: "+ mm.toString());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) {
         System.out.println("Message recieved by the broker!");
         
    }
    
     
    @Override
    public void run() {
        System.out.println("MqttClient is starting!");
        mqttClientSetup();
    }
    
}
