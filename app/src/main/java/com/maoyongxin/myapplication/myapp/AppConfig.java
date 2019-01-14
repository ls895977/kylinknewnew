package com.maoyongxin.myapplication.myapp;

/**
 * Created by dingke on 2017/8/3.
 */

public class AppConfig {

    /**
     * todo 打包时要修改DEBUG模式
     */
    public static final boolean DEBUG = true;
    /**
     * todo 打包时要修改环境配置
     */
    public static final int ENVIRONMENT = AppConstants.ENV_TMP;

    public static final int DEVICE_TYPE = 3;

    //web服务器的根域名
    public static String sRootUrl;
    public static String getTokenUrl;

    public static void initEnvironment() {
        switch (ENVIRONMENT) {
            case AppConstants.ENV_DEVELOP: {//服务器域名
                sRootUrl = "http://st.3dgogo.com:8080";
                break;
            }
            case AppConstants.ENV_BETA: {//situServices
                sRootUrl = "http://39.104.70.59:80";
//                sRootUrl = "http://120.7.190.81:8443/situ";
                break;
            }
            case AppConstants.ENV_RELEASE: {//内网
                sRootUrl = "http://192.168.1.107:8080";
                break;

            }
            case AppConstants.ENV_TMP:
                sRootUrl = "http://118.24.2.164:8089";
                break;
        }
    }
}


