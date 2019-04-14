package rocks.myddns.isaacaflores2.SpringBootMqttHttpsBridge;

import java.util.ArrayList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author iflores
 */

@RestController
@RequestMapping("/devicemanager")
public class DeviceManagerController 
{
    private DeviceManagerService deviceManagerService; 

    public DeviceManagerController(DeviceManagerService deviceManagerService) 
    {
        this.deviceManagerService = deviceManagerService; 
    }
      
    /*
    *TO DO: 
    1) Provide useful output for index mapping    
    */
    @RequestMapping("/")
    public String index()
    {
        return "Device Manager home";
    }

    @RequestMapping("/numdevices")
    public int getNumDevices()
    {
        return deviceManagerService.numDevices(); 
    }
    
    @RequestMapping("/getdevices")
    public ArrayList<Device> getDevices()
    {
        return deviceManagerService.devices();
    }
    
    @RequestMapping("/loadDevices")
    public void loadDevices()
    {
        deviceManagerService.loadDevices("object");
    }
  
    @RequestMapping("/get/{id}")
    public Device getDevice(@PathVariable String id)
    {
        return deviceManagerService.getDevice(id);
    }
    
    @RequestMapping("/contains/{id}")
    public boolean containsDevice(@PathVariable String id)
    {
        return deviceManagerService.contains(id);
    }
          
    @RequestMapping("/status/{id}")
    public String getDeviceStatus(@PathVariable String id)
    {
        return deviceManagerService.getDeviceStatus(id);
    }
          
    @RequestMapping("/updatestatus/{id}/status/{status}")
    public boolean updateDeviceStatus(@PathVariable String id, @PathVariable String status)
    {
        return deviceManagerService.updateDeviceStatus(id, status);
    }
    
    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<Exception> handleAllExceptions(RuntimeException e)
    {
        return new ResponseEntity<Exception>(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}
