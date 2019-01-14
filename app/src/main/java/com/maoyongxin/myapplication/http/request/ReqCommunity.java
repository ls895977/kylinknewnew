package com.maoyongxin.myapplication.http.request;

import android.content.Context;

import com.maoyongxin.myapplication.http.ApiMgr;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.response.BaseResp;
import com.maoyongxin.myapplication.http.response.CommunityJoinResponse;
import com.maoyongxin.myapplication.http.response.CommunityListResponse;
import com.maoyongxin.myapplication.http.response.CommunityRequestResponse;
import com.maoyongxin.myapplication.http.response.CommunityUsersResponse;
import com.maoyongxin.myapplication.http.response.MyCommunityResponse;

/**
 * Created by maoyongxin on 2017/12/1.
 */

public class ReqCommunity extends Req {
    public static void getNearbyCommunityList(Context context, String httpTag, String areaCode,
                                              final EntityCallBack<CommunityListResponse> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.GET_NEARBY_COMMUNITY, httpTag)
                .addBody("areaCode", areaCode)
                .build(), new EntityCallBack<CommunityListResponse>() {
            @Override
            public Class<CommunityListResponse> getEntityClass() {
                return CommunityListResponse.class;
            }

            @Override
            public void onSuccess(CommunityListResponse resp) {
                callBack.onSuccess(resp);
            }

            @Override
            public void onFailure(Throwable e) {
                callBack.onFailure(e);
            }

            @Override
            public void onCancelled(Throwable e) {
                callBack.onCancelled(e);

            }

            @Override
            public void onFinished() {
                callBack.onFinished();
            }
        });
    }

    /**
     * 获取自己加入的小区
     *
     * @param context
     * @param httpTag
     * @param callBack
     */
    public static void getMyCommunity(Context context, String httpTag, String userId,
                                      final EntityCallBack<MyCommunityResponse> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.GET_MY_COMMUNITY, httpTag)
                .addBody("userId", userId)
                .build(), new EntityCallBack<MyCommunityResponse>() {
            @Override
            public Class<MyCommunityResponse> getEntityClass() {
                return MyCommunityResponse.class;
            }

            @Override
            public void onSuccess(MyCommunityResponse resp) {
                callBack.onSuccess(resp);
            }

            @Override
            public void onFailure(Throwable e) {
                callBack.onFailure(e);
            }

            @Override
            public void onCancelled(Throwable e) {
                callBack.onCancelled(e);

            }

            @Override
            public void onFinished() {
                callBack.onFinished();
            }
        });
    }

    /**
     * 获取自己小区成员
     *
     * @param context
     * @param httpTag
     * @param callBack
     */
    public static void getMyCommunityPersons(Context context, String httpTag, String userId, String communityId,
                                             final EntityCallBack<CommunityUsersResponse> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.GET_MY_COMMUNITY_USERS, httpTag)
                .addBody("userId", userId)
                .addBody("communityId", communityId)
                .build(), new EntityCallBack<CommunityUsersResponse>() {
            @Override
            public Class<CommunityUsersResponse> getEntityClass() {
                return CommunityUsersResponse.class;
            }

            @Override
            public void onSuccess(CommunityUsersResponse resp) {
                callBack.onSuccess(resp);
            }

            @Override
            public void onFailure(Throwable e) {
                callBack.onFailure(e);
            }

            @Override
            public void onCancelled(Throwable e) {
                callBack.onCancelled(e);

            }

            @Override
            public void onFinished() {
                callBack.onFinished();
            }
        });
    }

    /**
     * 申请成为管理员
     *
     * @param context
     * @param httpTag
     * @param callBack
     */
    public static void becomeManager(Context context, String httpTag, String userId, String communityId, String note,
                                     final EntityCallBack<BaseResp> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.ASK_FOR_BECOMEMANAGER, httpTag)
                .addBody("userId", userId)
                .addBody("note", note)
                .addBody("communityId", communityId)
                .build(), new EntityCallBack<BaseResp>() {
            @Override
            public Class<BaseResp> getEntityClass() {
                return BaseResp.class;
            }

            @Override
            public void onSuccess(BaseResp resp) {
                callBack.onSuccess(resp);
            }

            @Override
            public void onFailure(Throwable e) {
                callBack.onFailure(e);
            }

            @Override
            public void onCancelled(Throwable e) {
                callBack.onCancelled(e);

            }

            @Override
            public void onFinished() {
                callBack.onFinished();
            }
        });
    }

    /**
     * 退出小区
     *
     * @param context
     * @param httpTag
     * @param callBack
     */
    public static void deleteCommunity(Context context, String httpTag, String userId, String communityId,
                                       final EntityCallBack<BaseResp> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.DO_OUTCOMMUNITY, httpTag)
                .addBody("userId", userId)
                .addBody("communityId", communityId)
                .build(), new EntityCallBack<BaseResp>() {
            @Override
            public Class<BaseResp> getEntityClass() {
                return BaseResp.class;
            }

            @Override
            public void onSuccess(BaseResp resp) {
                callBack.onSuccess(resp);
            }

            @Override
            public void onFailure(Throwable e) {
                callBack.onFailure(e);
            }

            @Override
            public void onCancelled(Throwable e) {
                callBack.onCancelled(e);

            }

            @Override
            public void onFinished() {
                callBack.onFinished();
            }
        });
    }

    /**
     * 加入小区
     *
     * @param context
     * @param httpTag
     * @param callBack
     */
    public static void joinCommunity(Context context, String httpTag, String userId, String communityId, String note,
                                     final EntityCallBack<BaseResp> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.JOIN_COMMUNITY, httpTag)
                .addBody("userId", userId)
                .addBody("communityId", communityId)
                .addBody("note", note)
                .build(), new EntityCallBack<BaseResp>() {
            @Override
            public Class<BaseResp> getEntityClass() {
                return BaseResp.class;
            }

            @Override
            public void onSuccess(BaseResp resp) {
                callBack.onSuccess(resp);
            }

            @Override
            public void onFailure(Throwable e) {
                callBack.onFailure(e);
            }

            @Override
            public void onCancelled(Throwable e) {
                callBack.onCancelled(e);

            }

            @Override
            public void onFinished() {
                callBack.onFinished();
            }
        });
    }

    /**
     * 管理员移出小区成员
     *
     * @param context
     * @param httpTag
     * @param callBack
     */
    public static void deleteUser(Context context, String httpTag, String managerId, String deleteUserId, String communityId,
                                  final EntityCallBack<BaseResp> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.DELETE_COMMUNITY_USER, httpTag)
                .addBody("managerId", managerId)
                .addBody("deleteUserId", deleteUserId)
                .addBody("communityId", communityId)
                .build(), new EntityCallBack<BaseResp>() {
            @Override
            public Class<BaseResp> getEntityClass() {
                return BaseResp.class;
            }

            @Override
            public void onSuccess(BaseResp resp) {
                callBack.onSuccess(resp);
            }

            @Override
            public void onFailure(Throwable e) {
                callBack.onFailure(e);
            }

            @Override
            public void onCancelled(Throwable e) {
                callBack.onCancelled(e);

            }

            @Override
            public void onFinished() {
                callBack.onFinished();
            }
        });
    }

    /**
     * 获取自己的创建请求
     *
     * @param context
     * @param httpTag
     * @param callBack
     */
    public static void getMyCreatRequest(Context context, String httpTag, String userId,
                                         final EntityCallBack<CommunityRequestResponse> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.GET_MYCREATEREQUEST, httpTag)
                .addBody("userId", userId)
                .build(), new EntityCallBack<CommunityRequestResponse>() {
            @Override
            public Class<CommunityRequestResponse> getEntityClass() {
                return CommunityRequestResponse.class;
            }

            @Override
            public void onSuccess(CommunityRequestResponse resp) {
                callBack.onSuccess(resp);
            }

            @Override
            public void onFailure(Throwable e) {
                callBack.onFailure(e);
            }

            @Override
            public void onCancelled(Throwable e) {
                callBack.onCancelled(e);

            }

            @Override
            public void onFinished() {
                callBack.onFinished();
            }
        });
    }

    /**
     * 管理员获取加入请求
     *
     * @param context
     * @param httpTag
     * @param callBack
     */
    public static void getAskRequest(Context context, String httpTag, String userId, String communityId,
                                     final EntityCallBack<CommunityJoinResponse> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.GET_ASK_FOR_JOIN, httpTag)
                .addBody("userId", userId)
                .addBody("communityId", communityId)

                .build(), new EntityCallBack<CommunityJoinResponse>() {
            @Override
            public Class<CommunityJoinResponse> getEntityClass() {
                return CommunityJoinResponse.class;
            }

            @Override
            public void onSuccess(CommunityJoinResponse resp) {
                callBack.onSuccess(resp);
            }

            @Override
            public void onFailure(Throwable e) {
                callBack.onFailure(e);
            }

            @Override
            public void onCancelled(Throwable e) {
                callBack.onCancelled(e);

            }

            @Override
            public void onFinished() {
                callBack.onFinished();
            }
        });
    }

    /**
     * 管理员处理加入请求
     *
     * @param context
     * @param httpTag
     * @param callBack
     */
    public static void dealJoinRequest(Context context, String httpTag, String managerId, String requestUserId, String communityId, String addType,
                                       final EntityCallBack<BaseResp> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.MANAGER_DEAL_REQUEST, httpTag)
                .addBody("managerId", managerId)
                .addBody("requestUserId", requestUserId)
                .addBody("communityId", communityId)
                .addBody("addType", addType)
                .build(), new EntityCallBack<BaseResp>() {
            @Override
            public Class<BaseResp> getEntityClass() {
                return BaseResp.class;
            }

            @Override
            public void onSuccess(BaseResp resp) {
                callBack.onSuccess(resp);
            }

            @Override
            public void onFailure(Throwable e) {
                callBack.onFailure(e);
            }

            @Override
            public void onCancelled(Throwable e) {
                callBack.onCancelled(e);

            }

            @Override
            public void onFinished() {
                callBack.onFinished();
            }
        });
    }

    /**
     * 超级管理员获取成为管理员请求
     *
     * @param context
     * @param httpTag
     * @param callBack
     */
    public static void getSuperRequest(Context context, String httpTag, String userId, String communityId,
                                       final EntityCallBack<CommunityJoinResponse> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.SUPERMANAGER_GET_REQUEST, httpTag)
                .addBody("userId", userId)
                .addBody("communityId", communityId)

                .build(), new EntityCallBack<CommunityJoinResponse>() {
            @Override
            public Class<CommunityJoinResponse> getEntityClass() {
                return CommunityJoinResponse.class;
            }

            @Override
            public void onSuccess(CommunityJoinResponse resp) {
                callBack.onSuccess(resp);
            }

            @Override
            public void onFailure(Throwable e) {
                callBack.onFailure(e);
            }

            @Override
            public void onCancelled(Throwable e) {
                callBack.onCancelled(e);

            }

            @Override
            public void onFinished() {
                callBack.onFinished();
            }
        });
    }

    /**
     * 超级管理员处理成为管理员请求
     *
     * @param context
     * @param httpTag
     * @param callBack
     */
    public static void dealSuperRequest(Context context, String httpTag, String superManagerId, String requestUserId, String communityId, String addType,
                                        final EntityCallBack<BaseResp> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.SUPERMANAGER_DEAL_REQUEST, httpTag)
                .addBody("superManagerId", superManagerId)
                .addBody("requestUserId", requestUserId)
                .addBody("communityId", communityId)
                .addBody("type", addType)
                .build(), new EntityCallBack<BaseResp>() {
            @Override
            public Class<BaseResp> getEntityClass() {
                return BaseResp.class;
            }

            @Override
            public void onSuccess(BaseResp resp) {
                callBack.onSuccess(resp);
            }

            @Override
            public void onFailure(Throwable e) {
                callBack.onFailure(e);
            }

            @Override
            public void onCancelled(Throwable e) {
                callBack.onCancelled(e);

            }

            @Override
            public void onFinished() {
                callBack.onFinished();
            }
        });
    }

    /**
     * 超级管理员修改小区描述
     *
     * @param context
     * @param httpTag
     * @param callBack
     */
    public static void updateCommunityNote(Context context, String httpTag, String userId, String communityNote, String communityId,
                                           final EntityCallBack<BaseResp> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.UPDATE_COMMUNITY_INFO, httpTag)
                .addBody("userId", userId)
                .addBody("communityNote", communityNote)
                .addBody("communityId", communityId)
                .build(), new EntityCallBack<BaseResp>() {
            @Override
            public Class<BaseResp> getEntityClass() {
                return BaseResp.class;
            }

            @Override
            public void onSuccess(BaseResp resp) {
                callBack.onSuccess(resp);
            }

            @Override
            public void onFailure(Throwable e) {
                callBack.onFailure(e);
            }

            @Override
            public void onCancelled(Throwable e) {
                callBack.onCancelled(e);

            }

            @Override
            public void onFinished() {
                callBack.onFinished();
            }
        });
    }
}
