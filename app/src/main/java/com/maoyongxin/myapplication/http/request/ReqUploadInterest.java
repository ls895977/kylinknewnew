package com.maoyongxin.myapplication.http.request;

import android.content.Context;

import com.maoyongxin.myapplication.http.ApiMgr;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.response.UploadInterestResponse;

/**
 * Created by maoyongxin on 2017/12/1.
 */

public class ReqUploadInterest extends Req {
    public static void setInterest(Context context, String httpTag, String userId, String interestId,
                                   final EntityCallBack<UploadInterestResponse> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.UPLOAD_INTEREST, httpTag)
                .addBody("userId", userId)
                .addBody("interestId", interestId)
                .build(), new EntityCallBack<UploadInterestResponse>() {
            @Override
            public Class<UploadInterestResponse> getEntityClass() {
                return UploadInterestResponse.class;
            }

            @Override
            public void onSuccess(UploadInterestResponse resp) {
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
