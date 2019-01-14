package com.maoyongxin.myapplication.http.request;

import android.content.Context;

import com.maoyongxin.myapplication.http.ApiMgr;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.response.UploadMyPublishResponse;

/**
 * Created by maoyongxin on 2017/12/1.
 */

public class ReqUploadPublishInfo extends Req {
    public static void uploadPublishInfo(Context context, String httpTag, String noticeTitle, String noticeType,
                                         String userId, String areacode, String content, String businessType, String thingTypeId,
                                         final EntityCallBack<UploadMyPublishResponse> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.UPLOAD_MYPUBLISH, httpTag)
                .addBody("noticeTitle", noticeTitle)
                .addBody("noticeType", noticeType)
                .addBody("userId", userId)
                .addBody("areacode", areacode)
                .addBody("content", content)
                .addBody("businessType", businessType)
                .addBody("thingTypeId", thingTypeId)
                .build(), new EntityCallBack<UploadMyPublishResponse>() {
            @Override
            public Class<UploadMyPublishResponse> getEntityClass() {
                return UploadMyPublishResponse.class;
            }

            @Override
            public void onSuccess(UploadMyPublishResponse resp) {
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
