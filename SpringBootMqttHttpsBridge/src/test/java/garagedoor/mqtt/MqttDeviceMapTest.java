package garagedoor.mqtt;

import garagedoor.config.Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class MqttDeviceMapTest {

    public String topic1  = "garage/door/toggle";
    public String topic2  = "garage/sensor/door";
    public String id1 = "1";
    public String id2 = "2";
    public String status = "default";
    public String data = "open";
    public int numDevices = 2;

    public String testKey = "testKey";
    public String testValue = "testValue";

    public Config config;
    public MqttDeviceMap mqttDeviceMap;

    @BeforeEach
    public void setUp() throws Exception {
        config = new Config();
        config.mqttTopics = new String[]{topic1, topic2};
        config.deviceIds = new String[]{id1, id2};
        config.numDevices = "2";
        config.mqttBrokerAddress = "";
        config.mqttClientId = "";
        config.mqttBrokerUsername = "";
        config.mqttBrokerPassword = "";
        mqttDeviceMap = new MqttDeviceMap(config);
    }

    @Test
    public void getDeviceMapValue() {

        assertThrows(IllegalArgumentException.class, () -> {mqttDeviceMap.getDeviceMapValue(null);});

        mqttDeviceMap.addDevice(testKey, testValue);
        String returnedValue = mqttDeviceMap.getDeviceMapValue(testKey);
        assertEquals(testValue,returnedValue);
    }

    @Test
    public void getDeviceMapInverseValue() {

        assertThrows(IllegalArgumentException.class, () -> {mqttDeviceMap.getDeviceMapInverseValue(null);});

        mqttDeviceMap.addDevice(testKey, testValue);
        String returnedValue = mqttDeviceMap.getDeviceMapInverseValue(testValue);
        assertEquals(testKey,returnedValue);
    }

    @Test
    public void removeDevice() {

        assertThrows(IllegalArgumentException.class, () -> {mqttDeviceMap.removeDevice(null);});

        mqttDeviceMap.addDevice(testKey, testValue);
        mqttDeviceMap.removeDevice(testKey);
        String returnedValue = mqttDeviceMap.getDeviceMapValue(testKey);
        assertNull(returnedValue);
    }

    @Test
    public void addDevice() {

        assertThrows(IllegalArgumentException.class, () -> {mqttDeviceMap.addDevice(null, "value");});

        mqttDeviceMap.addDevice(testKey, testValue);
        String returnedValue = mqttDeviceMap.getDeviceMapValue(testKey);
        assertEquals(testValue,returnedValue);
    }
}