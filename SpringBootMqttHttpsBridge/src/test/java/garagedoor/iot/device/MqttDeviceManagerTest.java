/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package garagedoor.iot.device;

import garagedoor.Configurations.Config;
import garagedoor.mqtt.MqttDevice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author iflores
 */
public class MqttDeviceManagerTest
{

    private Config config;
       
    public MqttDeviceManager mqttDeviceManager;
    
    public Device expectedMqttDevices[];
    public String topic1  = "garage/door/toggle";
    public String topic2  = "garage/sensor/door";
    public String id1 = "1";
    public String id2 = "2";
    public String status = "default";
    public String data = null;
    public int numDevices = 2;
    
    @BeforeEach
    public void setUp() 
    {         
        
        mqttDeviceManager = new MqttDeviceManager();
        expectedMqttDevices = new MqttDevice[2];
        expectedMqttDevices[0] = new MqttDevice(id1, topic1 ,status, data);
        expectedMqttDevices[1] = new MqttDevice(id2, topic2  ,status, data);
        config = new Config();
        config.mqttTopics = new String[]{topic1, topic2};
        config.deviceIds = new String[]{id1, id2};
        config.numDevices = "2";
        config.mqttBrokerAddress = "";
        config.mqttClientId = "";
        config.mqttBrokerUsername = "";
        config.mqttBrokerPassword = "";

    }

    @Test
    public void testNumDevices() 
    {
        assertEquals(0, mqttDeviceManager.numDevices());
        mqttDeviceManager.addDevice(expectedMqttDevices[0]);
        assertEquals(1, mqttDeviceManager.numDevices());
    }
    
    @Test
    public void testAddDevice() 
    {
        assertThrows(RuntimeException.class, () -> mqttDeviceManager.addDevice(null));

        mqttDeviceManager.addDevice(expectedMqttDevices[0]);
        assertEquals(1, mqttDeviceManager.numDevices());
        assertEquals(expectedMqttDevices[0], mqttDeviceManager.getDevice(id1));
    }
    
    @Test
    public void testRemoveDevice() 
    {
        assertThrows(RuntimeException.class, () -> mqttDeviceManager.removeDevice(null));

        mqttDeviceManager.addDevice(expectedMqttDevices[0]);
        mqttDeviceManager.addDevice(expectedMqttDevices[1]);
        mqttDeviceManager.removeDevice(expectedMqttDevices[1]);
        assertEquals(1, mqttDeviceManager.numDevices());
    }
    
    @Test
    public void testGetDevice() 
    {
        assertNull(mqttDeviceManager.getDevice(id1));
        mqttDeviceManager.addDevice(expectedMqttDevices[0]);
        assertEquals(expectedMqttDevices[0], mqttDeviceManager.getDevice(id1));
    }
        
    @Test
    public void testContains() 
    {
        mqttDeviceManager.addDevice(expectedMqttDevices[0]);
        assertTrue(mqttDeviceManager.contains(id1));
        mqttDeviceManager.removeDevice(expectedMqttDevices[0]);
        assertFalse(mqttDeviceManager.contains(id1));
        
    }
    @Test
    public void testLoadDevice() 
    {
        assertThrows(IllegalArgumentException.class, () -> {mqttDeviceManager.loadDevices(null);});

        //Test object that is not of type Config
        assertThrows(IllegalArgumentException.class, () -> {mqttDeviceManager.loadDevices(data);});

        mqttDeviceManager.loadDevices(config);

        assertEquals(numDevices, mqttDeviceManager.numDevices());
        assertEquals(expectedMqttDevices[0].toString(), mqttDeviceManager.getDevice(id1).toString());
        assertEquals(expectedMqttDevices[1].toString(), mqttDeviceManager.getDevice(id2).toString());

    }
    
    @Test
    public void testGetDeviceStatus() 
    {
        mqttDeviceManager.addDevice(expectedMqttDevices[0]);
        assertEquals(status, mqttDeviceManager.getDeviceStatus(id1));
    }
    
    @Test
    public void testGetDeviceData()
    {
        mqttDeviceManager.addDevice(expectedMqttDevices[0]);
        assertEquals(data, mqttDeviceManager.getDeviceData(id1));
    }
    
    @Test
    public void testUpdateDeviceStatus() 
    {
        assertThrows(IllegalArgumentException.class, () -> {mqttDeviceManager.updateDeviceStatus(null, status);});

        mqttDeviceManager.addDevice(expectedMqttDevices[0]);
        mqttDeviceManager.updateDeviceStatus(id1, "updated getStatus");
        assertEquals("updated getStatus", mqttDeviceManager.getDeviceStatus(id1));
    }
    
    @Test
    public void testUpdateDeviceData()
    {
        assertThrows(IllegalArgumentException.class, () -> {mqttDeviceManager.updateDeviceData(null, data);});

        mqttDeviceManager.addDevice(expectedMqttDevices[0]);
        mqttDeviceManager.updateDeviceData(id1, "updated getData");
        assertEquals("updated getData", mqttDeviceManager.getDeviceData(id1));
    }
}
