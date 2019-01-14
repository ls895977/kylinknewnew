package com.maoyongxin.myapplication.ui.news.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class NewsItem implements Serializable {

    @SerializedName("_id")
    public String id;
    public String createAt;
    public String desc;
    public String publishedAt;
    public String type;
    public String url;
    public String who;
    public String day;
    public List<String> images;
    public String image;
    public boolean like;

    public String getImage() {
        if (image != null) {
            return image;
        } else if (!type.equals("重点新闻")){
            return images == null || images.size() == 0 ? null : images.get(0);
        } else {
            return url;
        }
    }
}
