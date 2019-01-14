package com.maoyongxin.myapplication.entity;

import com.maoyongxin.myapplication.myapp.AppConfig;

import java.util.List;

/**
 * Created by maoyongxin on 2017/12/6.
 */

public class FriendsInfo {

    private List<FriendList> friendList;

    public List<FriendList> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<FriendList> friendList) {
        this.friendList = friendList;
    }

    public static class FriendList {
        /**
         * userId : 10032
         * userName : 大慈寺好
         * longitude : null
         * latitude : null
         * token : eL3Mekzu1YjfCpYafNuXXNpSRhamXgLNKSGtRUbLwkBa6V8cMezoFGiCPy+oR9CHL08VBc66BbKoPmgF3TeMzQ==
         * note : 15465856
         * headImg : null
         * userPhone : null
         * sex : null
         * brithday : null
         * vipNum : null
         * userNoteName : 大慈寺好
         */
        private String sortLetters;  //显示数据拼音的首字母
        private String userId;
        private String userName;
        private String longitude;
        private String latitude;
        private String token;
        private String note;
        private String headImg;
        private String userPhone;
        private String sex;
        private String brithday;
        private String vipNum;
        private String userNoteName;

        public String getSortLetters() {
            return sortLetters;
        }

        public void setSortLetters(String sortLetters) {
            this.sortLetters = sortLetters;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public Object getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public Object getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getBrithday() {
            return brithday;
        }

        public void setBrithday(String brithday) {
            this.brithday = brithday;
        }

        public String getVipNum() {
            return vipNum;
        }

        public void setVipNum(String vipNum) {
            this.vipNum = vipNum;
        }

        public String getUserNoteName() {
            return userNoteName;
        }

        public void setUserNoteName(String userNoteName) {
            this.userNoteName = userNoteName;
        }
    }
}
