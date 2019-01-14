package com.maoyongxin.myapplication.entity;

/**
 * Created by yongxin on 2018/1/13.
 */

public class VipMoneyListInfo {

    /**
     * id : 1
     * vipTime : 1
     * vipMoney : 0.01
     * type : 1
     */

    private int id;
    private int vipTime;
    private double vipMoney;
    private int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVipTime() {
        return vipTime;
    }

    public void setVipTime(int vipTime) {
        this.vipTime = vipTime;
    }

    public double getVipMoney() {
        return vipMoney;
    }

    public void setVipMoney(double vipMoney) {
        this.vipMoney = vipMoney;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
