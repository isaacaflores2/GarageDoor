package garagedoor.Controllers;


import garagedoor.iot.device.Device;
import garagedoor.iot.device.DeviceManager;
import garagedoor.mqtt.MqttDevice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class DeviceManagerControllerImplTest {

    @InjectMocks
    private DeviceManagerControllerImpl deviceManagerControllerImpl;

    @Mock
    private DeviceManager deviceManager;

    private Device<String> expectedDevice;
    private Set<Device> expectedDeviceSet;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        expectedDevice = new MqttDevice("topic", "1", "running", "data");
        expectedDeviceSet = new HashSet();
        expectedDeviceSet.add(expectedDevice);
    }

    @Test
    public void index() {
        String index = "MqttDevice Manager home";
        assertEquals(index, deviceManagerControllerImpl.index());
    }

    @Test
    public void getNumDevices() {
        int expectedNumDevices = 2;
        when(deviceManager.numDevices()).thenReturn(expectedNumDevices);
        int numDevices = deviceManagerControllerImpl.getNumDevices();
        verify(deviceManager).numDevices();
        assertEquals(expectedNumDevices, numDevices);
    }

    @Test
    public void getDevices() {
        when(deviceManager.devices()).thenReturn(expectedDeviceSet);
        Set<Device> deviceList = deviceManagerControllerImpl.getDevices();
        verify(deviceManager).devices();
        assertEquals(expectedDeviceSet, deviceList);
    }

    @Test
    public void getDevice() {
        String id = "1";
        when(deviceManager.getDevice(id)).thenReturn(expectedDevice);
        Device device = deviceManagerControllerImpl.getDevice(id);
        verify(deviceManager).getDevice(id);
        assertEquals(expectedDevice, device);
    }

    @Test
    public void containsDevice() {
        String id = "1";
        when(deviceManager.contains(id)).thenReturn(true);
        boolean returnedBool =  deviceManagerControllerImpl.containsDevice(id);
        verify(deviceManager).contains(id);
        assert(returnedBool);
    }

    @Test
    public void getDeviceStatus() {
        String id = "1";
        String expectedStatus = "running";
        when(deviceManager.getDeviceStatus(id)).thenReturn(expectedStatus);
        String status  = deviceManagerControllerImpl.getDeviceStatus(id);
        verify(deviceManager).getDeviceStatus(id);
        assertEquals(expectedStatus, status);
    }

    @Test
    void updateDeviceStatus(){
        String id = "1";
        String expectedStatus = "running";

        doAnswer((i) -> {
            assertEquals(id, i.getArgument(0));
            assertTrue(expectedStatus.equals(i.getArgument(1)));
            return null;
        }).when(deviceManager).updateDeviceStatus(id, expectedStatus);

        when(deviceManager.getDeviceStatus(id)).thenReturn(expectedStatus);

        deviceManagerControllerImpl.updateDeviceStatus(id, expectedStatus);
        assertEquals(expectedStatus, deviceManagerControllerImpl.getDeviceStatus(id));
    }

    @Test
    public void handleAllExceptions() {
        RuntimeException e = new RuntimeException();
        assertEquals(new ResponseEntity<Exception>(e, HttpStatus.INTERNAL_SERVER_ERROR), deviceManagerControllerImpl.handleAllExceptions(e));
    }
}