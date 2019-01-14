package com.maoyongxin.myapplication.http.request;

import android.content.Context;

import com.maoyongxin.myapplication.http.ApiMgr;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.response.BaseResp;

/**
 * Created by maoyongxin on 2017/12/5.
 */

public class ReqUploadLocation extends Req {
    public static void uploadLocation(Context context, String httpTag, String userId, String longitude,String latitude,
                               final EntityCallBack<BaseResp> callBack) {
        request(context,getCommonReqBuilder(ApiMgr.UPLOAD_LOCATION, httpTag)
                .addBody("userId", userId)
                .addBody("longitude", longitude)
                .addBody("latitude", latitude)
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
