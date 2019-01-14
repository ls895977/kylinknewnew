package com.maoyongxin.myapplication.entity;

import com.maoyongxin.myapplication.http.response.DynamicComment;
import com.maoyongxin.myapplication.myapp.AppConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maoyongxin on 2017/12/25.
 */

public class MyDynamicListInfo {

    private List<DynamicList> dynamicList;

    public List<DynamicList> getDynamicList() {
        return dynamicList;
    }

    public void setDynamicList(List<DynamicList> dynamicList) {
        this.dynamicList = dynamicList;
    }

    public static class DynamicList {
        /**
         * dynamic : {"dynamicId":3,"createTime":1514171791000,"dynamicContent":"基因送一下","userId":10032}
         * imgList : [{"imgUrl":"4d65118b1d55487580f2dd815aea6a80.jpg"},{"imgUrl":"d3153b5119db4669a78839ff340f6577.jpg"}]
         */

        private Dynamic dynamic;
        private List<ImgList> imgList;
        private List<VideoList> videoList;
        private String dynamicContent;
        private int likeNum;
        private ArrayList<DynamicComment> dynamicCommentList;

        public String getDynamicContent() {
            return dynamicContent;
        }

        public void setDynamicContent(String dynamicContent) {
            this.dynamicContent = dynamicContent;
        }

        public List<VideoList> getVideoList() {
            return videoList;
        }

        public void setVideoList(List<VideoList> videoList) {
            this.videoList = videoList;
        }

        public Dynamic getDynamic() {
            return dynamic;
        }

        public void setDynamic(Dynamic dynamic) {
            this.dynamic = dynamic;
        }

        public List<ImgList> getImgList() {
            return imgList;
        }

        public void setImgList(List<ImgList> imgList) {
            this.imgList = imgList;
        }

        public int getLikeNum() {
            return likeNum;
        }

        public void setLikeNum(int likeNum) {
            this.likeNum = likeNum;
        }

        public ArrayList<DynamicComment> getDynamicCommentList() {
            return dynamicCommentList;
        }

        public void setDynamicCommentList(ArrayList<DynamicComment> dynamicCommentList) {
            this.dynamicCommentList = dynamicCommentList;
        }

        public static class Dynamic {
            /**
             * dynamicId : 3
             * createTime : 1514171791000
             * dynamicContent : 基因送一下
             * userId : 10032
             */

            private int dynamicId;
            private String createTime;
            private String dynamicContent;
            private int userId;

            public int getDynamicId() {
                return dynamicId;
            }

            public void setDynamicId(int dynamicId) {
                this.dynamicId = dynamicId;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getDynamicContent() {
                return dynamicContent;
            }

            public void setDynamicContent(String dynamicContent) {
                this.dynamicContent = dynamicContent;
            }

            public int getUserId() {
                return userId;
            }

            public void c(int userId) {
                this.userId = userId;
            }
        }

        public static class ImgList {
            /**
             * imgUrl : 4d65118b1d55487580f2dd815aea6a80.jpg
             */

            private String imgUrl;

            public String getImgUrl() {
                return AppConfig.sRootUrl + "/dynamiccontroller/getDynamicImg/" + imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }
        }

        public static class VideoList {
            private String videoUrl;

            public String getVideoUrl() {
                return "http://118.24.2.164:8083" + videoUrl;
               //  return "http://st.3dgogo.com:8083" + videoUrl;
            }

            public String getShareUrl()
            {
                return "http://st.3dgogo.com:8083" + videoUrl;
            }
            public void setVideoUrl(String videoUrl) {
                this.videoUrl = videoUrl;
            }
        }
    }
}
