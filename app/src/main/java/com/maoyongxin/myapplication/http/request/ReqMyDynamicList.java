package com.maoyongxin.myapplication.http.request;

import android.content.Context;

import com.maoyongxin.myapplication.http.ApiMgr;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.response.BaseResp;
import com.maoyongxin.myapplication.http.response.MyDongTaiListResponse;

/**
 * Created by maoyongxin on 2017/12/1.
 */

public class ReqMyDynamicList extends Req {
    public static void getDongtaiList(Context context, String httpTag, String userId, String page,
                                      final EntityCallBack<MyDongTaiListResponse> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.GET_MY_DYNAMICLIST, httpTag)
                .addBody("userId", userId)
                .addBody("page", page)
                .build(), new EntityCallBack<MyDongTaiListResponse>() {
            @Override
            public Class<MyDongTaiListResponse> getEntityClass() {
                return MyDongTaiListResponse.class;
            }

            @Override
            public void onSuccess(MyDongTaiListResponse resp) {
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

    public static void getStrangerDynamicList(Context context, String httpTag, String userId, String page,
                                              final EntityCallBack<MyDongTaiListResponse> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.GET_STRANGER_DYNAMICLIST, httpTag)
                .addBody("userId", userId)
                .addBody("page", page)
                .build(), new EntityCallBack<MyDongTaiListResponse>() {
            @Override
            public Class<MyDongTaiListResponse> getEntityClass() {
                return MyDongTaiListResponse.class;
            }

            @Override
            public void onSuccess(MyDongTaiListResponse resp) {
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

    public static void deleteMyDynamic(Context context, String httpTag, String userId, String dynamicId,
                                       final EntityCallBack<BaseResp> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.DELETE_MY_DYNAMIC, httpTag)
                .addBody("userId", userId)
                .addBody("dynamicId", dynamicId)
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

//    public static void getDynamicById(Context context, String httpTag, String dynamicId,
//                                      final EntityCallBack<BaseResp> callBack) {
//        request(context, getCommonReqBuilder(ApiMgr.getApiUrl(ApiMgr.GET_DYNAMIC_BY_ID) + dynamicId, httpTag)
//                .build(), new EntityCallBack<> {
//        });
//
//    }
}
