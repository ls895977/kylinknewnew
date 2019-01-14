package com.maoyongxin.myapplication.http.request;

import android.content.Context;

import com.maoyongxin.myapplication.http.ApiMgr;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.response.RegisterResponse;

/**
 * Created by maoyongxin on 2017/12/1.
 */

public class ReqRegister extends Req {
    public static void doRegister(Context context, String httpTag, String userName,String userPhone,String password,
                                         final EntityCallBack<RegisterResponse> callBack) {
        request(context,getCommonReqBuilder(ApiMgr.DO_REGISTER, httpTag)
                .addBody("userName", userName)
                .addBody("userPhone", userPhone)
                .addBody("note", "")
                .addBody("password", password)
                .build(), new EntityCallBack<RegisterResponse>() {
            @Override
            public Class<RegisterResponse> getEntityClass() {
                return RegisterResponse.class;
            }

            @Override
            public void onSuccess(RegisterResponse resp) {
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
