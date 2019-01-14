package com.maoyongxin.myapplication.entity;

import java.util.List;

/**
 * Created by maoyongxin on 2018/1/5.
 */

public class CommunityAddRequestInfo {

    private List<RequestList> requestList;

    public List<RequestList> getRequestList() {
        return requestList;
    }

    public void setRequestList(List<RequestList> requestList) {
        this.requestList = requestList;
    }

    public static class RequestList {
        /**
         * id : 11
         * userId : 10035
         * communityId : 7
         * note : 测试加入
         * addType : 1
         */

        private int id;
        private int userId;
        private int communityId;
        private String note;
        private int addType;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getCommunityId() {
            return communityId;
        }

        public void setCommunityId(int communityId) {
            this.communityId = communityId;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public int getAddType() {
            return addType;
        }

        public void setAddType(int addType) {
            this.addType = addType;
        }
    }
}
