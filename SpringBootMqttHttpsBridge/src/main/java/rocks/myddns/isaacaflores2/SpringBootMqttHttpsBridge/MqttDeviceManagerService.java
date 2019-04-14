/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.myddns.isaacaflores2.SpringBootMqttHttpsBridge;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author iflores
 */

@Service
public class MqttDeviceManagerService implements DeviceManagerService 
{   
    private ConfigService config; 
    private String[] deviceTopics;
    private String[] deviceIDs; 
    protected ArrayList<Device> deviceList;
    private Logger logger; 
    
    public MqttDeviceManagerService() 
    {
        this.config = null;
        this.deviceTopics = null;
        this.deviceIDs = null; 
        this.deviceList = new ArrayList<Device>();
        this.logger = LoggerFactory.getLogger(MqttDeviceManagerService.class);
    
    }    
    
    public MqttDeviceManagerService(ConfigService config) 
    {
        this.config = config;
        this.deviceTopics = null;
        this.deviceIDs = null; 
        this.deviceList = new ArrayList<Device>();
        this.logger = LoggerFactory.getLogger(MqttDeviceManagerService.class);
    
    }      
       
    public void updateDeviceWithTopic(String topic, String data)
    {
         for(int i = 0; i < deviceList.size(); i++)
        {
            if(topic.equals( deviceList.get(i).topic()) )
            {
                deviceList.get(i).updateData(data);
            }
        }        
    }
    
    @Override
    public int numDevices()
    {
        return deviceList.size();
    }
    
    @Override
    public ArrayList<Device> devices()
    {
        return deviceList; 
    
    }
    
    @Override
    public void addDevice(Device device)
    {
        logger.info("Adding a new device...");
        if(device == null)
        {
            throw new RuntimeException("Provide device is null");
        }
        deviceList.add(device);
    }
    
    @Override
    public void removeDevice(Device device)
    {
        logger.info("Removing device...");
        
        if(device == null)
        {
            throw new RuntimeException("Provide device is null");
        }       
        deviceList.remove(device);  
    }
    
    @Override
    public Device getDevice(String id)
    {
        logger.info("Getting device with id: " + id);
        
        for(int i = 0; i < deviceList.size(); i++)
        {
            if(id.equals( deviceList.get(i).id()) )
            {
                return deviceList.get(i);
            }
        }
        return null;  
    }
    
    @Override
    public boolean contains(String id)
    {
        logger.info("Contains device with id: " + id);
        
        Device device = getDevice(id);
        
        if( device == null)
            return  false; 
        else
            return true;
       
    }
    
    @Override
    public void loadDevices(Object o) 
    {
        logger.info("Loading new device...");
        
        //Load device parameters from ConfigService object
        if(o == null)
            return;
                
        this.config = (ConfigService) o;
        
        //Load parameters
        this.deviceTopics = config.mqttTopics;          
        this.deviceIDs = config.deviceIds;
                
        if(deviceTopics != null )
        {
            if(deviceTopics.length > 0 )            
            {
                for(int i = 0; i < deviceTopics.length; i++) 
                {
                    deviceList.add(new Device(deviceTopics[i], deviceIDs[i] , "default", null));                    
                }
            }                           
            else
            {
                 if(MainApplication.debugFlag)
                    System.out.println("Device Topics Length is 0");
            }
        }       
    }
    
    @Override
    public Object getDeviceData(String id)
    {
        logger.info("Getting device data for: " + id);
        
        Device device = getDevice(id);
        
        if( device == null)
            return  null; 
        else
            return device.data();
    }
    
    @Override
    public String getDeviceStatus(String id) 
    {
        logger.info("Getting device status for: " + id);
        
        Device device = getDevice(id);
        
        if( device == null)
            return  null; 
        else
            return device.status();
    }
    
    @Override
    public boolean updateDeviceStatus(String id, String status)
    {    
        logger.info("Update device status for: " + id);
        
        Device device = getDevice(id);
        
        if( device == null)
            return  false; 
        else
            return device.updateStatus(status);        
    }   
    
    @Override
    public boolean updateDeviceData(String id, String data)
    {
        logger.info("Update device data for: " + id);
        
        Device device = getDevice(id);
        
        if( device == null)
            return  false; 
        else
            return device.updateData(data);  
    }
    
    public String topic(int i)
    {
        logger.info("Getting topic...");
        
        if(i <= deviceTopics.length-1)
            return deviceTopics[i];        
        return null; 
    }
}
