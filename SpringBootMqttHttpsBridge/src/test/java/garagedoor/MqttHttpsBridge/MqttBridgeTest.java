/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package garagedoor.MqttHttpsBridge;

import garagedoor.Configurations.Config;
import garagedoor.iot.device.Device;
import garagedoor.iot.device.DeviceManager;
import garagedoor.iot.device.MqttDeviceManager;
import garagedoor.mqtt.MqttBroker;
import garagedoor.mqtt.MqttDevice;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


/**
 *
 * @author iflores
 */
public class MqttBridgeTest
{

    private Config config;
    
    @InjectMocks
    private  MqttBridge mqttBridge;

    @Mock
    MqttClient mqttClient;

    private DeviceManager<String> deviceManager;
    private MqttBroker mqttBroker;

    
    public Device expMqttDevices[];
    public String topic1  = "garage/door/toggle";
    public String topic2  = "garage/sensor/door";
    public String id1 = "1";
    public String id2 = "2";
    public String status = "default";
    public String data = "open";
    public int numDevices = 2;

    @BeforeEach
    public void setUp() 
    {

        expMqttDevices = new MqttDevice[2];
        expMqttDevices[0] = new MqttDevice(topic1 , id1 ,status, data);
        expMqttDevices[1] = new MqttDevice(topic2 , id2 ,status, data);
        config = new Config();
        config.mqttTopics = new String[]{topic1, topic2};
        config.deviceIds = new String[]{id1, id2};
        config.numDevices = "2";
        config.mqttBrokerAddress = "ssl://localhost";
        config.mqttClientId = "bridgeTest";
        config.mqttBrokerUsername = "iflores";
        config.mqttBrokerPassword = "smarthomeoptimis13!";


        deviceManager = new MqttDeviceManager<>(config);
        mqttBroker = new MqttBroker(config);
        mqttBridge = new MqttBridge(config, deviceManager, mqttBroker);
        mqttBridge.loadConfigurationParameters();

        MockitoAnnotations.initMocks(this);
    }      
    
    @Test
    public void testMqttClientSetup()
    {
        System.out.println("setup Test:");
        when(mqttClient.isConnected()).thenReturn(true);
        Boolean response =  mqttBridge.getMqttClient().isConnected();
        verify(mqttClient).isConnected();
        assertEquals(true, response );
    }
    
    @Test
    public void testPublish() throws MqttException {
        System.out.println("publish Test:");
        String content = "test test";
        MqttMessage mqttMsg = new MqttMessage(content.getBytes());
        mqttMsg.setQos(2);

        doAnswer(invocationOnMock -> {
            assertTrue(topic2.equals(invocationOnMock.getArgument(0)));
            assertTrue(mqttMsg.equals(invocationOnMock.getArgument(1)));
            return null;
        }).when(mqttClient).publish(topic2, mqttMsg);

        when(mqttClient.isConnected()).thenReturn(true);
        String response = mqttBridge.publish(id2, content);
        //verify(mqttClient).publish(topic2, mqttMsg);
        assertEquals(content, response);        
    }
    
    @Test
    public void testSubscribe() throws MqttException {
        System.out.println("subscribe Test:");

        doAnswer(invocationOnMock -> {
            assertTrue(topic1.equals(invocationOnMock.getArgument(0)));
            return null;
        }).when(mqttClient).subscribe(topic1);

        when(mqttClient.isConnected()).thenReturn(true);
        mqttBridge.subscribe();
        assertEquals(true, mqttBridge.isSubscribed());
        assertEquals(true, mqttBridge.isSubscribed);
    }
    
    @Test
    public void testConnectionLost() 
    {
        System.out.println("connectionLost Test:");
        Throwable thrwbl = new Throwable();

        when(mqttClient.isConnected()).thenReturn(true);
        mqttBridge.connectionLost(thrwbl);

        assertTrue(mqttBridge.isSubscribed());
    }

    @Test
    public void testMessageArrived() throws Exception 
    {
        System.out.println("messageArrived test");
        String payload = "test test";
        MqttMessage message = new MqttMessage(payload.getBytes());

        /*doAnswer(invocationOnMock -> {
            assertTrue(topic1.equals(invocationOnMock.getArgument(0)));
            assertTrue(message.equals(invocationOnMock.getArgument(1)));
            return null;
        }).when(mqttBridge).messageArrived(topic1, message);
        */

        mqttBridge.messageArrived(topic2, message);
        assertEquals(payload, deviceManager.getDeviceData(id2));
        
    }

    @Test
    public void testIsConnected() 
    {
        System.out.println("test isConnected: ");
        assertEquals(false, mqttBridge.isSubscribed());
        mqttBridge.isSubscribed = true;
        assertTrue(mqttBridge.isSubscribed());
    }
}
