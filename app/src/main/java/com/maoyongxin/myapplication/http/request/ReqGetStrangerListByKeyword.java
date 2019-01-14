package com.maoyongxin.myapplication.http.request;

import android.content.Context;

import com.maoyongxin.myapplication.http.ApiMgr;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.response.StrangerResponse;

/**
 * Created by maoyongxin on 2017/12/1.
 */

public class ReqGetStrangerListByKeyword extends Req {
    public static void getStranger(Context context, String httpTag, String userId, String Keywords,
                               final EntityCallBack<StrangerResponse> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.GET_STRANGER_BY_KETWORD, httpTag)
                .addBody("userId", userId)
                .addBody("Keywords", Keywords)
                .build(), new EntityCallBack<StrangerResponse>() {
            @Override
            public Class<StrangerResponse> getEntityClass() {
                return StrangerResponse.class;
            }

            @Override
            public void onSuccess(StrangerResponse resp) {
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
