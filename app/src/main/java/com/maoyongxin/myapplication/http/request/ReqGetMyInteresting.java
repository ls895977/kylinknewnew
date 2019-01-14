package com.maoyongxin.myapplication.http.request;

import android.content.Context;

import com.maoyongxin.myapplication.http.ApiMgr;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.response.GetMyInterestResponse;

/**
 * Created by maoyongxin on 2017/12/1.
 */

public class ReqGetMyInteresting extends Req {
    public static void getInteresting(Context context, String httpTag, String userId,
                                final EntityCallBack<GetMyInterestResponse> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.GET_MYINTERESTING, httpTag)
                .addBody("userId", userId)
                .build(), new EntityCallBack<GetMyInterestResponse>() {
            @Override
            public Class<GetMyInterestResponse> getEntityClass() {
                return GetMyInterestResponse.class;
            }

            @Override
            public void onSuccess(GetMyInterestResponse resp) {
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
