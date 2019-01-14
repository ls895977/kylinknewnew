package com.maoyongxin.myapplication.entity;

/**
 * Created by 鱼呈瑞 on 2018/3/24.
 */

public class fuwuInfo {
    private String fwsName;
    private String fwsHEadIma;
    private  String fwsIntro;
    private  String fwsadress;
    private String fwsDetail;
    public void setfuwuInfo(String fwsname,String fwsHEadIma,String fwsIntro,String fwsadress,String fwsDetail){
        this.fwsName=fwsname;
        this.fwsHEadIma=fwsHEadIma;
        this.fwsIntro=fwsIntro;
        this.fwsadress=fwsadress;
        this.fwsDetail=fwsDetail;

    }

    public String getFwsName(){
        return this.fwsName;
    }

    public String getFwsHEadIma(){
        return  this.fwsHEadIma;
    }
    public  String getFwsIntro(){
        return  this.fwsIntro;
    }
    public  String getAdress(){
        return  this.fwsadress;
    }
    public String getFwsDetail(){return  this.fwsDetail;}
}
