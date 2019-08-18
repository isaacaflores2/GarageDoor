package garagedoor.iot.device;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Component
public interface DeviceManager<T> {
    public int numDevices();

    public Set<Device<T>> devices();

    public void addDevice(Device<T> device);

    public void removeDevice(Device<T> device);

    public Device<T> getDevice(String id);

    public Device<T> getDeviceFromTopic(String topic);

    public boolean contains(String id);

    public void loadDevices(Object o);

    public T getDeviceData(String id);

    public String getDeviceStatus(String id);

    public void updateDeviceStatus(String id, String status);

    public void updateDeviceData(String id, T data);

}
