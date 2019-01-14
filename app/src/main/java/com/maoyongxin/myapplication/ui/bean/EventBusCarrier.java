package com.maoyongxin.myapplication.ui.bean;

import com.maoyongxin.myapplication.wxapi.WXUserBean;

public class EventBusCarrier {
    private String eventType; //区分事件的类型
    private Object object;  //事件的实体类

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
    String openid;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public WXUserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(WXUserBean userBean) {
        this.userBean = userBean;
    }

    private WXUserBean userBean;
}
