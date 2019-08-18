package garagedoor.iot.device;

public interface Device<T> {

    public String getId();

    public String getTopic();

    public String getStatus();

    public T getData();

    public void setId(String id);

    public void setTopic(String topic);

    public void setStatus(String status);

    public void setData(T data);
}
