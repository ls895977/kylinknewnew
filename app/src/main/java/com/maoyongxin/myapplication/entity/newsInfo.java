package com.maoyongxin.myapplication.entity;

/**
 * Created by yusr on 2018/5/7.
 */

public class newsInfo {
    private  String newsTitle;
    private  String newsDate;
    private  String newsImg;
    private  String newurl;

    public void setNewsInfo(String newsTitle,String newsDate,String newsImg,String newsurl)
    {
        this.newsTitle=newsTitle;
        this.newsDate=newsDate;
        this.newsImg=newsImg;
        this.newurl=newsurl;

    }



    public String getNewsImg()
    {
        return this.newsImg;
    }
    public String getNewsTitle()
    {
        return this.newsTitle;
    }
    public String getNewsDate()
    {
        return this.newsDate;
    }
    public String getNewsurl()
    {
        return this.newurl;
    }

}
