/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bb_garagedooropener;

/**
 *
 * @author iflores
 */
public class MqttDevice 
{
    private String topic; 
    private String id; 
    private String status; 
    private Object data; 
    public boolean updated; 
    
    public MqttDevice() 
    {
        this.topic = null;
        this.id = null;
        this.status = null;
        this.data = null; 
        this.updated = false; 
    }
    
    public MqttDevice(String topic, String id, String status, Object data)
    {
        this.topic = topic;
        this.id = id;
        this.status = status;
        this.data = data; 
        this.updated = false; 
    }
    
    public String topic()
    {
        return topic; 
    }
    
    public String id()
    {
        return id; 
    }
    
    public String status()
    {
        return status; 
    }
    
    public Object data()
    {
        return data; 
    }
    
    public boolean isUpdated(){
        return updated;
    }
    
    public void updateId(String id)
    {
        this.id = id; 
    }
    
    public void updateStatus(String status)
    {
        this.status = status;
    }
    
    public void updateData(String data)
    {
        this.data = data;
    }
    
    public void  setUpdateFlag(boolean status)
    {
        this.updated = status; 
    }
    
    public void callback()
    {
        return; 
    }
    
}

