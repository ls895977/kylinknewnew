package com.maoyongxin.myapplication.ui;

import java.util.List;

/**
 * Created by Administrator on 2018/5/4 0004.
 */

public class newBean {


    /**
     * head : {"success":true,"msg":"成功！"}
     * body : {"id":"13","publishVersionNum":"11","publishVersionName":"0.0","fileName":"app-situ.apk","path":"http://st.3dgogo.com/uploads/files/st_versions_apk/20180719/00aa7747c22cfa3f553e3b46ebc85e34.apk","publishRemark":"11.0.0","createTime":"1531986797","isUpdated":"1","packageStatus":"1","versionsType":"100","newFunction":["广场列表点击头像查看详情","聊天列表添加好友弹窗"],"majorization":["管理员团队管理方式","优化了公司信息展示界面"]}
     */

    private HeadnewBean head;
    private BodynewBean body;

    public HeadnewBean getHead() {
        return head;
    }

    public void setHead(HeadnewBean head) {
        this.head = head;
    }

    public BodynewBean getBody() {
        return body;
    }

    public void setBody(BodynewBean body) {
        this.body = body;
    }

    public static class HeadnewBean {
        /**
         * success : true
         * msg : 成功！
         */

        private boolean success;
        private String msg;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    public static class BodynewBean {
        /**
         * id : 13
         * publishVersionNum : 11
         * publishVersionName : 0.0
         * fileName : app-situ.apk
         * path : http://st.3dgogo.com/uploads/files/st_versions_apk/20180719/00aa7747c22cfa3f553e3b46ebc85e34.apk
         * publishRemark : 11.0.0
         * createTime : 1531986797
         * isUpdated : 1
         * packageStatus : 1
         * versionsType : 100
         * newFunction : ["广场列表点击头像查看详情","聊天列表添加好友弹窗"]
         * majorization : ["管理员团队管理方式","优化了公司信息展示界面"]
         */

        private String id;
        private String publishVersionNum;
        private String publishVersionName;
        private String fileName;
        private String path;
        private String publishRemark;
        private String createTime;
        private String isUpdated;
        private String packageStatus;
        private String versionsType;
        private List<String> newFunction;
        private List<String> majorization;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPublishVersionNum() {
            return publishVersionNum;
        }

        public void setPublishVersionNum(String publishVersionNum) {
            this.publishVersionNum = publishVersionNum;
        }

        public String getPublishVersionName() {
            return publishVersionName;
        }

        public void setPublishVersionName(String publishVersionName) {
            this.publishVersionName = publishVersionName;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getPublishRemark() {
            return publishRemark;
        }

        public void setPublishRemark(String publishRemark) {
            this.publishRemark = publishRemark;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getIsUpdated() {
            return isUpdated;
        }

        public void setIsUpdated(String isUpdated) {
            this.isUpdated = isUpdated;
        }

        public String getPackageStatus() {
            return packageStatus;
        }

        public void setPackageStatus(String packageStatus) {
            this.packageStatus = packageStatus;
        }

        public String getVersionsType() {
            return versionsType;
        }

        public void setVersionsType(String versionsType) {
            this.versionsType = versionsType;
        }

        public List<String> getNewFunction() {
            return newFunction;
        }

        public void setNewFunction(List<String> newFunction) {
            this.newFunction = newFunction;
        }

        public List<String> getMajorization() {
            return majorization;
        }

        public void setMajorization(List<String> majorization) {
            this.majorization = majorization;
        }
    }
}
