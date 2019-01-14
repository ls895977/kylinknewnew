package com.maoyongxin.myapplication.http.request;

import android.content.Context;

import com.maoyongxin.myapplication.http.ApiMgr;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.response.InterestResponse;

/**
 * Created by maoyongxin on 2017/12/1.
 */

public class ReqFindInterestList extends Req {
    public static void findList(Context context, String httpTag, String parentId,
                                final EntityCallBack<InterestResponse> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.FIND_INTERESTING_LIST, httpTag)
                .addBody("parentId", parentId)
                .build(), new EntityCallBack<InterestResponse>() {
            @Override
            public Class<InterestResponse> getEntityClass() {
                return InterestResponse.class;
            }

            @Override
            public void onSuccess(InterestResponse resp) {
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
