package com.maoyongxin.myapplication.entity;

import java.io.Serializable;

/**
 * Created by maoyongxin on 2017/11/27.
 */

public class UserRespone implements Serializable {
    int code;//返回码，200为正常
    String token;//用户Token
    String userId;//用户Id

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
