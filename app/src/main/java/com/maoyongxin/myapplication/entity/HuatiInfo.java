package com.maoyongxin.myapplication.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yongxin on 2018/1/21.
 */

public class HuatiInfo implements Parcelable {
    private String huatiName;
    private String huatiTime;
    private String huatiUser;
    private String huatiPinglun;
    private String huatiIp;
    private String HeadImag;
    private String HolderId;
    private String contentImg;
    private String groupName;
    private String groupId;
    private String huatiContent;
    private String numZan;
    private String grouppicurl;
    private String topparam = "0";
    private String gambitId;
    private String parentUserId;

    private String firstPerson;
    private String lase_response;
    private int res_count;
    private String first_headimg;



    public void setfirst_headimg(String first_headimg) {
        this.first_headimg = first_headimg;
    }

    public String getfirst_headimg() {
        return this.first_headimg;
    }

    public void setparentUserId(String parentUserId) {
        this.parentUserId = parentUserId;
    }

    public String getparentUserId() {
        return this.parentUserId;
    }

    public void setFirstPerson(String firstPerson)
    {
        this.firstPerson=firstPerson;
    }
    public String getFirstPerson()
{
    return  firstPerson;
}

    public void setlaset_response(String lase_response)
    {
        this.lase_response=lase_response;
    }
    public String getlaset_response()
    {
        return  lase_response;
    }

    public void setres_count(int res_count)
    {
        this.res_count=res_count;
    }
    public int getres_count()
    {
        return  res_count;
    }



    public void setGambitId(String gbitId) {
        this.gambitId = gbitId;
    }

    public String getGambitId() {
        return this.gambitId;
    }

    public String getHuatiContent() {
        return huatiContent;
    }

    public void setHuatiContent(String huatiContent) {
        this.huatiContent = huatiContent;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getHuatiName() {
        return huatiName;
    }

    public String getHuatiIp() {
        return huatiIp;
    }

    public void setHuatiName(String huatiName) {
        this.huatiName = huatiName;
    }



    public void setNumZan(String numZan) {
        this.numZan = numZan;
    }

    public String setContentImg(String ContentImg) {
        return this.contentImg = ContentImg;
    }

    public void settopparam(String topparam) {
        this.topparam = topparam;
    }

    public String getTopparam() {
        return topparam;
    }
    public void setHolderId(String holderId)
    {
        this.HolderId = holderId;
    }
    public String getHolderId() {
        return HolderId;
    }

    public String getHuatiTime() {
        return this.huatiTime;
    }

    public String getContentImg() {
        return this.contentImg;
    }

    public void setHuatiTime(String huatiTime) {
        this.huatiTime = huatiTime;
    }

    public String getHuatiUser() {
        return huatiUser;
    }

    public void setHuatiUser(String huatiUser) {
        this.huatiUser = huatiUser;
    }

    public String getHuatiPinglun() {
        return huatiPinglun;
    }

    public String getNumZan() {
        return numZan;
    }

    public void setHuatiPinglun(String huatiPinglun) {
        this.huatiPinglun = huatiPinglun;
    }

    public void setHuatiIp(String huatuIp) {
        this.huatiIp = huatuIp;
    }

    public void setGroupName(String Groupname) {
        this.groupName = Groupname;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getHeadImg() {
        if (HeadImag == null || HeadImag.equals("")) {
            return "";
        } else {
            return this.HeadImag;
        }
    }

    public void setgrouppicurl(String url) {
        this.grouppicurl = url;
    }

    public String getgrouppicurl() {
        return grouppicurl;
    }

    public void setHeadImg(String headImg) {
        this.HeadImag = headImg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.huatiName);
        dest.writeString(this.huatiTime);
        dest.writeString(this.huatiUser);
        dest.writeString(this.huatiPinglun);
        dest.writeString(this.huatiIp);
        dest.writeString(this.HeadImag);
        dest.writeString(this.HolderId);
        dest.writeString(this.contentImg);
        dest.writeString(this.groupName);
        dest.writeString(this.groupId);
        dest.writeString(this.huatiContent);
        dest.writeString(this.numZan);
        dest.writeString(this.grouppicurl);
        dest.writeString(this.topparam);
        dest.writeString(this.gambitId);
    }

    public HuatiInfo() {
    }

    protected HuatiInfo(Parcel in) {
        this.huatiName = in.readString();
        this.huatiTime = in.readString();
        this.huatiUser = in.readString();
        this.huatiPinglun = in.readString();
        this.huatiIp = in.readString();
        this.HeadImag = in.readString();
        this.HolderId = in.readString();
        this.contentImg = in.readString();
        this.groupName = in.readString();
        this.groupId = in.readString();
        this.huatiContent = in.readString();
        this.numZan = in.readString();
        this.grouppicurl = in.readString();
        this.topparam = in.readString();
        this.gambitId = in.readString();
    }

    public static final Parcelable.Creator<HuatiInfo> CREATOR = new Parcelable.Creator<HuatiInfo>() {
        @Override
        public HuatiInfo createFromParcel(Parcel source) {
            return new HuatiInfo(source);
        }

        @Override
        public HuatiInfo[] newArray(int size) {
            return new HuatiInfo[size];
        }
    };
}
