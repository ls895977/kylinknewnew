package com.maoyongxin.myapplication.http;

import android.support.v4.util.SparseArrayCompat;

import com.maoyongxin.myapplication.myapp.AppConfig;


/**
 * Created by dingke on 2017/8/3.
 */

public class ApiMgr {


    private static SparseArrayCompat<String> sApiMap = new SparseArrayCompat<>();

    private static final String API_LOGIN = "/logincontroller";
    public static final int DO_REGISTER = 10;
    public static final int DO_LOGIN = 11;
    public static final int GET_VERCODE = 12;
    public static final int UPDATE_PSW = 13;
    private static final String API_USER = "/usercontroller";
    public static final int REFRESH_TOKEN = 20;
    public static final int GET_NEARBYLIST = 21;
    public static final int UPLOAD_LOCATION = 22;
    public static final int GET_USERINFO_BYID = 23;
    public static final int GET_MYFRIENDS = 24;
    public static final int UPDATE_FRIEND_NOTENAME = 25;
    public static final int DELETE_FRIEND = 26;
    public static final int GET_STRANGER_BY_KETWORD = 27;
    public static final int GET_STRANGER_LIST = 28;
    public static final int CHANGE_USERINFO = 29;

    private static final String API_FRIENDS = "/friendsrequestcontroller";
    public static final int ADD_FRIENDS = 30;
    public static final int GET_REQUESTLIST = 31;
    public static final int DEAL_REQUEST = 32;
    private static final String API_INTERESTING = "/interestcontroller";
    public static final int FIND_INTERESTING_LIST = 40;
    private static final String API_USER_INTERESTING = "/userinterestcontroller";
    public static final int UPLOAD_INTEREST = 50;
    public static final int GET_MYINTERESTING = 51;
    public static final int DELETE_MYINTEREST = 52;

    private static final String API_POI = "/poicontroller";
    public static final int GET_POITYPE = 60;

    private static final String API_THING = "/thingtypecontroller";
    public static final int GET_PROTYPE = 70;
    private static final String API_PUBLISH = "/noticecontroller";
    public static final int UPLOAD_MYPUBLISH = 80;
    public static final int GET_SELF_PUBLISH = 81;
    public static final int GET_PUBLIC_DETAIL = 82;
    public static final int DELETE_PUBLISH = 83;
    public static final int FIND_NOTICE_BYTHING = 84;
    private static final String API_FOLLOW = "/followcontroller";
    public static final int DO_LIKE_ONE = 90;
    public static final int GET_FOLLOWLIST = 91;
    public static final int UPDATE_FOLLOW = 92;
    public static final int DELETE_FOLLOW = 93;
    private static final String API_DYNAMIC = "/dynamiccontroller";
    public static final int GET_MY_DYNAMICLIST = 100;
    public static final int GET_STRANGER_DYNAMICLIST = 101;
    public static final int DELETE_MY_DYNAMIC = 102;
    private static final String API_COMMUNITY = "/communitycontroller";
    public static final int GET_NEARBY_COMMUNITY = 110;
    public static final int UPDATE_COMMUNITY_INFO = 111;
    private static final String API_USER_COMMUNITY = "/communityusercontroller";
    public static final int GET_MY_COMMUNITY = 121;
    public static final int GET_MY_COMMUNITY_USERS = 122;
    public static final int ASK_FOR_BECOMEMANAGER = 123;
    public static final int DO_OUTCOMMUNITY = 124;
    public static final int JOIN_COMMUNITY = 125;
    public static final int DELETE_COMMUNITY_USER = 126;
    public static final int GET_ASK_FOR_JOIN = 127;
    public static final int MANAGER_DEAL_REQUEST = 128;
    public static final int SUPERMANAGER_DEAL_REQUEST = 129;
    public static final int SUPERMANAGER_GET_REQUEST = 130;
    private static final String API_USER_COMMUNITY_REQUEST = "/communityrequestcontroller";
    public static final int GET_MYCREATEREQUEST = 140;
    private static final String API_BANNER = "/bannercontroller";
    public static final int GET_BANNERLIST = 150;
    private static final String API_ADDRESS = "/addresscontroller";
    public static final int GET_ADDRESS = 160;
    private static final String API_GROUP = "/groupController";
    public static final int CREATE_GROUP = 170;
    public static final int GET_GROUPINFO = 171;
    public static final int DELETE_GROUP = 172;
    public static final int UPDATE_GROUP = 173;
    private static final String API_GROUP_USER = "/groupUserController";
    public static final int GET_GROUP_LIST = 180;
    public static final int GET_GROUP_USER_LIST = 181;
    public static final int QUIET_GROUP = 182;
    public static final int ASKJION_GROUP = 183;
    public static final int UPDATE_GROUPNOTE = 184;
    public static final int GET_GROUPMESSAGE = 185;
    public static final int DEAL_GROUPMESSAGE = 186;
    private static final String API_PAY = "/aliPayController";
    public static final int GET_PAYMSG = 190;
    private static final String API_VIP = "/vipNumController";
    public static final int GET_VIPLIST = 200;

    public static final int POST_CHANNEL_ID = 250;
    private static final String API_CHANNEL_ID = "/pushcontroller";

    public static final int DYNAMIC_LIKE_ID = 251;
    private static final String DYNAMIC_LIKE_URL = "/dynamiclikecontroller/submitDynamicLike";
    public static final int DYNAMIC_COMMENT_ID = 252;
    private static final String DYNAMIC_COMMENT_URL = "/dynamiccommentcontroller/submitDynamicComment";

    static {
        sApiMap.put(DO_REGISTER, API_LOGIN + "/createUser");
        sApiMap.put(DO_LOGIN, API_LOGIN + "/userLogin");
        sApiMap.put(GET_VERCODE, API_LOGIN + "/getVerCodeMsm");
        sApiMap.put(REFRESH_TOKEN, API_USER + "/refreshToken");
        sApiMap.put(GET_NEARBYLIST, API_USER + "/getNearByUserList");
        sApiMap.put(UPLOAD_LOCATION, API_USER + "/uploadLocaltion");
        sApiMap.put(UPDATE_FRIEND_NOTENAME, API_USER + "/updateFriendNoteName");
        sApiMap.put(GET_MYFRIENDS, API_USER + "/getFriendList");
        sApiMap.put(DELETE_FRIEND, API_USER + "/deleteFriend");
        sApiMap.put(GET_USERINFO_BYID, API_USER + "/getUserInfoByUserId");
        sApiMap.put(GET_STRANGER_BY_KETWORD, API_USER + "/getStrangeUserByKeyWords");
        sApiMap.put(GET_STRANGER_LIST, API_USER + "/getStrangeUserList");
        sApiMap.put(CHANGE_USERINFO, API_USER + "/updateUserInfo");
        sApiMap.put(ADD_FRIENDS, API_FRIENDS + "/requestAddFriends");
        sApiMap.put(GET_REQUESTLIST, API_FRIENDS + "/getFriendsRequestList");
        sApiMap.put(DEAL_REQUEST, API_FRIENDS + "/dealRequest");
        sApiMap.put(FIND_INTERESTING_LIST, API_INTERESTING + "/findByParentId");
        sApiMap.put(UPLOAD_INTEREST, API_USER_INTERESTING + "/userSetInterest");
        sApiMap.put(GET_MYINTERESTING, API_USER_INTERESTING + "/userGetInterestList");
        sApiMap.put(DELETE_MYINTEREST, API_USER_INTERESTING + "/userDeleteInterest");
        sApiMap.put(GET_POITYPE, API_POI + "/getPoiList");
        sApiMap.put(GET_PROTYPE, API_THING + "/getThingTypeWithParentId");
        sApiMap.put(UPLOAD_MYPUBLISH, API_PUBLISH + "/submitNotice");
        sApiMap.put(GET_SELF_PUBLISH, API_PUBLISH + "/getSelfNotice");
        sApiMap.put(GET_PUBLIC_DETAIL, API_PUBLISH + "/getNoticeDetail");
        sApiMap.put(DELETE_PUBLISH, API_PUBLISH + "/deleteNotice");
        sApiMap.put(FIND_NOTICE_BYTHING, API_PUBLISH + "/getNotice");
        sApiMap.put(DO_LIKE_ONE, API_FOLLOW + "/addFollow");
        sApiMap.put(GET_FOLLOWLIST, API_FOLLOW + "/getFollowList");
        sApiMap.put(UPDATE_FOLLOW, API_FOLLOW + "/updateFollow");
        sApiMap.put(DELETE_FOLLOW, API_FOLLOW + "/deleteFollow");
        sApiMap.put(GET_MY_DYNAMICLIST, API_DYNAMIC + "/getDynamicListByUserId");
        sApiMap.put(GET_STRANGER_DYNAMICLIST, API_DYNAMIC + "/getDynamicList");
        sApiMap.put(DELETE_MY_DYNAMIC, API_DYNAMIC + "/deleteDynamic");
        sApiMap.put(GET_NEARBY_COMMUNITY, API_COMMUNITY + "/getCommunityList");//获取附近社区
        sApiMap.put(UPDATE_COMMUNITY_INFO, API_COMMUNITY + "/updateCommunityInfo");//修改小区描述
        sApiMap.put(GET_MY_COMMUNITY, API_USER_COMMUNITY + "/getCommunity");//获取自己加入的社区
        sApiMap.put(GET_MY_COMMUNITY_USERS, API_USER_COMMUNITY + "/getCommunityUserList");//获取小区成员
        sApiMap.put(ASK_FOR_BECOMEMANAGER, API_USER_COMMUNITY + "/requestCommunityManager");//申请成为小区管理员
        sApiMap.put(DO_OUTCOMMUNITY, API_USER_COMMUNITY + "/deleteCommunity");//退出小区
        sApiMap.put(JOIN_COMMUNITY, API_USER_COMMUNITY + "/addCommunity");//申请加入小区
        sApiMap.put(DELETE_COMMUNITY_USER, API_USER_COMMUNITY + "/managerDeleteUser");//移出成员
        sApiMap.put(GET_MYCREATEREQUEST, API_USER_COMMUNITY_REQUEST + "/getCommunityRequest");//获取自己创建小区的请求
        sApiMap.put(GET_ASK_FOR_JOIN, API_USER_COMMUNITY + "/managerGetRequest");//管理员获取加入信息
        sApiMap.put(MANAGER_DEAL_REQUEST, API_USER_COMMUNITY + "/managerDealRequest");//管理员处理加入信息
        sApiMap.put(SUPERMANAGER_GET_REQUEST, API_USER_COMMUNITY + "/superManagerGetManagerRequest");//超级管理员处理成为管理员信息
        sApiMap.put(SUPERMANAGER_DEAL_REQUEST, API_USER_COMMUNITY + "/superManagerDealManagerRequest");//超级管理员处理成为管理员信息
        sApiMap.put(GET_BANNERLIST, API_BANNER + "/getgetBannerList");//获取轮播图
        sApiMap.put(GET_ADDRESS, API_ADDRESS + "/getAddressByParentId");
        sApiMap.put(CREATE_GROUP, API_GROUP + "/createGroup");//建群
        sApiMap.put(GET_GROUPINFO, API_GROUP + "/getGroupInfo");//获取群信息
        sApiMap.put(UPDATE_GROUP, API_GROUP + "/updateGroupInfo");//修改群租信息
        sApiMap.put(GET_GROUP_LIST, API_GROUP_USER + "/getGroupList");//获取自己的群
        sApiMap.put(GET_GROUP_USER_LIST, API_GROUP_USER + "/getGroupUserList");//获取群成员
        sApiMap.put(DELETE_GROUP, API_GROUP + "/deleteGroup");//解散群
        sApiMap.put(QUIET_GROUP, API_GROUP_USER + "/quitGroup");//获取群成员
        sApiMap.put(ASKJION_GROUP, API_GROUP_USER + "/applyJoinGroup");//加群
        sApiMap.put(UPDATE_GROUPNOTE, API_GROUP_USER + "/updateGroupInfo");//修改自己在群里面的备注
        sApiMap.put(GET_GROUPMESSAGE, API_GROUP_USER + "/groupHostGetApplyJoinList");//群主获取加群请求
        sApiMap.put(DEAL_GROUPMESSAGE, API_GROUP_USER + "/groupHostDealJoinRequest");//群主处理加群请求
        sApiMap.put(GET_VIPLIST, API_VIP + "/getVipNumList");//获取vip价格
        sApiMap.put(GET_PAYMSG, API_PAY + "/getPayMessage");//获取阿里报文
        sApiMap.put(UPDATE_PSW, API_LOGIN + "/updatePassword");//修改密码
        sApiMap.put(POST_CHANNEL_ID, API_CHANNEL_ID + "/syncPush");
        sApiMap.put(DYNAMIC_LIKE_ID, DYNAMIC_LIKE_URL);
        sApiMap.put(DYNAMIC_COMMENT_ID, DYNAMIC_COMMENT_URL);
    }

    public static String getApiUrl(int api) {
        String apiTail = sApiMap.get(api);
        return AppConfig.sRootUrl + apiTail;
    }
}
