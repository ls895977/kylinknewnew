package com.maoyongxin.myapplication.entity;

import java.util.List;

/**
 * Created by maoyongxin on 2017/12/7.
 */

public class MyInterestingInfo {


    private List<UserInterestList> userInterestList;

    public List<UserInterestList> getUserInterestList() {
        return userInterestList;
    }

    public void setUserInterestList(List<UserInterestList> userInterestList) {
        this.userInterestList = userInterestList;
    }

    public static class UserInterestList {
        /**
         * interestId : 103
         * userId : 10032
         * createTime : 1512618019000
         * interestName : 测试分类B1甲
         */

        private int interestId;
        private int userId;
        private long createTime;
        private String interestName;

        public int getInterestId() {
            return interestId;
        }

        public void setInterestId(int interestId) {
            this.interestId = interestId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getInterestName() {
            return interestName;
        }

        public void setInterestName(String interestName) {
            this.interestName = interestName;
        }
    }
}
