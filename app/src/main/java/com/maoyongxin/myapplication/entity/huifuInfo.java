package com.maoyongxin.myapplication.entity;

/**
 * Created by yusr on 2018/5/10.
 */

public class huifuInfo {

    private  String pic;
    private  String huifu_user;
    private  String huatiId;
    private  String huifu_time;
    private String huaticontent;
    private int  huifuCai;
    private int huifuZan;
    private String huifuUserId;
    private String InfoType;
    private String parentusrId;
    private String parentname;
    public void sethuifuInfo(String InfoType,String touxiang,String huifu_user,String huatiId,String huifu_time,String huaticontent,int huifuCai,int huifuZan,String huifuUserId,String parentname,String parentusrId)
    {
        this.pic=touxiang;
        this.huifu_user=huifu_user;
        this.huatiId=huatiId;
        this.huifu_time=huifu_time;
        this.huaticontent=huaticontent;

        this.huifuCai=huifuCai;
        this.huifuZan=huifuZan;

        this.huifuUserId=huifuUserId;
        this.InfoType=InfoType;

        this.parentname=parentname;
        this.parentusrId=parentusrId;
    }



    public String getPic()
    {
        return this.pic;
    }
    public String getHuifu_user()
    {
        return this.huifu_user;
    }
    public String getHuatiId()
    {
        return this.huatiId;
    }
    public String getHuifu_time()
    {
        return this.huifu_time;
    }
    public String getHuaticontent()
    {
        return this.huaticontent;
    }
    public String getInfoType(){return  this.InfoType;}
    public int getHuifuCai(){return  this.huifuCai;}
    public int getHuifuZan(){return this.huifuZan;}
    public String getHuifuUserId(){return this.huifuUserId;}
    public String getParentusrId(){return this.parentusrId;}
    public String getParentname() {return  this.parentname;}

}
