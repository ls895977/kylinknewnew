package com.maoyongxin.myapplication.entity;

import java.util.List;

/**
 * Created by yongxin on 2017/12/18.
 */

public class PublicDetailInfo {

    /**
     * noticeDetail : {"noticeId":42,"noticeTitle":"测试测试","noticeType":2,"userId":10032,"createTime":1513599609000,"areaCode":520115,"content":"再试一次","businessType":1,"thingTypeId":3}
     * imgList : [{"imgUrl":"f9674f2ada8640e4a41e600f4c3f62ce.jpg"},{"imgUrl":"f70bd7c278bd4546b174b392b9e96d93.jpg"},{"imgUrl":"efeac7714aae4b34a92a1831f5cea211.jpg"}]
     */

    private NoticeDetail noticeDetail;
    private List<ImgList> imgList;

    public NoticeDetail getNoticeDetail() {
        return noticeDetail;
    }

    public void setNoticeDetail(NoticeDetail noticeDetail) {
        this.noticeDetail = noticeDetail;
    }

    public List<ImgList> getImgList() {
        return imgList;
    }

    public void setImgList(List<ImgList> imgList) {
        this.imgList = imgList;
    }

    public static class NoticeDetail {
        /**
         * noticeId : 42
         * noticeTitle : 测试测试
         * noticeType : 2
         * userId : 10032
         * createTime : 1513599609000
         * areaCode : 520115
         * content : 再试一次
         * businessType : 1
         * thingTypeId : 3
         */

        private int noticeId;
        private String noticeTitle;
        private int noticeType;
        private int userId;
        private long createTime;
        private int areaCode;
        private String content;
        private int businessType;
        private int thingTypeId;

        public int getNoticeId() {
            return noticeId;
        }

        public void setNoticeId(int noticeId) {
            this.noticeId = noticeId;
        }

        public String getNoticeTitle() {
            return noticeTitle;
        }

        public void setNoticeTitle(String noticeTitle) {
            this.noticeTitle = noticeTitle;
        }

        public int getNoticeType() {
            return noticeType;
        }

        public void setNoticeType(int noticeType) {
            this.noticeType = noticeType;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public int getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(int areaCode) {
            this.areaCode = areaCode;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getBusinessType() {
            return businessType;
        }

        public void setBusinessType(int businessType) {
            this.businessType = businessType;
        }

        public int getThingTypeId() {
            return thingTypeId;
        }

        public void setThingTypeId(int thingTypeId) {
            this.thingTypeId = thingTypeId;
        }
    }

    public static class ImgList {
        /**
         * imgUrl : f9674f2ada8640e4a41e600f4c3f62ce.jpg
         */

        private String imgUrl;

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }
    }
}
