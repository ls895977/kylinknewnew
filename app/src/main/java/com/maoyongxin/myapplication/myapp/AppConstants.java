package com.maoyongxin.myapplication.myapp;

/**
 * Created by dingke on 2017/8/3.
 */

public interface AppConstants {
    /* 工程环境 */
    /**
     * 内网测试
     */
    int ENV_DEVELOP = -1;
    /**
     * 外网测试
     */
    int ENV_BETA = 0;
    /**
     * 外网正式
     */
    int ENV_RELEASE = 1;

    int ENV_TMP = 2;

    /* preference constants */
    String PREFERENCE_KEY_CURRENT_TOKEN = "PREFERENCE_KEY_CURRENT_TOKEN";
    String PREFERENCE_KEY_NEED_GUIDE = "PREFERENCE_KEY_NEED_GUIDE";
    String PREFERENCE_KEY_CITY_CODE = "PREFERENCE_KEY_CITYCODE";
    String PREFERENCE_KEY_CITY_NAME = "PREFERENCE_KEY_CITY_NAME";
    String PREFERENCE_KEY_JPUSH_ALIAS = "PREFERENCE_KEY_JPUSH_ALIAS";

    /*接口调用key*/
    String API_KEY = "VVNFUk5BTUUyMDE3MDgyNQ==";
    /*登录接口android端识别码*/
    String LOGIN_TYPE = "7562e88b1bf04872aece324129f3be87";
    /* 广播 */
    String BROADCAST_BAD_TOKEN = "BROADCAST_BAD_TOKEN";
    String FINISHALL = "FINISH_ALL";

    Integer SIGN_IN_STATE = 1;
    Integer SIGN_OUT_STATE = 2;
}
