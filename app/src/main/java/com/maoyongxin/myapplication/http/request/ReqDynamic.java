package com.maoyongxin.myapplication.http.request;

import android.content.Context;
import android.util.Log;

import com.maoyongxin.myapplication.http.ApiMgr;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.response.DynamicCommentResponse;
import com.maoyongxin.myapplication.http.response.DynamicLikeResponse;

public class ReqDynamic extends Req {

    public static void likeDynamic(Context context, String httpTag, String uid, String dynamicId,
                                   final EntityCallBack<DynamicLikeResponse> callBack) {
        request(context, getCommonReqBuilder(
                ApiMgr.getApiUrl(ApiMgr.DYNAMIC_LIKE_ID) + "?uid=" + uid + "&dynamicId=" + dynamicId, httpTag)

                .build(), new EntityCallBack<DynamicLikeResponse>() {
            @Override
            public Class<DynamicLikeResponse> getEntityClass() {
                return DynamicLikeResponse.class;
            }

            @Override
            public void onSuccess(DynamicLikeResponse resp) {
                Log.d("---", "success");
                if (callBack != null) {
                    callBack.onSuccess(resp);
                }

            }

            @Override
            public void onFailure(Throwable e) {
                if (callBack != null) {
                    callBack.onFailure(e);
                }
            }

            @Override
            public void onCancelled(Throwable e) {

            }

            @Override
            public void onFinished() {
                if (callBack != null) {
                    callBack.onFinished();
                }

            }
        });
    }

    public static void commentDynamic(Context context, String httpTag, String uid, String dynamicId, String content,
                                      final EntityCallBack<DynamicCommentResponse> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.getApiUrl(ApiMgr.DYNAMIC_COMMENT_ID), httpTag)
                .addBody("uid", uid)
                .addBody("dynamicId", dynamicId)
                .addBody("content", content)
                .build(), new EntityCallBack<DynamicCommentResponse>() {

            @Override
            public Class<DynamicCommentResponse> getEntityClass() {
                return DynamicCommentResponse.class;
            }

            @Override
            public void onSuccess(DynamicCommentResponse resp) {
                if (callBack != null) {
                    callBack.onSuccess(resp);
                }
            }

            @Override
            public void onFailure(Throwable e) {
                if (callBack != null) {
                    callBack.onFailure(e);
                }
            }

            @Override
            public void onCancelled(Throwable e) {

            }

            @Override
            public void onFinished() {
                if (callBack != null) {
                    callBack.onFinished();
                }
            }

        });

    }
}
