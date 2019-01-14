package com.maoyongxin.myapplication.entity;

import com.maoyongxin.myapplication.myapp.AppConfig;

import java.util.List;

/**
 * Created by maoyongxin on 2017/12/8.
 */

public class StrangerInfo {

    private List<UserList> userList;

    public List<UserList> getUserList() {
        return userList;
    }

    public void setUserList(List<UserList> userList) {
        this.userList = userList;
    }

    public static class UserList {
        /**
         * hobbyList : [{"interestId":27,"interestName":"测试分类A3丙","parentId":24,"level":3,"creatTime":1512526479000},{"interestId":553,"interestName":"测试分类F5庚","parentId":546,"level":3,"creatTime":1512526579000}]
         * user : {"userId":10034,"userName":"mike","longitude":106.647753,"latitude":26.612004,"token":"OtQ7ZFPjaj2GD9V/myTg2tpSRhamXgLNKSGtRUbLwkBa6V8cMezoFK4HOnNXhhrMYohLSd1BAIstWk3d/e843w==","note":"mike","headImg":"","userPhone":"","sex":0,"brithday":null,"vipNum":0}
         */

        private User user;
        private List<HobbyList> hobbyList;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public List<HobbyList> getHobbyList() {
            return hobbyList;
        }

        public void setHobbyList(List<HobbyList> hobbyList) {
            this.hobbyList = hobbyList;
        }

        public static class User {
            /**
             * userId : 10034
             * userName : mike
             * longitude : 106.647753
             * latitude : 26.612004
             * token : OtQ7ZFPjaj2GD9V/myTg2tpSRhamXgLNKSGtRUbLwkBa6V8cMezoFK4HOnNXhhrMYohLSd1BAIstWk3d/e843w==
             * note : mike
             * headImg : 
             * userPhone : 
             * sex : 0
             * brithday : null
             * vipNum : 0
             */

            private int userId;
            private String userName;
            private double longitude;
            private double latitude;
            private String token;
            private String note;
            private String headImg;
            private String userPhone;
            private int sex;
            private String brithday;
            private int vipNum;

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

            public String getUserPhone() {
                return userPhone;
            }

            public void setUserPhone(String userPhone) {
                this.userPhone = userPhone;
            }

            public int getSex() {
                return sex;
            }

            public void setSex(int sex) {
                this.sex = sex;
            }

            public String getBrithday() {
                return brithday;
            }

            public void setBrithday(String brithday) {
                this.brithday = brithday;
            }

            public int getVipNum() {
                return vipNum;
            }

            public void setVipNum(int vipNum) {
                this.vipNum = vipNum;
            }
        }

        public static class HobbyList {
            /**
             * interestId : 27
             * interestName : 测试分类A3丙
             * parentId : 24
             * level : 3
             * creatTime : 1512526479000
             */

            private int interestId;
            private String interestName;
            private int parentId;
            private int level;
            private long creatTime;

            public int getInterestId() {
                return interestId;
            }

            public void setInterestId(int interestId) {
                this.interestId = interestId;
            }

            public String getInterestName() {
                return interestName;
            }

            public void setInterestName(String interestName) {
                this.interestName = interestName;
            }

            public int getParentId() {
                return parentId;
            }

            public void setParentId(int parentId) {
                this.parentId = parentId;
            }

            public int getLevel() {
                return level;
            }

            public void setLevel(int level) {
                this.level = level;
            }

            public long getCreatTime() {
                return creatTime;
            }

            public void setCreatTime(long creatTime) {
                this.creatTime = creatTime;
            }
        }
    }
}
