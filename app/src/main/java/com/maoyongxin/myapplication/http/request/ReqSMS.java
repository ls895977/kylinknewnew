package com.maoyongxin.myapplication.http.request;

import android.content.Context;

import com.maoyongxin.myapplication.http.ApiMgr;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.response.SMSResponse;

/**
 * Created by maoyongxin on 2017/12/1.
 */

public class ReqSMS extends Req {
    public static void getSmS(Context context, String httpTag, String userPhone,
                                      final EntityCallBack<SMSResponse> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.GET_VERCODE, httpTag)
                .addBody("userPhone", userPhone)
                .build(), new EntityCallBack<SMSResponse>() {
            @Override
            public Class<SMSResponse> getEntityClass() {
                return SMSResponse.class;
            }

            @Override
            public void onSuccess(SMSResponse resp) {
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
