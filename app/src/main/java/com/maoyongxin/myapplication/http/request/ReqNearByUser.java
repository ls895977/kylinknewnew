package com.maoyongxin.myapplication.http.request;

import android.content.Context;

import com.maoyongxin.myapplication.http.ApiMgr;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.response.NearbyUserResponse;

/**
 * Created by maoyongxin on 2017/12/1.
 */

public class ReqNearByUser extends Req {
    public static void getUsers(Context context, String httpTag, String userId, String latitude, String longitude,
                                final EntityCallBack<NearbyUserResponse> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.GET_NEARBYLIST, httpTag)
                .addBody("userId", userId)
                .addBody("latitude", latitude)
                .addBody("longitude", longitude)
                .build(), new EntityCallBack<NearbyUserResponse>() {
            @Override
            public Class<NearbyUserResponse> getEntityClass() {
                return NearbyUserResponse.class;
            }

            @Override
            public void onSuccess(NearbyUserResponse resp) {
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
