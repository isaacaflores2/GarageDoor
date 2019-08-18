package garagedoor.Controllers;

import java.util.List;

import garagedoor.iot.device.Device;
import garagedoor.iot.device.DeviceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/devicemanager")
public class DeviceManagerController 
{
    private DeviceManager deviceManager;

    @Autowired
    public DeviceManagerController(DeviceManager deviceManager)
    {
        this.deviceManager = deviceManager;
    }

    @RequestMapping("/")
    public String index()
    {
        return "MqttDevice Manager home";
    }

    @RequestMapping("/numdevices")
    public int getNumDevices()
    {
        return deviceManager.numDevices();
    }
    
    @RequestMapping("/getdevices")
    public List<Device> getDevices()
    {
        return deviceManager.devices();
    }

    @RequestMapping("/get/{getId}")
    public Device getDevice(@PathVariable String id)
    {
        return deviceManager.getDevice(id);
    }
    
    @RequestMapping("/contains/{getId}")
    public boolean containsDevice(@PathVariable String id)
    {
        return deviceManager.contains(id);
    }
          
    @RequestMapping("/getStatus/{getId}")
    public String getDeviceStatus(@PathVariable String id)
    {
        return deviceManager.getDeviceStatus(id);
    }
          
    @RequestMapping("/updatestatus/{getId}/getStatus/{getStatus}")
    public void updateDeviceStatus(@PathVariable String id, @PathVariable String status)
    {
        deviceManager.updateDeviceStatus(id, status);
    }
    
    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<Exception> handleAllExceptions(RuntimeException e)
    {
        return new ResponseEntity<Exception>(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}
