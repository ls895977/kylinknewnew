package com.maoyongxin.myapplication.entity;

/**
 * Created by 3d on 2018/2/2.
 */

public class shanghuiInfo {
    private  String pic;
    private  String gourpName;
    private  String gourpId;
    private  String memNum;
    private String topicsNum;
    private String groupTheme;
    private String holderId;
    private String groupNote;

    public void setshanghuiInfo(String touxiang,String shanghuiname,String shanghuinum,String memNum,String topicsNum,String groupTheme,String holderId,String groupNote)
    {
        this.pic=touxiang;
        this.gourpName=shanghuiname;
        this.gourpId=shanghuinum;
        this.memNum=memNum;
        this.topicsNum=topicsNum;
        this.groupTheme=groupTheme;
        this.holderId=holderId;
        this.groupNote=groupNote;

    }



    public String getPic()
    {
        return this.pic;
    }
    public String getGroupName()
    {
        return this.gourpName;
    }
    public String getGroupNum()
    {
        return this.gourpId;
    }
    public String getMemNum()
    {
        return this.memNum;
    }
    public String getTopicsNum()
    {
        return this.topicsNum;
    }
    public String getGroupTheme()
    {
        return this.groupTheme;
    }
    public String  getHolderId(){return this.holderId;}
    public String getGroupNote(){return this.groupNote;}

}
