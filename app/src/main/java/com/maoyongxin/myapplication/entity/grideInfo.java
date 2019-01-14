package com.maoyongxin.myapplication.entity;

/**
 * Created by yusr on 2018/5/29.
 */

public class grideInfo {

    private String FuwuName;
    private String FuwuImg;
    private  String FuwuId;

    public void setfuwuInfo(String FuwuName,String FuwuImg,String FuwuId){
        this.FuwuName=FuwuName;
        this.FuwuImg=FuwuImg;
        this.FuwuId=FuwuId;
    }

    public String getFuwuName(){
        return this.FuwuName;
    }
    public String getFuwuImg(){return  this.FuwuImg;}
    public String getFuwuId(){return  FuwuId;}

}
