package com.maoyongxin.myapplication.wxapi;

public class AccessTokenBean {

    /**
     * access_token : 17_SB45RbBTPsDHoOIZCGJcNX2ByGJ69klFA8aoJH84_2P-TKqI2jamw9nvMx__etkq4rP-nturj9qaKOVMdIXRUqZuxKTUil8ALyoUsnPHgi0
     * expires_in : 7200
     * refresh_token : 17_P-2iLEJRjq2zSyiexR_6FVcNNjjhezjK8o7ZdsY-76Dr8q2wv9yQ8nduNB1irvHfDEM0vxRM7UofKGYwKhYkKEaCrLyeTvGbr7Vigc0agj8
     * openid : oGNI207voc3x0Rg7PnypRtD7dD0U
     * scope : snsapi_userinfo
     * unionid : oFn820zcDf5SnkEPWn3c2_cuW4qI
     */

    private String access_token;
    private int expires_in;
    private String refresh_token;
    private String openid;
    private String scope;
    private String unionid;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
}
