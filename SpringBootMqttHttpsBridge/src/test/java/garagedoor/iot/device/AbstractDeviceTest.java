package garagedoor.iot.device;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class AbstractDeviceTest {

    @Mock
    private AbstractDevice abstractDevice;

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
        when(abstractDevice.toString()).thenReturn(expected);
        assertEquals(expected, abstractDevice.toString());
    }

    @Test
    void getId() {
        when(abstractDevice.getId()).thenReturn(id);
        String returnedId = abstractDevice.getId();
        verify(abstractDevice).getId();
        assertEquals(id, returnedId);
    }

    @Test
    void getStatus() {
        when(abstractDevice.getStatus()).thenReturn(status);
        String returnedStatus = abstractDevice.getStatus();
        verify(abstractDevice).getStatus();
        assertEquals(status, returnedStatus);
    }

    @Test
    void getData() {
        when(abstractDevice.getData()).thenReturn(data);
        String returnedData = (String) abstractDevice.getData();
        verify(abstractDevice).getData();
        assertEquals(data, returnedData);
    }

    @Test
    void setId() {
        doThrow(IllegalArgumentException.class).when(abstractDevice).setId(null);

        doAnswer((invocationOnMock -> {
            assertTrue(id.equals(invocationOnMock.getArgument(0)));
            return null;
        })).when(abstractDevice).setId(id);

        assertThrows(IllegalArgumentException.class, () -> abstractDevice.setId(null));

        when(abstractDevice.getId()).thenReturn(id);
        abstractDevice.setId(id);
        assertEquals(id, abstractDevice.getId());
    }

    @Test
    void setStatus() {
        doThrow(IllegalArgumentException.class).when(abstractDevice).setStatus(null);

        doAnswer((invocationOnMock -> {
            assertTrue(status.equals(invocationOnMock.getArgument(0)));
            return null;
        })).when(abstractDevice).setStatus(status);

        assertThrows(IllegalArgumentException.class, () -> abstractDevice.setStatus(null));

        when(abstractDevice.getStatus()).thenReturn(status);
        abstractDevice.setStatus(status);
        assertEquals(status, abstractDevice.getStatus());
    }

    @Test
    void setData() {
        doThrow(IllegalArgumentException.class).when(abstractDevice).setData(null);

        doAnswer((invocationOnMock -> {
            assertTrue(data.equals(invocationOnMock.getArgument(0)));
            return null;
        })).when(abstractDevice).setData(data);

        assertThrows(IllegalArgumentException.class, () -> abstractDevice.setData(null));

        when(abstractDevice.getData()).thenReturn(data);
        abstractDevice.setData(data);
        assertEquals(data, abstractDevice.getData());
    }
}