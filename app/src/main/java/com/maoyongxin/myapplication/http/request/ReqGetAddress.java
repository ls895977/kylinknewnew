package com.maoyongxin.myapplication.http.request;

import android.content.Context;

import com.maoyongxin.myapplication.http.ApiMgr;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.response.AddressResponse;

/**
 * Created by maoyongxin on 2017/12/1.
 */

public class ReqGetAddress extends Req {
    public static void getAddress(Context context, String httpTag, String parentId,
                                  final EntityCallBack<AddressResponse> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.GET_ADDRESS, httpTag)
                .addBody("parentId", parentId)
                .build(), new EntityCallBack<AddressResponse>() {
            @Override
            public Class<AddressResponse> getEntityClass() {
                return AddressResponse.class;
            }

            @Override
            public void onSuccess(AddressResponse resp) {
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
