package com.maoyongxin.myapplication.entity;

import com.maoyongxin.myapplication.myapp.AppConfig;

import java.util.List;

/**
 * Created by maoyongxin on 2018/1/2.
 */

public class MyCommunityPersonInfo {


    /**
     * userList : [{"userId":10031,"userName":"慢慢","longitude":106.647961,"latitude":26.611986,"token":"gULdQVesK42Ot/yAZOE/iMJsINgl58oCdAcirHOpvj7AXdZY1nFd9rOnrY1+HOtGpRyIw9Y/1zoyzc7QtKVwzA==","note":"123456789","headImg":"dd2e54505eb2433ead14310159a22682.jpg","userPhone":null,"sex":0,"brithday":null,"vipNum":0},{"userId":10032,"userName":"hhj","longitude":103.86068,"latitude":30.778638,"token":"eL3Mekzu1YjfCpYafNuXXNpSRhamXgLNKSGtRUbLwkBa6V8cMezoFGiCPy+oR9CHL08VBc66BbKoPmgF3TeMzQ==","note":"我的个性签名","headImg":"3a5f60c8c1444473aac7f98d08845aef.jpg","userPhone":null,"sex":0,"brithday":null,"vipNum":0}]
     * managerUserId : [10031]
     * superManagerUserId : 10032
     */

    private String superManagerUserId;
    private List<UserList> userList;
    private List<String> managerUserId;

    public String getSuperManagerUserId() {
        return superManagerUserId;
    }

    public void setSuperManagerUserId(String superManagerUserId) {
        this.superManagerUserId = superManagerUserId;
    }

    public List<UserList> getUserList() {
        return userList;
    }

    public void setUserList(List<UserList> userList) {
        this.userList = userList;
    }

    public List<String> getManagerUserId() {
        return managerUserId;
    }

    public void setManagerUserId(List<String> managerUserId) {
        this.managerUserId = managerUserId;
    }

    public static class UserList {
        /**
         * userId : 10031
         * userName : 慢慢
         * longitude : 106.647961
         * latitude : 26.611986
         * token : gULdQVesK42Ot/yAZOE/iMJsINgl58oCdAcirHOpvj7AXdZY1nFd9rOnrY1+HOtGpRyIw9Y/1zoyzc7QtKVwzA==
         * note : 123456789
         * headImg : dd2e54505eb2433ead14310159a22682.jpg
         * userPhone : null
         * sex : 0
         * brithday : null
         * vipNum : 0
         */

        private String userId;
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
            String imghead;
            if(headImg.equals("")||headImg==null)
            {
                imghead="";
            }
            else if(headImg.startsWith("http"))
            {
                imghead=headImg;
            }
            else
            {
                imghead=AppConfig.sRootUrl+"/logincontroller/getHeadImg/"+headImg;
            }
            return imghead;
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
}
