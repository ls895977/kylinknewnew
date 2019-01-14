package com.maoyongxin.myapplication.http.second;

import com.maoyongxin.myapplication.myapp.AppConfig;

/**
 * 接口
 * Created by admin on 2017/2/23.
 */

public class HttpContants {
    //获取反馈
    public static final String UPLOAD_MYPUBLISH= AppConfig.sRootUrl+"/noticecontroller/submitNotice";//noticeTitle=公告标题&&noticeType=（公告类型1跳转地址2内容显示）&&userId=用户账户&&areacode=地理编码&&content=具体内容&&businessType=1采购2出售&&thingTypeId=（服务器获取的三级物品的ID）
    public static final String UPLOAD_DONGTAI= AppConfig.sRootUrl+"/dynamiccontroller/createDynamic";//发布动态
    public static final String UPLOAD_HEADIMG= AppConfig.sRootUrl+"/logincontroller/uploadHeadImg";//上传头像
    public static final String CREATE_COMMUNITY= AppConfig.sRootUrl+"/communityrequestcontroller/createCommunity";//申请创建社区
    public static final String CHANGE_COMMUNITY_ICON= AppConfig.sRootUrl+"/communitycontroller/updateCommunityImg";//修改社区图片

}
