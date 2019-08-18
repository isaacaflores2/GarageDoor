package garagedoor.MqttHttpsBridge;

public interface Bridge<T> {

    public void subscribe();
    public String publish(String deviceId, String content);
    public boolean isSubscribed();

}
