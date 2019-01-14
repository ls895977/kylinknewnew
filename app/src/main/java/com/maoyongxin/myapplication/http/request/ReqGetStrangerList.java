package com.maoyongxin.myapplication.http.request;

import android.content.Context;

import com.maoyongxin.myapplication.http.ApiMgr;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.response.StrangerResponse;

/**
 * Created by maoyongxin on 2017/12/1.
 */

public class ReqGetStrangerList extends Req {
    public static void getList(Context context, String httpTag, String userId, String page,
                               final EntityCallBack<StrangerResponse> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.GET_STRANGER_LIST, httpTag)
                .addBody("userId", userId)
                .addBody("page", page)
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
