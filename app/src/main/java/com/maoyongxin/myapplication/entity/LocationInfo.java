package com.maoyongxin.myapplication.entity;

/**
 * Created by maoyongxin on 2017/11/29.
 */

public class LocationInfo {
    private String address;
    private String latitude;
    private String lngtitude;
    private String cityName;
    private String adCode;

    public String getAdCode() {
        return adCode;
    }

    public void setAdCode(String adCode) {
        this.adCode = adCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtitude() {
        return lngtitude;
    }

    public void setLngtitude(String lngtitude) {
        this.lngtitude = lngtitude;
    }
}
