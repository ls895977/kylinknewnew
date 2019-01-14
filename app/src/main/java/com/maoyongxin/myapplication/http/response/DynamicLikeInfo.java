package com.maoyongxin.myapplication.http.response;

public class DynamicLikeInfo {
    private int id;
    private long createdTime;
    private int uid;
    private long dynamicId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public long getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(long dynamicId) {
        this.dynamicId = dynamicId;
    }

    @Override
    public String toString() {
        return "DynamicLikeInfo{" +
                "id=" + id +
                ", createdTime=" + createdTime +
                ", uid=" + uid +
                ", dynamicId=" + dynamicId +
                '}';
    }
}
