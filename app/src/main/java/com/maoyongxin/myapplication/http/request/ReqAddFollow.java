package com.maoyongxin.myapplication.http.request;

import android.content.Context;

import com.maoyongxin.myapplication.http.ApiMgr;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.response.BaseResp;
import com.maoyongxin.myapplication.http.response.FollowResponse;

/**
 * Created by maoyongxin on 2017/12/1.
 */

public class ReqAddFollow extends Req {
    public static void addFollow(Context context, String httpTag, String userId, String followUserId,String note,
                                      final EntityCallBack<FollowResponse> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.DO_LIKE_ONE, httpTag)
                .addBody("userId", userId)
                .addBody("followUserId", followUserId)
                .addBody("note", note)
                .build(), new EntityCallBack<FollowResponse>() {
            @Override
            public Class<FollowResponse> getEntityClass() {
                return FollowResponse.class;
            }

            @Override
            public void onSuccess(FollowResponse resp) {
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
    public static void upDateFollow(Context context, String httpTag, String followId,String note,
                                 final EntityCallBack<FollowResponse> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.UPDATE_FOLLOW, httpTag)
                .addBody("followId", followId)
                .addBody("note", note)
                .build(), new EntityCallBack<FollowResponse>() {
            @Override
            public Class<FollowResponse> getEntityClass() {
                return FollowResponse.class;
            }

            @Override
            public void onSuccess(FollowResponse resp) {
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
    public static void deleteFollow(Context context, String httpTag, String followId,
                                    final EntityCallBack<BaseResp> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.DELETE_FOLLOW, httpTag)
                .addBody("followId", followId)
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
