package com.maoyongxin.myapplication.entity;

import java.util.List;

/**
 * Created by yongxin on 2018/1/10.
 */

public class GroupMemberInfo {

    private List<UserList> userList;

    public List<UserList> getUserList() {
        return userList;
    }

    public void setUserList(List<UserList> userList) {
        this.userList = userList;
    }

    public static class UserList {
        /**
         * id : 3
         * userType : 1
         * groupId : 3
         * groupHostUserId : 10064
         * joinUserId : 10064
         * joinUserName : 毛sir
         * joinUserNote : 群主
         * joinState : 3
         * joinDate : 1515588426000
         */

        private int id;
        private int userType;
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

        public int getUserType() {
            return userType;
        }

        public void setUserType(int userType) {
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
