package com.maoyongxin.myapplication.ui;

/**
 * Created by 3d on 2018/2/2.
 */

public class mingpianInfo {
    private String companyName;
    private String legalPersonName;
    private String zhiwei;

    private String TelNumber;
    private String email;

    private String webAdress;

    private String zhuying;
    public mingpianInfo(String companyName, String legalPersonName, String zhiwei, String TelNumber, String email, String webAdress, String zhuying)
    {
        this.companyName=companyName;
        this.legalPersonName=legalPersonName;
        this.zhiwei=zhiwei;
        this.TelNumber=TelNumber;
        this.email=email;
        this.webAdress=webAdress;
        this.zhuying=zhuying;


    }
    public String getcompanyName()
    {
        return companyName;
    }
    public String getlegalPersonName()
    {
        return legalPersonName;
    }
    public String getzhiwei()
    {
        return zhiwei;
    }
    public String getTelNumber()
    {
        return TelNumber;
    }
    public String getemail()
    {
        return email;
    }
    public String webAdress()
    {
        return webAdress;
    }
    public String getZhuying(){return  zhuying;}


}
