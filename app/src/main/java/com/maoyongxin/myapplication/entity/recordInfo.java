package com.maoyongxin.myapplication.entity;

/**
 * Created by 3d on 2018/2/2.
 */

public class recordInfo {

    private  String companyName;
    private  String companyTele;
    private  String recordTime;

    private  String teleinfo;
    private  String smginfo;
    private  String scinfo;

    public void setshanghuiInfo(String companyName,String companyTele,String recordTime,String teleinfo,String smginfo,String scinfo)
    {

        this.companyName=companyName;
        this.companyTele=companyTele;
        this.recordTime=recordTime;

        this.teleinfo=teleinfo;
        this.smginfo=smginfo;
        this.scinfo=scinfo;

    }


    public String  getcompanyName(){return  this.companyName; }
    public String getcompanyTele(){return this.companyTele;}
    public String getrecordTime(){return this.recordTime;}
    public String getteleinfo(){return this.teleinfo;}
    public String getsmginfo(){return this.smginfo;}
    public String getscinfo(){return this.scinfo;}
}
