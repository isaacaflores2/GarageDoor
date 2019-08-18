package garagedoor.iot.device;

import garagedoor.mqtt.MqttDevice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class MqttDeviceTest {

    @Mock
    private MqttDevice mqttDevice;

    private String id;
    private String status;
    private String data;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        id = "1";
        status = "running";
        data = "data";

    }

    @Test
    void toString1() {
        String expected = "id: " + id + ", status: " + status + ", data: " + data;
        when(mqttDevice.toString()).thenReturn(expected);
        assertEquals(expected, mqttDevice.toString());
    }

    @Test
    void getId() {
        when(mqttDevice.getId()).thenReturn(id);
        String returnedId = mqttDevice.getId();
        verify(mqttDevice).getId();
        assertEquals(id, returnedId);
    }

    @Test
    void getStatus() {
        when(mqttDevice.getStatus()).thenReturn(status);
        String returnedStatus = mqttDevice.getStatus();
        verify(mqttDevice).getStatus();
        assertEquals(status, returnedStatus);
    }

    @Test
    void getData() {
        when(mqttDevice.getData()).thenReturn(data);
        String returnedData = (String) mqttDevice.getData();
        verify(mqttDevice).getData();
        assertEquals(data, returnedData);
    }

    @Test
    void setId() {
        doThrow(IllegalArgumentException.class).when(mqttDevice).setId(null);

        doAnswer((invocationOnMock -> {
            assertTrue(id.equals(invocationOnMock.getArgument(0)));
            return null;
        })).when(mqttDevice).setId(id);

        assertThrows(IllegalArgumentException.class, () -> mqttDevice.setId(null));

        when(mqttDevice.getId()).thenReturn(id);
        mqttDevice.setId(id);
        assertEquals(id, mqttDevice.getId());
    }

    @Test
    void setStatus() {
        doThrow(IllegalArgumentException.class).when(mqttDevice).setStatus(null);

        doAnswer((invocationOnMock -> {
            assertTrue(status.equals(invocationOnMock.getArgument(0)));
            return null;
        })).when(mqttDevice).setStatus(status);

        assertThrows(IllegalArgumentException.class, () -> mqttDevice.setStatus(null));

        when(mqttDevice.getStatus()).thenReturn(status);
        mqttDevice.setStatus(status);
        assertEquals(status, mqttDevice.getStatus());
    }

    @Test
    void setData() {
        doThrow(IllegalArgumentException.class).when(mqttDevice).setData(null);

        doAnswer((invocationOnMock -> {
            assertTrue(data.equals(invocationOnMock.getArgument(0)));
            return null;
        })).when(mqttDevice).setData(data);

        assertThrows(IllegalArgumentException.class, () -> mqttDevice.setData(null));

        when(mqttDevice.getData()).thenReturn(data);
        mqttDevice.setData(data);
        assertEquals(data, mqttDevice.getData());
    }
}