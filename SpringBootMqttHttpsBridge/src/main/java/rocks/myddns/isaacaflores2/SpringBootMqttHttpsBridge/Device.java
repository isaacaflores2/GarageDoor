/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.myddns.isaacaflores2.SpringBootMqttHttpsBridge;

/**
 *
 * @author iflores
 */

public class Device 
{
    private String topic; 
    private String id;  
    private String status; 
    private Object data; 
    public boolean updated; 
    
        
    public Device() 
    {
        this.topic = null;
        this.id = null;
        this.status = null;
        this.data = null; 
        this.updated = false; 
    }
    
    public Device(String topic, String id, String status, Object data)
    {
        this.topic = topic;
        this.id = id;
        this.status = status;
        this.data = data; 
        this.updated = false; 
    }
    
    //Getter methods
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
    
    //Setter methods
    public void updateTopic(String topic)
    {
        this.topic = topic;        
    }
    
    
    public void updateId(String id)
    {
        this.id = id; 
    }
    
    public boolean updateStatus(String status)
    {
        if(status == null || status.isEmpty() )
        {
            this.updated = false; 
            return false; 
        }
        
        this.status = status;
        this.updated = true;
        return true; 
    }
    
    public boolean updateData(Object data)
    {
        this.data = data;
        return true;
    }
       
    public void callback()
    {
        return; 
    }    
}

