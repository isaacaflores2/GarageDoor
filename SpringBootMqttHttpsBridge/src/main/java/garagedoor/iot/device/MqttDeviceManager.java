/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package garagedoor.iot.device;

import java.util.ArrayList;
import java.util.List;
import garagedoor.config.Config;
import garagedoor.mqtt.MqttDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MqttDeviceManager<T> implements DeviceManager
{
    private Config config;
    private String[] deviceTopics;
    private String[] deviceIDs; 
    protected List<Device<T>> mqttDeviceList;
    private Logger logger; 
    
    public MqttDeviceManager() {
        this.config = null;
        this.deviceTopics = null;
        this.deviceIDs = null; 
        this.mqttDeviceList = new ArrayList<Device<T>>();
        this.logger = LoggerFactory.getLogger(MqttDeviceManager.class);
    
    }

    @Autowired
    public MqttDeviceManager(Config config) {
        this.config = config;
        this.deviceTopics = null;
        this.deviceIDs = null; 
        this.mqttDeviceList = new ArrayList<Device<T>>();
        this.logger = LoggerFactory.getLogger(MqttDeviceManager.class);
        loadDevices(this.config);
    }      
       

    @Override
    public int numDevices() {
        return mqttDeviceList.size();
    }
    
    @Override
    public List<Device<T>> devices() {
        return mqttDeviceList;
    
    }
    
    @Override
    public void addDevice(Device device) {
        logger.info("Adding a new mqttDevice...");

        if(device == null) {
            throw new RuntimeException("Provide device is null");
        }

        mqttDeviceList.add(device);
    }
    
    @Override
    public void removeDevice(Device device) {
        logger.info("Removing device...");
        
        if(device == null) {
            throw new RuntimeException("Provide mqttDevice is null");
        }

        mqttDeviceList.remove(device);
    }
    
    @Override
    public Device getDevice(String id) {
        logger.info("Getting device with getId: " + id);
        
        for(int i = 0; i < mqttDeviceList.size(); i++) {

            if(id.equals( mqttDeviceList.get(i).getId()) ) {
                return mqttDeviceList.get(i);
            }
        }

        return null;
    }
    
    @Override
    public boolean contains(String id) {
        logger.info("Contains mqttDevice with getId: " + id);
        Device device = getDevice(id);

        if( device == null)
            return  false; 
        else
            return true;
       
    }
    
    @Override
    public void loadDevices(Object o) {
        logger.info("Loading new device...");
        
        //Load device parameters from Config object
        if(o == null)
            throw new IllegalArgumentException("Object is null. Cannot load devices.");

        if( o instanceof Config)
            this.config = (Config) o;
        else
            throw new IllegalArgumentException("Object is not instance of type Config. Cannot load devices.");
        
        //Load parameters
        this.deviceTopics = config.mqttTopics;          
        this.deviceIDs = config.deviceIds;
                
        if(deviceTopics != null ) {

            if(deviceTopics.length > 0 ) {

                for(int i = 0; i < deviceTopics.length; i++) {
                    mqttDeviceList.add(new MqttDevice(deviceTopics[i], deviceIDs[i] , "default", null));
                }
            }                           
            else
                   logger.error("MqttDevice Topics Length is 0");
        }       
    }
    
    @Override
    public T getDeviceData(String id) {
        logger.info("Getting mqttDevice getData for: " + id);

        Device device = getDevice(id);
        
        if( device == null)
            return  null; 
        else
            return (T) device.getData();
    }
    
    @Override
    public String getDeviceStatus(String id) {
        logger.info("Getting mqttDevice getStatus for: " + id);

        Device device = getDevice(id);
        
        if( device == null)
            return  null; 
        else
            return device.getStatus();
    }
    
    @Override
    public void updateDeviceStatus(String id, String status)  {
        logger.info("Update mqttDevice getStatus for: " + id);

        Device device = getDevice(id);
        
        if( device != null)
            device.setStatus(status);
        else
            throw new IllegalArgumentException("Device with id: " + id + " does not exist.");
    }



    @Override
    public void updateDeviceData(String id, Object data) {
        logger.info("Update mqttDevice getData for: " + id);

        Device device = getDevice(id);
        
        if( device != null)
            device.setData((T) data);
        else
            throw new IllegalArgumentException("Device with id: " + id + " does not exist.");
    }

    public String topic(int i) {
        logger.info("Getting topic...");
        
        if(i <= deviceTopics.length-1)
            return deviceTopics[i];

        return null; 
    }
}
