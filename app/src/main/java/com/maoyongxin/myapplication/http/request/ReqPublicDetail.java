package com.maoyongxin.myapplication.http.request;

import android.content.Context;

import com.maoyongxin.myapplication.http.ApiMgr;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.response.PublishDetailResponse;

/**
 * Created by maoyongxin on 2017/12/1.
 */

public class ReqPublicDetail extends Req {
    public static void getPublic(Context context, String httpTag, String userId, String noticeId,
                               final EntityCallBack<PublishDetailResponse> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.GET_PUBLIC_DETAIL, httpTag)
                .addBody("userId", userId)
                .addBody("noticeId", noticeId)
                .build(), new EntityCallBack<PublishDetailResponse>() {
            @Override
            public Class<PublishDetailResponse> getEntityClass() {
                return PublishDetailResponse.class;
            }

            @Override
            public void onSuccess(PublishDetailResponse resp) {
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
