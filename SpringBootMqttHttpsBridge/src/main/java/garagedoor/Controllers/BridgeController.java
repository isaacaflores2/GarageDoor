package garagedoor.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

public interface BridgeController {
    @RequestMapping("/")
    public String index();

    @GetMapping("/clientstatus")
    public String getMqttBridgeClientStatus();

    @GetMapping("/numdevices")
    public int getNumDevices();

    @GetMapping("/device/data/{deviceId}")
    public String getDeviceData(@PathVariable String deviceId);

    @GetMapping("/device/status/{deviceId}")
    public String getDeviceStatus(@PathVariable String deviceId);

    @GetMapping("/device/toggle/{deviceId}")
    public String toggle(@PathVariable String deviceId);

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Exception> handleAllExceptions(RuntimeException e);
}
