package garagedoor.iot.device;

public interface Device<T> {

    public String getId();
    public String getStatus();
    public T getData();

    public void setId(String id);
    public void setStatus(String status);
    public void setData(T data);
}
