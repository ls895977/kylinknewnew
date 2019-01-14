package com.maoyongxin.myapplication.entity;

import com.maoyongxin.myapplication.myapp.AppConfig;

import static com.maoyongxin.myapplication.ui.adapter.FollowListAdapter.getStrTime;

/**
 * Created by maoyongxin on 2017/12/1.
 */

public class UserInfoService {

    /**
     * userId : 10029
     * userName : 昵称
     * longitude : null
     * latitude : null
     * token : j4hr8L3wcohFhEakekQlt9pSRhamXgLNKSGtRUbLwkBa6V8cMezoFMVEue/ktSljQ5OKoRVDZrsnqw5SK36czw==
     * note : 备注
     * headImg : null
     * userPhone : null
     * sex : null
     * brithday : null
     * vipNum : null
     */
    private String friendNum;
    private String userId;
    private String userName;
    private String longitude;
    private String latitude;
    private String token;
    private String note;
    private String headImg;
    private String userPhone;
    private String sex;
    private String brithday;
    private String vipNum;
    private Boolean serverhead=true;



    public void  setFriendNum(String friendNum){
        this.friendNum=friendNum;
    }
    public  String getFriendNum(){
        return friendNum;
    }
    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        String userNames="";
        try
        {
             userNames=this.userName;
        }
        catch (Exception e)
        {
            userNames="";
        }
        return  userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }



    public String getHeadImg() {

            String  headurl="";

            if(serverhead)
            {

                if(headImg==null||headImg.equals("")){
                    headurl= "";
                }
                else{
                    if(headImg.startsWith("http"))
                    {
                        headurl=headImg;
                    }
                    else{
                        headurl= AppConfig.sRootUrl+"/logincontroller/getHeadImg/"+headImg;
                    }

                }
            }
            else
            {
                headurl=headImg;
            }
            return headurl;


    }


    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }
    public void setServerhead(Boolean isserver){
        this.serverhead=isserver;
    }

    public String getUserPhone() {
        if(userPhone==null){
            return "";
        }
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBrithday() {
        if(brithday==null||brithday.equals("")){
            return "";
        }else{
            return getStrTime(brithday);
        }
    }

    public void setBrithday(String brithday) {
        this.brithday = brithday;
    }

    public String getVipNum() {
        return vipNum;
    }

    public void setVipNum(String vipNum) {
        this.vipNum = vipNum;
    }
}
