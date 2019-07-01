package garagedoor.mqtt;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import garagedoor.config.Config;
import garagedoor.iot.device.DeviceMap;
import org.springframework.stereotype.Component;

@Component
public class MqttDeviceMap implements DeviceMap<String, String> {

    private BiMap<String, String> biMap;
    private Config config;

    public MqttDeviceMap(Config config){

        this.config = config;
        this.biMap = HashBiMap.create();
        loadConfig();
    }

    private void loadConfig(){
        java.lang.String[] topics = config.mqttTopics;
        java.lang.String[] ids = config.deviceIds;

        if(topics == null || ids == null )
            throw new NullPointerException("Config is null. Check your application.properties file or reference.");

        if(topics.length != ids.length)
            throw new RuntimeException("Config issue. Number of Mqtt topics does not match number of device Ids");

        for (int i = 0; i < ids.length; i++) {
            biMap.put(ids[i], topics[i]);
        }
    }

    @Override
    public String getDeviceMapValue(String id) {
        if( id == null )
            throw new IllegalArgumentException("String id cannot be null");
        else
            return biMap.get(id);
    }

    @Override
    public String getDeviceMapInverseValue(String inverseId) {
        if( inverseId == null )
            throw new IllegalArgumentException("String inverseId cannot be null");
        else
            return biMap.inverse().get(inverseId);
    }

    @Override
    public void removeDevice(String id) {
        if( id == null )
            throw new IllegalArgumentException("String id cannot be null");

        if(biMap.containsKey(id))
            biMap.remove(id);
    }

    @Override
    public void addDevice(String id, String value) {
        if( id == null )
            throw new IllegalArgumentException("String id cannot be null");

        biMap.put((String) id, (String) value);
    }
}
