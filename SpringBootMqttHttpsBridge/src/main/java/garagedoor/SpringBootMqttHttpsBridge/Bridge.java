package garagedoor.SpringBootMqttHttpsBridge;

public interface Bridge<T> {

    public void subscribe();
    public String publish(String deviceId, String content);
    public boolean isConnected();

}
