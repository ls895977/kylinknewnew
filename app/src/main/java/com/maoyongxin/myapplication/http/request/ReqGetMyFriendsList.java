package com.maoyongxin.myapplication.http.request;

import android.content.Context;

import com.maoyongxin.myapplication.http.ApiMgr;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.response.FriendsResponse;

/**
 * Created by maoyongxin on 2017/12/1.
 */

public class ReqGetMyFriendsList extends Req {
    public static void getFriends(Context context, String httpTag, String userId,
                                  final EntityCallBack<FriendsResponse> callBack) {
        request(context,getCommonReqBuilder(ApiMgr.GET_MYFRIENDS, httpTag)
                .addBody("userId", userId)
                .build(), new EntityCallBack<FriendsResponse>() {
            @Override
            public Class<FriendsResponse> getEntityClass() {
                return FriendsResponse.class;
            }

            @Override
            public void onSuccess(FriendsResponse resp) {
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
