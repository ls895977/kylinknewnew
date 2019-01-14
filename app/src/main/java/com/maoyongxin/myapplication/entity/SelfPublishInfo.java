package com.maoyongxin.myapplication.entity;

import java.util.List;

/**
 * Created by maoyongxin on 2017/12/15.
 */

public class SelfPublishInfo {

    private List<NoticeList> noticeList;

    public List<NoticeList> getNoticeList() {
        return noticeList;
    }

    public void setNoticeList(List<NoticeList> noticeList) {
        this.noticeList = noticeList;
    }

    public static class NoticeList {
        /**
         * thingName : 衣柜
         * notice : {"noticeId":4,"noticeTitle":"话费积分","noticeType":2,"userId":10032,"createTime":null,"areaCode":null,"content":"几分几分","businessType":2,"thingTypeId":3}
         */

        private String thingName;
        private Notice notice;

        public String getThingName() {
            return thingName;
        }

        public void setThingName(String thingName) {
            this.thingName = thingName;
        }

        public Notice getNotice() {
            return notice;
        }

        public void setNotice(Notice notice) {
            this.notice = notice;
        }

        public static class Notice {
            /**
             * noticeId : 4
             * noticeTitle : 话费积分
             * noticeType : 2
             * userId : 10032
             * createTime : null
             * areaCode : null
             * content : 几分几分
             * businessType : 2
             * thingTypeId : 3
             */

            private String noticeId;
            private String noticeTitle;
            private String noticeType;
            private String userId;
            private long createTime;
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

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
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
    }
}
