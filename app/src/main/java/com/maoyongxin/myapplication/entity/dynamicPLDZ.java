package com.maoyongxin.myapplication.entity;

/**
 * Created by yusr on 2018/5/13.
 */

public class dynamicPLDZ {
    private int numPL;
    private int numDZ;

    public void setInfo(int numPL,int numDZ)
    {
        this.numPL=numPL;
        this.numDZ=numDZ;
    }
    public int getNumPL(){
        return this.numPL;
    }
    public int getNumDZ()
    {
        return this.numDZ;
    }
}
