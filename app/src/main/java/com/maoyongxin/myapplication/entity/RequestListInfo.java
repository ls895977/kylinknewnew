package com.maoyongxin.myapplication.entity;

import java.util.List;

/**
 * Created by maoyongxin on 2017/12/5.
 */

public class RequestListInfo {

    private List<RequestList> requestList;

    public List<RequestList> getRequestList() {
        return requestList;
    }

    public void setRequestList(List<RequestList> requestList) {
        this.requestList = requestList;
    }

    public static class RequestList {
        /**
         * requestId : 1
         * userId : 10033
         * requestUserId : 10030
         * note : 加我
         * requestDate : 1512462512000
         * requestState : 1
         */

        private String requestId;
        private String userId;
        private String requestUserId;
        private String note;
        private String requestDate;
        private String requestState;

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getRequestUserId() {
            return requestUserId;
        }

        public void setRequestUserId(String requestUserId) {
            this.requestUserId = requestUserId;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getRequestDate() {
            return requestDate;
        }

        public void setRequestDate(String requestDate) {
            this.requestDate = requestDate;
        }

        public String getRequestState() {
            return requestState;
        }

        public void setRequestState(String requestState) {
            this.requestState = requestState;
        }
    }
}
