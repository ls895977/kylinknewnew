package com.maoyongxin.myapplication.entity;

/**
 * Created by maoyongxin on 2018/1/10.
 */

public class GroupInfo {

    /**
     * groupId : 1
     * groupName : 群名称
     * groupHostId : 10030
     * groupNote : 群备注
     */

    private int groupId;
    private String groupName;
    private int groupHostId;
    private String groupNote;
    private String groupImg;

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getGroupHostId() {
        return groupHostId;
    }

    public void setGroupHostId(int groupHostId) {
        this.groupHostId = groupHostId;
    }

    public String getGroupNote() {
        return groupNote;
    }

    public void setGroupNote(String groupNote) {
        this.groupNote = groupNote;
    }

    public void setGroupImg(String GroupImg) {
        this.groupImg = GroupImg;
    }

    public String getGroupImg()

    {
      return  groupImg;
    }
}
