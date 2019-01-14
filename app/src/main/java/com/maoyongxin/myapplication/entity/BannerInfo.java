package com.maoyongxin.myapplication.entity;

import com.maoyongxin.myapplication.myapp.AppConfig;

import java.util.List;

/**
 * Created by maoyongxin on 2018/1/10.
 */

public class BannerInfo {

    private List<BannerList> bannerList;

    public List<BannerList> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<BannerList> bannerList) {
        this.bannerList = bannerList;
    }

    public static class BannerList {
        /**
         * id : 33
         * imgUrl : dda99eb5168144dc95616166ac6451b6
         * connectionAddress : https://www.baidu.com
         * creatTime : 1515424330000
         */

        private int id;
        private String imgUrl;
        private String connectionAddress;
        private long creatTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImgUrl() {
            return AppConfig.sRootUrl+"/bannercontroller/getBannerImg/"+imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getConnectionAddress() {
            return connectionAddress;
        }

        public void setConnectionAddress(String connectionAddress) {
            this.connectionAddress = connectionAddress;
        }

        public long getCreatTime() {
            return creatTime;
        }

        public void setCreatTime(long creatTime) {
            this.creatTime = creatTime;
        }
    }
}
