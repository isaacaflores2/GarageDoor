package rocks.myddns.isaacaflores2.SpringBootMqttHttpsBridge;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author iflores
 */
@RestController
@RequestMapping("/mqttbridge")
public class MqttBridgeController 
{
    private MqttBridge mqttBridgeClient;
    
    public MqttBridgeController(MqttBridge mqttBridgeClient)
    {
        this.mqttBridgeClient = mqttBridgeClient;
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
                        
        if( mqttBridgeClient.isConnected()){
            response = "<h1> MQTT Client is running </h1>";
        }
        else                             
            response = "<h1> MQTT Client is not running....FIX IT</h1>";
                        
        return response; 
    }
    
    @GetMapping("/numdevices")
    public int getNumDevices()
    {
        int numDevices;
        numDevices = mqttBridgeClient.getNumDevices();
        return numDevices;
    }
    
    @GetMapping("/device/data/{deviceId}")
    public String getDeviceData(@PathVariable String deviceId)
    {
        String deviceData;
        deviceData = mqttBridgeClient.getDeviceData(deviceId);                        
        return deviceData; 
    }
    
    @GetMapping("/device/status/{deviceId}")
    public String getDeviceStatus(@PathVariable String deviceId)
    {
        String deviceData;
        deviceData = mqttBridgeClient.getDeviceStatus(deviceId);                        
        return deviceData; 
    }
    
    @GetMapping("/device/toggle/{deviceId}")
    public String toggle(@PathVariable String deviceId)
    {
        String response = "<h1> The garage door toggle. </h1>";
        String topic = mqttBridgeClient.getDeviceTopic(deviceId);
        mqttBridgeClient.publish(topic, "toggle");                     
        return response;
    }
    
    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<Exception> handleAllExceptions(RuntimeException e)
    {
        return new ResponseEntity<Exception>(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}
