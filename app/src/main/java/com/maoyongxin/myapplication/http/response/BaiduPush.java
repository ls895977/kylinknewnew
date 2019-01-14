package com.maoyongxin.myapplication.http.response;

public class BaiduPush {
    private int id;
    private String userId;
    private String channelId;
    private int deviceType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    @Override
    public String toString() {
        return "BaiduPush{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", channelId='" + channelId + '\'' +
                ", deviceType=" + deviceType +
                '}';
    }
}
