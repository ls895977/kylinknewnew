package com.maoyongxin.myapplication.http.request;

import android.content.Context;

import com.maoyongxin.myapplication.http.ApiMgr;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.response.RequestListResponse;

/**
 * Created by maoyongxin on 2017/12/1.
 */

public class ReqGetRequestList extends Req {
    public static void getList(Context context, String httpTag, String userId,
                                  final EntityCallBack<RequestListResponse> callBack) {
        request(context,getCommonReqBuilder(ApiMgr.GET_REQUESTLIST, httpTag)
                .addBody("userId", userId)
                .build(), new EntityCallBack<RequestListResponse>() {
            @Override
            public Class<RequestListResponse> getEntityClass() {
                return RequestListResponse.class;
            }

            @Override
            public void onSuccess(RequestListResponse resp) {
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
