package com.maoyongxin.myapplication.http.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DynamicComment implements Parcelable {
    private int id;
    private String content;
    private long createTime;
    private int uid;
    private long dynamicId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public String getCreateTimeByDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(this.createTime);
        return simpleDateFormat.format(date);
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public long getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(long dynamicId) {
        this.dynamicId = dynamicId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.content);
        dest.writeLong(this.createTime);
        dest.writeInt(this.uid);
        dest.writeLong(this.dynamicId);
    }

    public DynamicComment() {
    }

    protected DynamicComment(Parcel in) {
        this.id = in.readInt();
        this.content = in.readString();
        this.createTime = in.readLong();
        this.uid = in.readInt();
        this.dynamicId = in.readLong();
    }

    public static final Creator<DynamicComment> CREATOR = new Creator<DynamicComment>() {
        @Override
        public DynamicComment createFromParcel(Parcel source) {
            return new DynamicComment(source);
        }

        @Override
        public DynamicComment[] newArray(int size) {
            return new DynamicComment[size];
        }
    };
}
