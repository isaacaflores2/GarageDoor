package garagedoor.Controllers;

import java.util.List;
import java.util.Set;

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
public class DeviceManagerControllerImpl implements DeviceManagerController
{
    private DeviceManager deviceManager;

    @Autowired
    public DeviceManagerControllerImpl(DeviceManager deviceManager)
    {
        this.deviceManager = deviceManager;
    }

    @Override
    public String index()
    {
        return "MqttDevice Manager home";
    }

    @Override
    public int getNumDevices()
    {
        return deviceManager.numDevices();
    }

    @Override
    public Set<Device> getDevices()
    {
        return deviceManager.devices();
    }

    @Override
    public Device getDevice(@PathVariable String id)
    {
        return deviceManager.getDevice(id);
    }

    @Override
    public boolean containsDevice(@PathVariable String id)
    {
        return deviceManager.contains(id);
    }

    @Override
    public String getDeviceStatus(@PathVariable String id)
    {
        return deviceManager.getDeviceStatus(id);
    }

    @Override
    public void updateDeviceStatus(@PathVariable String id, @PathVariable String status)
    {
        deviceManager.updateDeviceStatus(id, status);
    }
    
    @Override
    public final ResponseEntity<Exception> handleAllExceptions(RuntimeException e)
    {
        return new ResponseEntity<Exception>(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}
