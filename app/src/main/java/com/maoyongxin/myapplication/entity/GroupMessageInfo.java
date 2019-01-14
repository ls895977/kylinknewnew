package com.maoyongxin.myapplication.entity;

import java.util.List;

/**
 * Created by maoyongxin on 2018/1/11.
 */

public class GroupMessageInfo {

    private List<UserList> userList;

    public List<UserList> getUserList() {
        return userList;
    }

    public void setUserList(List<UserList> userList) {
        this.userList = userList;
    }

    public static class UserList {
        /**
         * id : 12
         * userType : null
         * groupId : 5
         * groupHostUserId : 10070
         * joinUserId : 10069
         * joinUserName : 毛毛虫
         * joinUserNote : 我想加入
         * joinState : 1
         * joinDate : 1515643688000
         */

        private int id;
        private Object userType;
        private int groupId;
        private int groupHostUserId;
        private int joinUserId;
        private String joinUserName;
        private String joinUserNote;
        private int joinState;
        private long joinDate;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Object getUserType() {
            return userType;
        }

        public void setUserType(Object userType) {
            this.userType = userType;
        }

        public int getGroupId() {
            return groupId;
        }

        public void setGroupId(int groupId) {
            this.groupId = groupId;
        }

        public int getGroupHostUserId() {
            return groupHostUserId;
        }

        public void setGroupHostUserId(int groupHostUserId) {
            this.groupHostUserId = groupHostUserId;
        }

        public int getJoinUserId() {
            return joinUserId;
        }

        public void setJoinUserId(int joinUserId) {
            this.joinUserId = joinUserId;
        }

        public String getJoinUserName() {
            return joinUserName;
        }

        public void setJoinUserName(String joinUserName) {
            this.joinUserName = joinUserName;
        }

        public String getJoinUserNote() {
            return joinUserNote;
        }

        public void setJoinUserNote(String joinUserNote) {
            this.joinUserNote = joinUserNote;
        }

        public int getJoinState() {
            return joinState;
        }

        public void setJoinState(int joinState) {
            this.joinState = joinState;
        }

        public long getJoinDate() {
            return joinDate;
        }

        public void setJoinDate(long joinDate) {
            this.joinDate = joinDate;
        }
    }
}
