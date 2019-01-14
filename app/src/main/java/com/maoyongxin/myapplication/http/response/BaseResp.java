package com.maoyongxin.myapplication.http.response;

import java.io.Serializable;

/**
 * Created by dingke on 2017/8/3.
 */

public class BaseResp implements Serializable {
    public boolean success;
    public int code;
    public String msg;

    public boolean is200() {
        return code == 200;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isBadToken() {
        return code == 1403 || code == 1404;
    }

    @Override
    public String toString() {
        return "BaseResp{" +
                "success=" + success +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
