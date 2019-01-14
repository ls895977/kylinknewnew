package com.maoyongxin.myapplication.http.request;

import android.content.Context;

import com.maoyongxin.myapplication.http.ApiMgr;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.response.ProTypeResponse;

/**
 * Created by maoyongxin on 2017/12/1.
 */

public class ReqGetProType extends Req {
    public static void getTypeList(Context context, String httpTag, String parentId, String areaCode,
                                  final EntityCallBack<ProTypeResponse> callBack) {
        request(context,getCommonReqBuilder(ApiMgr.GET_PROTYPE, httpTag)
                .addBody("parentId", parentId)
                .addBody("areaCode", areaCode)
                .build(), new EntityCallBack<ProTypeResponse>() {
            @Override
            public Class<ProTypeResponse> getEntityClass() {
                return ProTypeResponse.class;
            }

            @Override
            public void onSuccess(ProTypeResponse resp) {
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
