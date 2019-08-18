package garagedoor.mqtt;

import garagedoor.iot.device.Device;

public class MqttDevice<T> implements Device<T> {

    private String id;
    private String topic;
    private String status;
    private T data;

    public MqttDevice() {
        this.id = null;
        this.status = null;
        this.data = null;
    }

    public MqttDevice(String id, String topic, String status, T data) {
        this.id = id;
        this.topic = topic;
        this.status = status;
        this.data = data;
    }

    @Override
    public String toString() {
        return "id: " + id + " , topic:  " + topic + " , status: " + status + " , data: " + data;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getTopic() {
        return topic;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public void setId(String id) {
        if (id != null)
            this.id = id;
        else
            throw new IllegalArgumentException("Provided id is null");
    }

    @Override
    public void setTopic(String topic) {
        if (topic != null)
            this.topic = topic;
        else
            throw new IllegalArgumentException("Provided topic is null");
    }

    @Override
    public void setStatus(String status) {
        if (status != null)
            this.status = status;
        else
            throw new IllegalArgumentException("Provided status is null");
    }

    @Override
    public void setData(T data) {
        if (data != null)
            this.data = data;
        else
            throw new IllegalArgumentException("Provided data is null");
    }
}
