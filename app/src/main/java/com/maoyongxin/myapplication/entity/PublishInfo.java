package com.maoyongxin.myapplication.entity;

import java.util.List;

/**
 * Created by maoyongxin on 2017/12/14.
 */

public class PublishInfo {

    /**
     * imgUrlList : [{"imgurl":"iodfjgldjljfkgjkd.jpg"},{"imgurl":"iodfjgldjljfkgjkd.jpg"},{"imgurl":"iodfjgldjljfkgjkd.jpg"}]
     * notice : {"noticeId":"1","noticeTitle":"sdasdad","noticeType":"1","userId":"10031","createTime":"2012.2.2","areaCode":"520115","content":"asdsdadas","businessType":"1","thingTypeId":"2"}
     */

    private Notice notice;
    private List<ImgUrlList> imgUrlList;

    public Notice getNotice() {
        return notice;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }

    public List<ImgUrlList> getImgUrlList() {
        return imgUrlList;
    }

    public void setImgUrlList(List<ImgUrlList> imgUrlList) {
        this.imgUrlList = imgUrlList;
    }

    public static class Notice {
        /**
         * noticeId : 1
         * noticeTitle : sdasdad
         * noticeType : 1
         * userId : 10031
         * createTime : 2012.2.2
         * areaCode : 520115
         * content : asdsdadas
         * businessType : 1
         * thingTypeId : 2
         */

        private String noticeId;
        private String noticeTitle;
        private String noticeType;
        private String userId;
        private String createTime;
        private String areaCode;
        private String content;
        private String businessType;
        private String thingTypeId;

        public String getNoticeId() {
            return noticeId;
        }

        public void setNoticeId(String noticeId) {
            this.noticeId = noticeId;
        }

        public String getNoticeTitle() {
            return noticeTitle;
        }

        public void setNoticeTitle(String noticeTitle) {
            this.noticeTitle = noticeTitle;
        }

        public String getNoticeType() {
            return noticeType;
        }

        public void setNoticeType(String noticeType) {
            this.noticeType = noticeType;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getBusinessType() {
            return businessType;
        }

        public void setBusinessType(String businessType) {
            this.businessType = businessType;
        }

        public String getThingTypeId() {
            return thingTypeId;
        }

        public void setThingTypeId(String thingTypeId) {
            this.thingTypeId = thingTypeId;
        }
    }

    public static class ImgUrlList {
        /**
         * imgurl : iodfjgldjljfkgjkd.jpg
         */

        private String imgurl;

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }
    }
}
