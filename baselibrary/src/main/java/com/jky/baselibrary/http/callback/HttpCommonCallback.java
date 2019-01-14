package com.jky.baselibrary.http.callback;

import com.jky.baselibrary.http.response.BaseResponse;

public interface HttpCommonCallback<T extends BaseResponse> {

    void onSuccess(T resp);

    void onFailure(Throwable e);

    void onCancelled(Throwable e);

    void onFinished();
}
