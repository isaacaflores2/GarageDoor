package garagedoor.bridgeController;

import garagedoor.SpringBootMqttHttpsBridge.Bridge;
import garagedoor.iot.device.DeviceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mqttbridge")
public class BridgeController
{
    private Bridge<String> bridge;
    private DeviceManager<String> deviceManager;

    @Autowired
    public BridgeController(Bridge<String> bridge, DeviceManager<String> deviceManager)
    {
        this.bridge = bridge;
        this.deviceManager = deviceManager;
    }
    
    @RequestMapping("/")
    public String index()
    {
        return "<h1>GarageDoor</h1>";
    }

    @GetMapping("/clientstatus")
    public String getMqttBridgeClientStatus()
    {
        String response;
                        
        if( bridge.isConnected()){
            response = "<h1> MQTT Client is running </h1>";
        }
        else                             
            response = "<h1> MQTT Client is not running....FIX IT</h1>";
                        
        return response; 
    }
    
    @GetMapping("/numdevices")
    public int getNumDevices()
    {
        return deviceManager.numDevices();
    }
    
    @GetMapping("/device/data/{deviceId}")
    public String getDeviceData(@PathVariable String deviceId) {

        String data  = deviceManager.getDeviceData(deviceId);

        if(data == null)
            data = "Sorry. There is no available status on your garage right now.";

        return data;
    }
    
    @GetMapping("/device/status/{deviceId}")
    public String getDeviceStatus(@PathVariable String deviceId)
    {
        String status = deviceManager.getDeviceStatus(deviceId);

        if(status == null)
            status = "Sorry. There is no available status on your garage right now.";

        return status;
    }
    
    @GetMapping("/device/toggle/{deviceId}")
    public String toggle(@PathVariable String deviceId) {
        String response = "<h1> The garage door toggle. </h1>";
        bridge.publish(deviceId, "toggle");
        return response;
    }
    
    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<Exception> handleAllExceptions(RuntimeException e) {
        return new ResponseEntity<Exception>(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}
