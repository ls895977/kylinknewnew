package com.maoyongxin.myapplication.http.request;

import android.content.Context;

import com.maoyongxin.myapplication.http.ApiMgr;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.response.BaseResp;

/**
 * Created by maoyongxin on 2017/12/1.
 */

public class ReqDeleteInterestRequest extends Req {
    public static void dealRequest(Context context, String httpTag, String userId, String interestId,
                                  final EntityCallBack<BaseResp> callBack) {
        request(context,getCommonReqBuilder(ApiMgr.DELETE_MYINTEREST, httpTag)
                .addBody("userId", userId)
                .addBody("interestId", interestId)
                .build(), new EntityCallBack<BaseResp>() {
            @Override
            public Class<BaseResp> getEntityClass() {
                return BaseResp.class;
            }

            @Override
            public void onSuccess(BaseResp resp) {
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
