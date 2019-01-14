package com.maoyongxin.myapplication.http.response;

public class BaiduPushResponse extends BaseResp {

    private BaiduPush obj;

    public BaiduPush getObj() {
        return obj;
    }

    public void setObj(BaiduPush obj) {
        this.obj = obj;
    }

    @Override
    public String toString() {
        return "BaiduPushResponse{" +
                "obj=" + obj +
                ", success=" + success +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
