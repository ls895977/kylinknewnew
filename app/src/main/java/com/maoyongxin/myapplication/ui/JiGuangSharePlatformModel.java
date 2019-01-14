package com.maoyongxin.myapplication.ui;

import android.os.Parcel;
import android.os.Parcelable;

import com.maoyongxin.myapplication.R;

public class JiGuangSharePlatformModel implements Parcelable {
    private String platformName;

    private int DrawableID;
    public PlatFormType platFormType;
//    SinaWeibo.Name、SinaWeiboMessage.Name、QQ.Name、QZone.Name、Facebook.Name、FbMessenger.Name、Twitter.Name

    public JiGuangSharePlatformModel(String platformName) {

        switch (platformName) {
            case "Wechat":
                this.DrawableID = R.drawable.ic_wechat;
                this.platformName = "微信好友";
                this.platFormType = PlatFormType.WE_CHAT;
                break;
            case "WechatMoments":
                this.DrawableID = R.drawable.ic_pengyouquan;
                this.platformName = "微信朋友圈";
                this.platFormType = PlatFormType.WE_CHAT_MOMNETS;
                break;
            case "WechatFavorite":
                this.DrawableID = R.drawable.ic_wechat;
                this.platformName = "微信收藏";
                break;
            case "SinaWeibo":
                this.DrawableID = R.drawable.ic_sina0;
                this.platformName = "新浪微博";
                break;
            case "SinaWeiboMessage":
                this.platformName = "微信好友";
                break;
            case "QQ":
                this.DrawableID = R.drawable.ic_qq;
                this.platformName = "QQ好友";
                break;
            case "QZone":
                this.platformName = "QQ空间";
                this.DrawableID = R.drawable.ic_kongjian;
                break;
            case "Facebook":
                this.platformName = "FaceBook";
                break;
            case "FbMessenger":
                this.platformName = "微信好友";
                break;
            case "Twitter":
                this.platformName = "Twitter";
                break;
        }
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public int getDrawableID() {
        return DrawableID;
    }

    public void setDrawableID(int drawableID) {
        DrawableID = drawableID;
    }

    public PlatFormType getPlatFormType() {
        return platFormType;
    }

    public void setPlatFormType(PlatFormType platFormType) {
        this.platFormType = platFormType;
    }

    public enum PlatFormType {
        WE_CHAT, WE_CHAT_MOMNETS, SINA_WEIBO
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.platformName);
        dest.writeInt(this.DrawableID);
        dest.writeInt(this.platFormType == null ? -1 : this.platFormType.ordinal());
    }

    protected JiGuangSharePlatformModel(Parcel in) {
        this.platformName = in.readString();
        this.DrawableID = in.readInt();
        int tmpPlatFormType = in.readInt();
        this.platFormType = tmpPlatFormType == -1 ? null : PlatFormType.values()[tmpPlatFormType];
    }

    public static final Creator<JiGuangSharePlatformModel> CREATOR = new Creator<JiGuangSharePlatformModel>() {
        @Override
        public JiGuangSharePlatformModel createFromParcel(Parcel source) {
            return new JiGuangSharePlatformModel(source);
        }

        @Override
        public JiGuangSharePlatformModel[] newArray(int size) {
            return new JiGuangSharePlatformModel[size];
        }
    };
}


