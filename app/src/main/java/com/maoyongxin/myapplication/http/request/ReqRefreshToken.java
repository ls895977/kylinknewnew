package com.maoyongxin.myapplication.http.request;

import android.content.Context;

import com.maoyongxin.myapplication.http.ApiMgr;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.response.RefreshTokenResponse;

/**
 * Created by maoyongxin on 2017/12/1.
 */

public class ReqRefreshToken extends Req {
    public static void refreshToken(Context context, String httpTag, String userId,
                                         final EntityCallBack<RefreshTokenResponse> callBack) {
        request(context,getCommonReqBuilder(ApiMgr.REFRESH_TOKEN, httpTag)
                .addBody("userId", userId)
                .build(), new EntityCallBack<RefreshTokenResponse>() {
            @Override
            public Class<RefreshTokenResponse> getEntityClass() {
                return RefreshTokenResponse.class;
            }

            @Override
            public void onSuccess(RefreshTokenResponse resp) {
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
