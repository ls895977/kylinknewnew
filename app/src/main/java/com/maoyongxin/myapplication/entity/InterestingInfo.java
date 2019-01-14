package com.maoyongxin.myapplication.entity;

import java.util.List;

/**
 * Created by maoyongxin on 2017/12/7.
 */

public class InterestingInfo {

    private List<InterestList> interestList;

    public List<InterestList> getInterestList() {
        return interestList;
    }

    public void setInterestList(List<InterestList> interestList) {
        this.interestList = interestList;
    }

    public static class InterestList {
        /**
         * interestId : 1
         * interestName : 测试分类A
         * parentId : 0
         * level : 1
         * creatTime : 1512526474000
         */

        private String interestId;
        private String interestName;
        private String parentId;
        private int level;
        private String creatTime;

        public String getInterestId() {
            return interestId;
        }

        public void setInterestId(String interestId) {
            this.interestId = interestId;
        }

        public String getInterestName() {
            return interestName;
        }

        public void setInterestName(String interestName) {
            this.interestName = interestName;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getCreatTime() {
            return creatTime;
        }

        public void setCreatTime(String creatTime) {
            this.creatTime = creatTime;
        }
    }
}
