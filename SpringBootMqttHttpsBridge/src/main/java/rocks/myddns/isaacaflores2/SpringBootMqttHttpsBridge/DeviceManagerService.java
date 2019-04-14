package rocks.myddns.isaacaflores2.SpringBootMqttHttpsBridge;

import java.util.ArrayList;

/**
 * @author iflores
 */

public interface DeviceManagerService 
{       
    public int numDevices();
    
    public ArrayList<Device> devices();
    
    public void addDevice(Device device);
    
    public void removeDevice(Device device);
    
    public Device getDevice(String topic);
    
    public boolean contains(String topic);
        
    public void loadDevices(Object o);
                   
    public Object getDeviceData(String topic);
    
    public String getDeviceStatus(String topic);
          
    public boolean updateDeviceStatus(String topic, String status);  
    
    public boolean updateDeviceData(String topic, String data);  
}
