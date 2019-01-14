package com.maoyongxin.myapplication.http.request;

import android.content.Context;

import com.maoyongxin.myapplication.http.ApiMgr;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.response.FollowListResponse;

/**
 * Created by maoyongxin on 2017/12/1.
 */

public class ReqGetFollowList extends Req {
    public static void getList(Context context, String httpTag, String userId,
                               final EntityCallBack<FollowListResponse> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.GET_FOLLOWLIST, httpTag)
                .addBody("userId", userId)
                .build(), new EntityCallBack<FollowListResponse>() {
            @Override
            public Class<FollowListResponse> getEntityClass() {
                return FollowListResponse.class;
            }

            @Override
            public void onSuccess(FollowListResponse resp) {
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
