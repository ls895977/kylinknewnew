package com.maoyongxin.myapplication.entity;

/**
 * Created by yusr on 2018/5/7.
 */

public class newsZP {
    private  String numZan;
    private  String numPlun;


    public void setNewsInfo(String numZan,String numPlun)
    {
        this.numZan=numZan;
        this.numPlun=numPlun;


    }

    public void setNumZan(String numZan){
        this.numZan=numZan;
    }

    public void setNumPlun(String numPlun){
        this.numZan=numPlun;
    }

    public String getNumZan()
    {
        return this.numZan;
    }
    public String getNumPlun()
    {
        return this.numPlun;
    }

}
