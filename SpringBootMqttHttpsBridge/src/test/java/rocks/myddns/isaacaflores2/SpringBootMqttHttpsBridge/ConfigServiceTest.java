/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.myddns.isaacaflores2.SpringBootMqttHttpsBridge;

import junit.framework.TestCase;
import static junit.framework.TestCase.assertEquals;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;


/**
 *
 * @author iflores
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfigServiceTest extends TestCase
{
   @Autowired
    ConfigService config; 
    
    @Test
     public void testMqttTopics()
    {
        String[] expResult = new String[2];
        expResult[0] = "sensor/one";
        expResult[1] = "topic/two";
        //assertEquals(expResult[0], config.mqttTopics()[0]);
        //assertEquals(expResult[1], config.mqttTopics()[2]);
        Assert.assertArrayEquals(expResult, config.mqttTopics);
    }
     
    @Test
    public void testDeviceIds()
    {
        String[] expResult = new String[2];
        expResult[0] = "1";
        expResult[1] = "2";
        Assert.assertArrayEquals(expResult, config.deviceIds);
    }
    
    @Test
    public void testNumDevices()
    {
        assertEquals("2", config.numDevices);
    }
    
    @Test
    public void testMqttBrokerAddress()
    {
        assertEquals("tcp://localhost:1883", config.mqttBrokerAddress);
    }
    
    @Test
    public void testMqttClientId()
    {
        assertEquals("garageDoor", config.mqttClientId);
    }
    
    @Test
    public void testMqttBrokerUsername()
    {
        assertEquals("iflores", config.mqttBrokerUsername);
    }
    
    @Test
    public void testMqttBrokerPassword()
    {
        assertEquals("smarthomeoptimis13!", config.mqttBrokerPassword);
    }
}
