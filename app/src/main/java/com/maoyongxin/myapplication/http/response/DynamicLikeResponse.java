package com.maoyongxin.myapplication.http.response;

public class DynamicLikeResponse extends BaseResp {
    private DynamicLikeInfo dynamicLike;

    public DynamicLikeInfo getDynamicLike() {
        return dynamicLike;
    }

    public void setDynamicLike(DynamicLikeInfo dynamicLike) {
        this.dynamicLike = dynamicLike;
    }

    @Override
    public String toString() {
        return "DynamicLikeResponse{" +
                "dynamicLike=" + dynamicLike +
                '}';
    }
}
