package com.maoyongxin.myapplication.http.request;

import android.content.Context;

import com.maoyongxin.myapplication.http.ApiMgr;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.response.StringObjResponse;

/**
 * Created by maoyongxin on 2017/12/1.
 */

public class ReqPay extends Req {
    public static void getAliMessage(Context context, String httpTag,String userId,String vipId,
                                      final EntityCallBack<StringObjResponse> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.GET_PAYMSG, httpTag)
                .addBody("userId", userId)
                .addBody("vipId", vipId)
                .build(), new EntityCallBack<StringObjResponse>() {
            @Override
            public Class<StringObjResponse> getEntityClass() {
                return StringObjResponse.class;
            }

            @Override
            public void onSuccess(StringObjResponse resp) {
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
