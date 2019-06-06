/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.myddns.isaacaflores2.SpringBootMqttHttpsBridge;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.After;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author iflores
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MqttBridgeTest 
{
    @Autowired
    private ConfigService config;
    
    @Autowired
    private  MqttBridge mqttBridgeClient; 
    
    public Device expDevices[];
    public String topic1  = "garage/door/toggle";
    public String topic2  = "garage/sensor/door";
    public String id = "2";

    @Before
    public void setUp() 
    {                       
        mqttBridgeClient.loadConfig();      
        if(!mqttBridgeClient.isConnected())
            mqttBridgeClient.mqttClientSetup(); 
        mqttBridgeClient.subscribe();        
        
        expDevices = new Device[2];
        expDevices[0] = new Device(topic1 , "1" ,null, null);
        expDevices[1] = new Device(topic2 , "2" ,null, null);
    }      
    
    @Test
    public void testMqttClientSetup()
    {
        System.out.println("mqttClientSetup Test:");
        //MqttBridgeClient mqttBridgeClient = new MqttBridge(config);
        mqttBridgeClient.loadConfig();
        mqttBridgeClient.mqttClientSetup();
        assertEquals(true, mqttBridgeClient.getMqttClient().isConnected());
    }
    
    @Test
    public void testPublish() 
    {       
        System.out.println("publish Test:");
        String content = "test test";
        String response = mqttBridgeClient.publish(topic2, content);
        assertEquals(content, response);        
    }
    
    @Test
    public void testSubscribe() 
    {       
        System.out.println("subscribe Test:");
        String content = "test test";
        assertEquals(true, mqttBridgeClient.isConnected());                
    }
    
    @Test
    public void testConnectionLost() 
    {
        System.out.println("connectionLost Test:");
        Throwable thrwbl = null;             
        mqttBridgeClient.connectionLost(thrwbl);        
        assertTrue(mqttBridgeClient.isConnected());     
    }

    @Test
    public void testMessageArrived() throws Exception 
    {
        System.out.println("messageArrived test");
        String payload = "test test";
        MqttMessage message = new MqttMessage(payload.getBytes());
        mqttBridgeClient.messageArrived(topic2, message);
        assertEquals(payload, mqttBridgeClient.getDeviceData(id));
        
    }

    @Test
    public void testGetDeviceStatus() 
    {
        System.out.println("test getSensorStatus");
        String content = "test test";
        String expResult = "default";
        String result = mqttBridgeClient.getDeviceStatus(id);
        assertEquals(expResult, result);        
    }

    @Test
    public void testGetDeviceData() throws Exception 
    {
        System.out.println(" test getSensorData");
        String content = "test test";             
        MqttMessage message = new MqttMessage(content.getBytes());
        mqttBridgeClient.messageArrived(topic2, message);

        String result = mqttBridgeClient.getDeviceData(id);
        assertEquals(content, result);        
    }
     
    @Test
    public void testGetDeviceTopic() throws Exception 
    {
        assertEquals(topic2, mqttBridgeClient.getDeviceTopic(id));
    }
    
    @Test
    public void testIsConnected() 
    {
        System.out.println("test isConnected: ");
        
        boolean expResult = false;
        boolean result = mqttBridgeClient.isConnected();        
        //assertEquals(expResult, result);            
        
        expResult = true;
        result = mqttBridgeClient.isConnected();
        assertEquals(expResult, result);
    }
}
