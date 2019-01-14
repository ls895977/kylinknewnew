package com.maoyongxin.myapplication.entity;

/**
 * Created by maoyongxin on 2017/12/20.
 */

public class FollowInfo {

    /**
     * userId : 10033
     * followUserId : 10032
     * note :
     * followDate : 1513754270184
     * id : 1
     */

    private String userId;
    private String followUserId;
    private String note;
    private String followDate;
    private String id;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFollowUserId() {
        return followUserId;
    }

    public void setFollowUserId(String followUserId) {
        this.followUserId = followUserId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getFollowDate() {
        return followDate;
    }

    public void setFollowDate(String followDate) {
        this.followDate = followDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
