package com.maoyongxin.myapplication.http.request;

import android.content.Context;

import com.maoyongxin.myapplication.http.ApiMgr;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.response.FriendsResponse;
import com.maoyongxin.myapplication.http.response.NewsZPResponse;

/**
 * Created by maoyongxin on 2017/12/1.
 */

public class ReqNewsZanPlun extends Req {
    public static void getnewsZP(Context context, String httpTag, String newsId,
                                  final EntityCallBack<NewsZPResponse> callBack) {
        request(context,getCommonReqBuilder("http://st.3dgogo.com/index/news/get_read_post_num_api.html", httpTag)
                .addBody("id", newsId)
                .build(), new EntityCallBack<NewsZPResponse>() {
            @Override
            public Class<NewsZPResponse> getEntityClass() {
                return NewsZPResponse.class;
            }

            @Override
            public void onSuccess(NewsZPResponse resp) {
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
