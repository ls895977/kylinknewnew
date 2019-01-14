package com.maoyongxin.myapplication.http.request;

import android.content.Context;
import android.util.Log;

import com.maoyongxin.myapplication.http.ApiMgr;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.response.BaiduPush;
import com.maoyongxin.myapplication.http.response.BaiduPushResponse;
import com.maoyongxin.myapplication.http.response.ChangeUserInfoResponse;
import com.maoyongxin.myapplication.http.response.LoginResponse;

/**
 * Created by maoyongxin on 2017/12/1.
 */

public class ReqUserServer extends Req {
    public static void doLogin(Context context, String httpTag, String userId, String password,
                               final EntityCallBack<LoginResponse> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.DO_LOGIN, httpTag)
                .addBody("userId", userId)
                .addBody("password", password)
                .build(), new EntityCallBack<LoginResponse>() {
            @Override
            public Class<LoginResponse> getEntityClass() {
                return LoginResponse.class;
            }

            @Override
            public void onSuccess(LoginResponse resp) {
                callBack.onSuccess(resp);
            }

            @Override
            public void onFailure(Throwable e) {
                callBack.onFailure(e);
            }

            @Override
            public void onCancelled(Throwable e) {
                callBack.onCancelled(e);
            }

            @Override
            public void onFinished() {
                callBack.onFinished();
            }
        });
    }
//http://118.24.2.164:8089/usercontroller/updateUserInfo
    public static void changeUserInfo(Context context, String httpTag, String userId, String note, String sex, String birthday, String userName,
                                      final EntityCallBack<ChangeUserInfoResponse> callBack) {
        Log.e("aa","------------changeUserInfo----"+ApiMgr.getApiUrl(ApiMgr.CHANGE_USERINFO));
        Log.e("aa","------------changeUserInfo----userId==="+userId+"+/n"
                +"---------note-==="+note+"/n"+"----------sex==="+sex+"---------brithday-"+birthday+"=======userName==="+userName);
        request(context, getCommonReqBuilder(ApiMgr.CHANGE_USERINFO, httpTag)
                .addBody("userId", userId)
                .addBody("note", note)
                .addBody("sex", sex)
                .addBody("brithday", birthday)
                .addBody("userName", userName)
                .build(), new EntityCallBack<ChangeUserInfoResponse>() {
            @Override
            public Class<ChangeUserInfoResponse> getEntityClass() {
                return ChangeUserInfoResponse.class;
            }

            @Override
            public void onSuccess(ChangeUserInfoResponse resp) {
                callBack.onSuccess(resp);
            }

            @Override
            public void onFailure(Throwable e) {
                callBack.onFailure(e);
            }

            @Override
            public void onCancelled(Throwable e) {
                callBack.onCancelled(e);

            }

            @Override
            public void onFinished() {
                callBack.onFinished();
            }
        });
    }

    public static void uploadBaiduPushInfo(Context context, String httpTag, long userId,
                                           String channelId, int deviceType,
                                           final EntityCallBack<BaiduPushResponse> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.getApiUrl(ApiMgr.POST_CHANNEL_ID)
                        + "?userId=" + userId
                        + "&channelId=" + channelId
                        + "&deviceType=" + deviceType,
                httpTag)
                .build(), new EntityCallBack<BaiduPushResponse>() {
            @Override
            public Class<BaiduPushResponse> getEntityClass() {
                return BaiduPushResponse.class;
            }

            @Override
            public void onSuccess(BaiduPushResponse resp) {
                callBack.onSuccess(resp);

            }

            @Override
            public void onFailure(Throwable e) {
                callBack.onFailure(e);

            }

            @Override
            public void onCancelled(Throwable e) {
                callBack.onCancelled(e);

            }

            @Override
            public void onFinished() {
                callBack.onFinished();

            }
        });

    }
}
