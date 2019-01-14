package com.maoyongxin.myapplication.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by maoyongxin on 2017/12/14.
 */

public class PictureEntity implements Parcelable {
    private String imgUrl;
    private int imgResource;
    private String filePath;
    private String videoUrl;
    private String title;

    private int type;//1是图片，2是视频

    public PictureEntity(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getImgResource() {
        return imgResource;
    }

    public void setImgResource(int imgResource) {
        this.imgResource = imgResource;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imgUrl);
        dest.writeInt(this.imgResource);
        dest.writeString(this.filePath);
        dest.writeString(this.videoUrl);
        dest.writeString(this.title);
        dest.writeInt(this.type);
    }

    protected PictureEntity(Parcel in) {
        this.imgUrl = in.readString();
        this.imgResource = in.readInt();
        this.filePath = in.readString();
        this.videoUrl = in.readString();
        this.title = in.readString();
        this.type = in.readInt();
    }

    public static final Creator<PictureEntity> CREATOR = new Creator<PictureEntity>() {
        @Override
        public PictureEntity createFromParcel(Parcel source) {
            return new PictureEntity(source);
        }

        @Override
        public PictureEntity[] newArray(int size) {
            return new PictureEntity[size];
        }
    };
}
