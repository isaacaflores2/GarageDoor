package garagedoor.iot.device;

public interface DeviceMap<K,V> {

    public V getDeviceMapValue(K id);
    public V getDeviceMapInverseValue(V inverseId);

    public void removeDevice(K id);
    public void addDevice(K id, V value);
}
