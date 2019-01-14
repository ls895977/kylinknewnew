package com.maoyongxin.myapplication.http.callback;


import com.maoyongxin.myapplication.http.response.BaseResp;

/**
 * Created by dingke on 2017/8/3.
 */

public interface EntityCallBack<T extends BaseResp> {

    Class<T> getEntityClass();

    void onSuccess(T resp);

    void onFailure(Throwable e);

    void onCancelled(Throwable e);

    void onFinished();
}
