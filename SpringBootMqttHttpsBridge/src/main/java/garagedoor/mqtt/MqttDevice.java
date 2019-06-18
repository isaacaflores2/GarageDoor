/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package garagedoor.mqtt;
import garagedoor.iot.device.AbstractDevice;
import garagedoor.iot.device.Device;

/**
 *
 * @author iflores
 */

public class MqttDevice<T> extends AbstractDevice implements Device {
    private String topic; 

    public boolean updated; 
    
        
    public MqttDevice() {
        super();
        this.topic = null;
        this.updated = false; 
    }
    
    public MqttDevice(String topic, String id, String status, T data) {
        super(id, status, data);
        this.topic = topic;
        this.updated = false; 
    }
    
    //Getter methods
    public String getTopic() {
        return topic; 
    }

    public boolean isUpdated(){
        return updated;
    }
    
    //Setter methods
    public void setTopic(String topic) {
        this.topic = topic;        
    }

}

