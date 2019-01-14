package com.maoyongxin.myapplication.entity;

import com.maoyongxin.myapplication.myapp.AppConfig;

import java.util.List;

/**
 * Created by maoyongxin on 2018/1/9.
 */

public class NearByUserInfo {

    private List<Userlist> userlist;

    public List<Userlist> getUserlist() {
        return userlist;
    }

    public void setUserlist(List<Userlist> userlist) {
        this.userlist = userlist;
    }

    public static class Userlist {
        /**
         * userId : 10031
         * userName : 慢慢
         * longitude : 106.64802
         * latitude : 26.612027
         * token : gULdQVesK42Ot/yAZOE/iMJsINgl58oCdAcirHOpvj7AXdZY1nFd9rOnrY1+HOtGpRyIw9Y/1zoyzc7QtKVwzA==
         * note : 123456789
         * headImg : 34d5cab4285a4e0bbc4dbb9a28cf1ae6.jpg
         * userPhone : null
         * sex : 0
         * brithday : null
         * vipNum : 0
         * isPublish : null
         * isLogin : null
         * acceptInfo : null
         * isPublicInfo : null
         */

        private int userId;
        private String userName;
        private double longitude;
        private double latitude;
        private String token;
        private String note;
        private String headImg;
        private Object userPhone;
        private int sex;
        private Object brithday;
        private int vipNum;
        private Object isPublish;
        private Object isLogin;
        private Object acceptInfo;
        private Object isPublicInfo;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
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
            if(headImg==null||headImg.equals("")){
                return "";
            }else{
                return AppConfig.sRootUrl+"/logincontroller/getHeadImg/"+headImg;
            }
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public Object getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(Object userPhone) {
            this.userPhone = userPhone;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public Object getBrithday() {
            return brithday;
        }

        public void setBrithday(Object brithday) {
            this.brithday = brithday;
        }

        public int getVipNum() {
            return vipNum;
        }

        public void setVipNum(int vipNum) {
            this.vipNum = vipNum;
        }

        public Object getIsPublish() {
            return isPublish;
        }

        public void setIsPublish(Object isPublish) {
            this.isPublish = isPublish;
        }

        public Object getIsLogin() {
            return isLogin;
        }

        public void setIsLogin(Object isLogin) {
            this.isLogin = isLogin;
        }

        public Object getAcceptInfo() {
            return acceptInfo;
        }

        public void setAcceptInfo(Object acceptInfo) {
            this.acceptInfo = acceptInfo;
        }

        public Object getIsPublicInfo() {
            return isPublicInfo;
        }

        public void setIsPublicInfo(Object isPublicInfo) {
            this.isPublicInfo = isPublicInfo;
        }
    }
}
