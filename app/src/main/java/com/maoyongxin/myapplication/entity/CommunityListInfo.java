package com.maoyongxin.myapplication.entity;

import com.maoyongxin.myapplication.myapp.AppConfig;

import java.util.List;

/**
 * Created by maoyongxin on 2017/12/29.
 */

public class CommunityListInfo {

    private List<CommnunityList> commnunityList;

    public List<CommnunityList> getCommnunityList() {
        return commnunityList;
    }

    public void setCommnunityList(List<CommnunityList> commnunityList) {
        this.commnunityList = commnunityList;
    }

    public static class CommnunityList {
        /**
         * communityId : 1
         * communityName : 德福小区
         * communityNote : 高消费，低收入
         * areaCode : 520115
         * address : 贵州省贵阳市观山湖区德福中心
         * addressName : 德福中心
         * longitude : 126.666667
         * latitude : 26.666666
         * creatTime : 1514446150000
         * communityImg : null
         */

        private String communityId;
        private String communityName;
        private String communityNote;
        private int areaCode;
        private String address;
        private String addressName;
        private double longitude;
        private double latitude;
        private long creatTime;
        private String communityImg;

        public String getCommunityId() {
            return communityId;
        }

        public void setCommunityId(String communityId) {
            this.communityId = communityId;
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

        public String getCommunityImg() {
            if(communityImg!=null&&!communityImg.equals("")){
                return AppConfig.sRootUrl+"/communitycontroller/getCommunityImg/"+communityImg;
            }else {
                return "";
            }
        }

        public void setCommunityImg(String communityImg) {
            this.communityImg = communityImg;
        }
    }
}
