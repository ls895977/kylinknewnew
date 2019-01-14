package com.maoyongxin.myapplication.entity;

/**
 * Created by 鱼呈瑞 on 2018/3/24.
 */

public class hotDynamicInfo {


    /**
     * dynamicId : 110
     * createTime : 2018-05-17 02:25:19
     * dynamicContent : 俄罗斯大妈
     * userId : 10077
     * praiseNum : 16
     * treadNum : 0
     * commentNum : 1
     * is_praise_tread : 1
     */

    private String dynamicId;
    private String createTime;
    private String dynamicContent;
    private String userId;
    private String praiseNum;
    private String treadNum;
    private String commentNum;
    private String is_praise_tread;

    public String getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(String dynamicId) {
        this.dynamicId = dynamicId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDynamicContent() {
        return dynamicContent;
    }

    public void setDynamicContent(String dynamicContent) {
        this.dynamicContent = dynamicContent;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(String praiseNum) {
        this.praiseNum = praiseNum;
    }

    public String getTreadNum() {
        return treadNum;
    }

    public void setTreadNum(String treadNum) {
        this.treadNum = treadNum;
    }

    public String getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
    }

    public String getIs_praise_tread() {
        return is_praise_tread;
    }

    public void setIs_praise_tread(String is_praise_tread) {
        this.is_praise_tread = is_praise_tread;
    }
}
