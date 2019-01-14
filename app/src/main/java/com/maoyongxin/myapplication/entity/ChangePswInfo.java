package com.maoyongxin.myapplication.entity;

/**
 * Created by yongxin on 2018/1/13.
 */

public class ChangePswInfo {

    /**
     * userId : 10069
     * userName : 毛毛虫
     * longitude : 106.700419
     * latitude : 26.554834
     * token : 3SA48qodhVa3cJGkTlApfunJ3G0F4AJbTnZbdQO+ezwuiyHu9ZpOJ4pEGQPXu0Bsz1K516C059pRG3qF/+QmHA==
     * note :
     * headImg : 0dda78801d9342d8b19bd130b2d4fd05.jpg
     * userPhone : 13595062746
     * sex : 0
     * brithday : null
     * vipNum : 0
     * isPublish : null
     * isLogin : null
     * acceptInfo : null
     * isPublicInfo : null
     */

    private int userId;
    private String userName;
    private double longitude;
    private double latitude;
    private String token;
    private String note;
    private String headImg;
    private String userPhone;
    private int sex;
    private Object brithday;
    private int vipNum;
    private Object isPublish;
    private Object isLogin;
    private Object acceptInfo;
    private Object isPublicInfo;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public Object getBrithday() {
        return brithday;
    }

    public void setBrithday(Object brithday) {
        this.brithday = brithday;
    }

    public int getVipNum() {
        return vipNum;
    }

    public void setVipNum(int vipNum) {
        this.vipNum = vipNum;
    }

    public Object getIsPublish() {
        return isPublish;
    }

    public void setIsPublish(Object isPublish) {
        this.isPublish = isPublish;
    }

    public Object getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(Object isLogin) {
        this.isLogin = isLogin;
    }

    public Object getAcceptInfo() {
        return acceptInfo;
    }

    public void setAcceptInfo(Object acceptInfo) {
        this.acceptInfo = acceptInfo;
    }

    public Object getIsPublicInfo() {
        return isPublicInfo;
    }

    public void setIsPublicInfo(Object isPublicInfo) {
        this.isPublicInfo = isPublicInfo;
    }
}
