package com.maoyongxin.myapplication.entity;

import java.util.List;

/**
 * Created by maoyongxin on 2017/12/22.
 */

public class FollowListInfo {

    private List<FollowList> followList;

    public List<FollowList> getFollowList() {
        return followList;
    }

    public void setFollowList(List<FollowList> followList) {
        this.followList = followList;
    }

    public static class FollowList {
        /**
         * userId : 10032
         * followUserId : null
         * note : 慢慢
         * followDate : 1513699200000
         * id : 2
         */

        private int userId;
        private String followUserId;
        private String note;
        private String followDate;
        private int id;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getFollowUserId() {
            return followUserId;
        }

        public void setFollowUserId(String followUserId) {
            this.followUserId = followUserId;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getFollowDate() {
            return followDate;
        }

        public void setFollowDate(String followDate) {
            this.followDate = followDate;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
