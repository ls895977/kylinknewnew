package com.maoyongxin.myapplication.http.request;

import android.content.Context;

import com.maoyongxin.myapplication.http.ApiMgr;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.response.BannerResponse;

/**
 * Created by maoyongxin on 2017/12/1.
 */

public class ReqGetBannerList extends Req {
    public static void getBanner(Context context, String httpTag,
                                 final EntityCallBack<BannerResponse> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.GET_BANNERLIST, httpTag)
                .build(), new EntityCallBack<BannerResponse>() {
            @Override
            public Class<BannerResponse> getEntityClass() {
                return BannerResponse.class;
            }

            @Override
            public void onSuccess(BannerResponse resp) {
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
