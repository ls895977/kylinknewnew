package com.maoyongxin.myapplication.entity;

/**
 * Created by yusr on 2018/5/29.
 */

public class tuijianInfo {

    private String tuijian_title;
    private String tuijian_subtitle;
    private String tuijianurl;
    private String share_img;
    private String tuijian_img;
    private String hostId;

    public void settuijianInfo(String tuijian_title,String tuijian_subtitle,String tuijianurl,String tuijian_img,String share_img,String hostId){
        this.tuijian_title=tuijian_title;
        this.tuijian_subtitle=tuijian_subtitle;
        this.tuijianurl=tuijianurl;
        this.tuijian_img=tuijian_img;
        this.share_img=share_img;
        this.hostId= hostId;
    }

    public String getTuijian_title(){
        return this.tuijian_title;
    }
    public String getTuijian_subtitle(){return  this.tuijian_subtitle;}
    public String getTuijianurl(){return  tuijianurl;}
    public String getTuijian_img(){return tuijian_img;}
    public String getShare_img() { return share_img;}
    public String getHostId(){return   hostId;}

}
