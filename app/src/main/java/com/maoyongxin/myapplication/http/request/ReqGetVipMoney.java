package com.maoyongxin.myapplication.http.request;

import android.content.Context;

import com.maoyongxin.myapplication.http.ApiMgr;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.response.BaseResp;
import com.maoyongxin.myapplication.http.response.VipMoneyListResponse;

/**
 * Created by maoyongxin on 2017/12/1.
 */

public class ReqGetVipMoney extends Req {
    public static void getMoney(Context context, String httpTag,
                                  final EntityCallBack<VipMoneyListResponse> callBack) {
        request(context,getCommonReqBuilder(ApiMgr.GET_VIPLIST, httpTag)
                .build(), new EntityCallBack<VipMoneyListResponse>() {
            @Override
            public Class<VipMoneyListResponse> getEntityClass() {
                return VipMoneyListResponse.class;
            }

            @Override
            public void onSuccess(VipMoneyListResponse resp) {
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
