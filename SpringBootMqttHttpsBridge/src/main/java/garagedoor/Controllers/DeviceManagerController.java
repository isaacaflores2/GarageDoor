package garagedoor.Controllers;

import garagedoor.iot.device.Device;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;

public interface DeviceManagerController {

    @RequestMapping("/")
    public String index();

    @RequestMapping("/numdevices")
    public int getNumDevices();

    @RequestMapping("/getdevices")
    public Set<Device> getDevices();

    @RequestMapping("/get/{getId}")
    public Device getDevice(@PathVariable String id);

    @RequestMapping("/contains/{getId}")
    public boolean containsDevice(@PathVariable String id);

    @RequestMapping("/getStatus/{getId}")
    public String getDeviceStatus(@PathVariable String id);

    @RequestMapping("/updatestatus/{getId}/getStatus/{getStatus}")
    public void updateDeviceStatus(@PathVariable String id, @PathVariable String status);

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Exception> handleAllExceptions(RuntimeException e);
}
