package com.maoyongxin.myapplication.http.request;

import android.content.Context;

import com.maoyongxin.myapplication.http.ApiMgr;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.response.SelfPublishResponse;

/**
 * Created by maoyongxin on 2017/12/1.
 */

public class ReqGetPublishListByThing extends Req {
    public static void getPublishList(Context context, String httpTag, String areaCode,String thingTypeId,String date, String page,
                                    final EntityCallBack<SelfPublishResponse> callBack) {
        request(context,getCommonReqBuilder(ApiMgr.FIND_NOTICE_BYTHING, httpTag)
                .addBody("date", date)
                .addBody("areaCode", areaCode)
                .addBody("page", page)
                .addBody("thingTypeId", thingTypeId)
                .build(), new EntityCallBack<SelfPublishResponse>() {
            @Override
            public Class<SelfPublishResponse> getEntityClass() {
                return SelfPublishResponse.class;
            }

            @Override
            public void onSuccess(SelfPublishResponse resp) {
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
