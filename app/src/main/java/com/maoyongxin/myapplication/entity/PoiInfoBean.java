package com.maoyongxin.myapplication.entity;

import java.io.Serializable;

/**
 * Created by maoyongxin on 2017/12/4.
 */

public class PoiInfoBean implements Serializable{
    private String latitude;
    private String lngtitude;
    private String poiName;
    private String poiAddress;
    private String poiType;
    private String poiDistance;
    private String targetId;
    private int poinote;




    public String getPoiType() {
        return poiType;
    }

    public void setPoiType(String poiType) {
        this.poiType = poiType;
    }

    public String getPoiDistance() {
        return poiDistance;
    }

    public void setPoiDistance(String poiDistance) {
        this.poiDistance = poiDistance;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLngtitude() {
        return lngtitude;
    }

    public void setLngtitude(String lngtitude) {
        this.lngtitude = lngtitude;
    }

    public String getPoiName() {
        return poiName;
    }

    public void setPoiName(String poiName) {
        this.poiName = poiName;
    }

    public String getPoiAddress() {
        return poiAddress;
    }

    public void setPoiAddress(String poiAddress) {
        this.poiAddress = poiAddress;
    }

    public void setTargetId(String targetId){this.targetId=targetId;}

    public String getTargetId(){return  targetId; }

    public void setPoinote(int poinote){ this.poinote=poinote;}

    public  int getPoinote(){return  poinote;}
}
