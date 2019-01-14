package com.maoyongxin.myapplication.entity;

/**
 * Created by yongxin on 2017/12/23.
 */

public class ChangUserInfo {

    /**
     * userId : 10032
     * userName : 大慈寺好
     * longitude : 106.700199
     * latitude : 26.554907
     * token : eL3Mekzu1YjfCpYafNuXXNpSRhamXgLNKSGtRUbLwkBa6V8cMezoFGiCPy+oR9CHL08VBc66BbKoPmgF3TeMzQ==
     * note : 我是么么哒
     * headImg : 6296049c37384542bacf0211442070da.jpg
     * userPhone : null
     * sex : null
     * brithday : null
     * vipNum : 0
     */

    private int userId;
    private String userName;
    private double longitude;
    private double latitude;
    private String token;
    private String note;
    private String headImg;
    private String userPhone;
    private String sex;
    private String brithday;
    private int vipNum;

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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBrithday() {
        return brithday;
    }

    public void setBrithday(String brithday) {
        this.brithday = brithday;
    }

    public int getVipNum() {
        return vipNum;
    }

    public void setVipNum(int vipNum) {
        this.vipNum = vipNum;
    }
}
