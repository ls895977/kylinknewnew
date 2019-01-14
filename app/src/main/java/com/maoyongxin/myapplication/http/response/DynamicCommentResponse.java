package com.maoyongxin.myapplication.http.response;

public class DynamicCommentResponse extends BaseResp {
    private DynamicComment dynamicComment;

    public DynamicComment getDynamicComment() {
        return dynamicComment;
    }

    public void setDynamicComment(DynamicComment dynamicComment) {
        this.dynamicComment = dynamicComment;
    }
}
