package com.maoyongxin.myapplication.entity;

/**
 * Created by 鱼呈瑞 on 2018/3/24.
 */

public class yellowPage_info {
    private String comapnyName;
    private String comapanyType;

    public void setCompanyInfo(String comapnyName,String comapnyType){
        this.comapnyName=comapnyName;
        this.comapanyType=comapnyType;
    }

    public String getComapnyName(){
        return this.comapnyName;
    }
    public String getComapanyType(){
        return this.comapanyType;
    }
}
