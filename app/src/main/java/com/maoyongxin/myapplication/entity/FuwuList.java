package com.maoyongxin.myapplication.entity;

/**
 * Created by 鱼呈瑞 on 2018/3/24.
 */

public class FuwuList {
    private String FuwuName;
    private String fuwListId;
    private  String fuwuId;
    private String fuwuimg;

    public void setfuwuInfo(String FuwuName,String fuwuId){
        this.FuwuName=FuwuName;

        this.fuwuId=fuwuId;
    }
    public void setFuwuimg(String fwimg)
    {
        this.fuwuimg=fwimg;
    }

    public String getFuwuimg(){return this.fuwuimg;}
    public String getFuwuName(){
        return this.FuwuName;
    }

    public String getFuwuId(){return  fuwuId;}

}
