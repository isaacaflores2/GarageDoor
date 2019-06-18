package garagedoor.bridgeController;

import garagedoor.SpringBootMqttHttpsBridge.Bridge;

import garagedoor.iot.device.DeviceManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BridgeControllerTest {

    @InjectMocks
    private BridgeController bridgeController;

    @Mock
    private Bridge<String> bridge;

    @Mock
    private DeviceManager<String> deviceManager;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void index() {
        String index = "<h1>GarageDoor</h1>";
        assertEquals(index,bridgeController.index());
    }

    @Test
    public void getMqttBridgeClientStatus() {
        String expecedResponse = "<h1> MQTT Client is running </h1>";
        when(bridge.isConnected()).thenReturn(true);
        String response = bridgeController.getMqttBridgeClientStatus();
        assertEquals(expecedResponse, response);

        expecedResponse = "<h1> MQTT Client is not running....FIX IT</h1>";
        when(bridge.isConnected()).thenReturn(false);
        response = bridgeController.getMqttBridgeClientStatus();
        assertEquals(expecedResponse, response);

    }

    @Test
    public void getNumDevices() {
        int expectedNumDevices = 2;
        when(deviceManager.numDevices()).thenReturn(expectedNumDevices);
        int numDevices = bridgeController.getNumDevices();
        verify(deviceManager).numDevices();
        assertEquals(expectedNumDevices, numDevices);
    }

    @Test
    public void getDeviceData() {
        String expectedData = "open";
        String id = "2";
        when(deviceManager.getDeviceData(id)).thenReturn(expectedData);
        String data = bridgeController.getDeviceData(id);
        verify(deviceManager).getDeviceData(id);
        assertEquals(expectedData, data);
    }

    @Test
    public void getDeviceStatus() {
        String expectedStatus = "running";
        String id = "2";
        when(deviceManager.getDeviceData(id)).thenReturn(expectedStatus);
        String status = bridgeController.getDeviceData(id);
        verify(deviceManager).getDeviceData(id);
        assertEquals(expectedStatus, status);
    }

    @Test
    public void toggle() {
        String expectedResponse = "<h1> The garage door toggle. </h1>";
        String id = "1";
        when(bridge.publish(id, "toggle")).thenReturn(expectedResponse);
        String response = bridgeController.toggle(id);
        verify(bridge).publish(id, "toggle");
        assertEquals(expectedResponse, response);
    }

    @Test
    public void handleAllExceptions() {
        RuntimeException e = new RuntimeException();
        assertEquals(new ResponseEntity<Exception>(e, HttpStatus.INTERNAL_SERVER_ERROR), bridgeController.handleAllExceptions(e));
    }
}