package com.maoyongxin.myapplication.entity;

import java.util.List;

/**
 * Created by maoyongxin on 2018/1/3.
 */

public class CommunityRequestInfo {

    private List<CommunityRequestList> communityRequestList;

    public List<CommunityRequestList> getCommunityRequestList() {
        return communityRequestList;
    }

    public void setCommunityRequestList(List<CommunityRequestList> communityRequestList) {
        this.communityRequestList = communityRequestList;
    }

    public static class CommunityRequestList {
        /**
         * requestId : 4
         * communityName : 10032的社区
         * communityNote : 这是10032的测试社区
         * areaCode : 520115
         * address : 贵州省贵阳市观山湖区德福中心
         * addressName : 德福中心
         * longitude : 26.61219
         * latitude : 106.64854
         * creatTime : 1514948959000
         * userId : 10032
         * note : null
         * requestStart : null
         */

        private int requestId;
        private String communityName;
        private String communityNote;
        private int areaCode;
        private String address;
        private String addressName;
        private double longitude;
        private double latitude;
        private long creatTime;
        private int userId;
        private String note;
        private String requestStart;

        public int getRequestId() {
            return requestId;
        }

        public void setRequestId(int requestId) {
            this.requestId = requestId;
        }

        public String getCommunityName() {
            return communityName;
        }

        public void setCommunityName(String communityName) {
            this.communityName = communityName;
        }

        public String getCommunityNote() {
            return communityNote;
        }

        public void setCommunityNote(String communityNote) {
            this.communityNote = communityNote;
        }

        public int getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(int areaCode) {
            this.areaCode = areaCode;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAddressName() {
            return addressName;
        }

        public void setAddressName(String addressName) {
            this.addressName = addressName;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public long getCreatTime() {
            return creatTime;
        }

        public void setCreatTime(long creatTime) {
            this.creatTime = creatTime;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getRequestStart() {
            return requestStart;
        }

        public void setRequestStart(String requestStart) {
            this.requestStart = requestStart;
        }
    }
}
