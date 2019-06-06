/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.myddns.isaacaflores2.SpringBootMqttHttpsBridge;

import java.util.ArrayList;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
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
public class MqttDeviceManagerServiceTest 
{
    @Autowired
    private ConfigService config; 
       
    public MqttDeviceManagerService instance;
    
    public Device expDevices[];
    public String topic1  = "garage/door/toggle";
    public String topic2  = "garage/sensor/door";
    public String id = "2";
    
    @Before
    public void setUp() 
    {         
        
        instance = new MqttDeviceManagerService(); 
        expDevices = new Device[2];
        expDevices[0] = new Device(topic1 , "1" ,null, null);
        expDevices[1] = new Device(topic2 , "2" ,null, null);
    }
    
          
    @Test
    public void testUpdateDeviceWithTopic()
    {
        Device device = new Device("test", "0", "default", null);                 
        Device device2 = new Device("test", "1", "default", null);                 
        instance.addDevice(device);
        instance.addDevice(device2);
        instance.updateDeviceWithTopic("test", "updated data");     
        assertEquals("updated data", instance.getDeviceData("0")); 
        assertEquals("updated data", instance.getDeviceData("1")); 
    }
    
    @Test
    public void testNumDevices() 
    {               
        Device device = new Device("test", "0", "default", null);                 
        instance.addDevice(device);
        assertEquals(1, instance.numDevices());  
        Assert.assertNotEquals(2, instance.numDevices());       
    }
    
    @Test
    public void testAddDevice() 
    {               
        Device device = new Device("test", "0", "default", null);                         
        //instance.addDevice(null);        
        //assertEquals(0, instance.numDevices());
        instance.addDevice(device);        
        assertEquals(1, instance.numDevices());
        assertEquals(device, instance.getDevice("0"));               
    }
    
    @Test
    public void testRemoveDevice() 
    {               
        Device device = new Device("test", "0", "default", null);                 
        Device device2 = new Device("test2", "1", "default", null);                 
        instance.addDevice(device);
        instance.addDevice(device2);
        //instance.removeDevice(null);        
        //assertEquals(2, instance.numDevices());        
        instance.removeDevice(device2);        
        assertEquals(1, instance.numDevices());        
    }
    
    @Test
    public void testGetDevice() 
    {               
        Device device = new Device("test", "0", "default", null);                 
        instance.addDevice(device);        
        assertEquals(device, instance.getDevice("0"));               
        
    }
        
    @Test
    public void testContains() 
    {        
        Device device = new Device("test", "0", "default", null);                 
        instance.addDevice(device);
        assertEquals(true, instance.contains("0"));
        instance.removeDevice(device);
        assertEquals(false, instance.contains("0"));
        
    }
    @Test
    public void testLoadDevice() 
    {
        instance.loadDevices(config);
        ArrayList<Device> devices;
        devices = instance.devices();         
        assertEquals(expDevices.length, devices.size());        
        assertEquals(expDevices[0].topic(), devices.get(0).topic());
        assertEquals(expDevices[0].id(), devices.get(0).id());
        assertEquals(expDevices[1].topic(), devices.get(1).topic());
        assertEquals(expDevices[1].id(), devices.get(1).id());              
    }
    
    @Test
    public void testGetDeviceStatus() 
    {    
        Device device = new Device("test", "0", "default", null);                 
        instance.addDevice(device);
        assertEquals("default", instance.getDeviceStatus("0"));  
    }
    
    @Test
    public void testGetDeviceData()
    {         
        Device device = new Device("test", "0", "default", "default data");                 
        instance.addDevice(device);
        assertEquals("default data", instance.getDeviceData("0")); 
    }
    
    @Test
    public void testUpdateDeviceStatus() 
    {        
        Device device = new Device("test", "0", "default", null);                 
        instance.addDevice(device);
        instance.updateDeviceStatus("0", "updated status");
        assertEquals("updated status", instance.getDeviceStatus("0"));  
    }
    
    @Test
    public void testUpdateDeviceData()
    {
        Device device = new Device("test", "0", "default", null);                 
        instance.addDevice(device);
        instance.updateDeviceData("0", "updated data");
        assertEquals("updated data", instance.getDeviceData("0"));  
    }
}
