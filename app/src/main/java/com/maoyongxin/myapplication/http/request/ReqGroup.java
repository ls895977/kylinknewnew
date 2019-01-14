package com.maoyongxin.myapplication.http.request;

import android.content.Context;

import com.maoyongxin.myapplication.http.ApiMgr;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.response.BaseResp;
import com.maoyongxin.myapplication.http.response.GetGroupMessageResponse;
import com.maoyongxin.myapplication.http.response.GroupListResponse;
import com.maoyongxin.myapplication.http.response.GroupMemberResponse;
import com.maoyongxin.myapplication.http.response.GroupResponse;

/**
 * Created by maoyongxin on 2017/12/1.
 */

public class ReqGroup extends Req {
    public static void CreateGroup(Context context, String httpTag, String userId, String groupName, String groupNote,
                                   final EntityCallBack<GroupResponse> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.CREATE_GROUP, httpTag)
                .addBody("userId", userId)
                .addBody("groupName", groupName)
                .addBody("groupNote", groupNote)
                .build(), new EntityCallBack<GroupResponse>() {
            @Override
            public Class<GroupResponse> getEntityClass() {
                return GroupResponse.class;
            }

            @Override
            public void onSuccess(GroupResponse resp) {
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

    public static void getGroupInfo(Context context, String httpTag, String groupId,
                                    final EntityCallBack<GroupResponse> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.GET_GROUPINFO, httpTag)
                .addBody("groupId", groupId)
                .build(), new EntityCallBack<GroupResponse>() {
            @Override
            public Class<GroupResponse> getEntityClass() {
                return GroupResponse.class;
            }

            @Override
            public void onSuccess(GroupResponse resp) {
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

    public static void getMyGroupList(Context context, String httpTag, String userId,
                                      final EntityCallBack<GroupListResponse> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.GET_GROUP_LIST, httpTag)
                .addBody("userId", userId)
                .build(), new EntityCallBack<GroupListResponse>() {
            @Override
            public Class<GroupListResponse> getEntityClass() {
                return GroupListResponse.class;
            }

            @Override
            public void onSuccess(GroupListResponse resp) {
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

    public static void getGroupMembers(Context context, String httpTag, String groupId,
                                       final EntityCallBack<GroupMemberResponse> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.GET_GROUP_USER_LIST, httpTag)
                .addBody("groupId", groupId)
                .build(), new EntityCallBack<GroupMemberResponse>() {
            @Override
            public Class<GroupMemberResponse> getEntityClass() {
                return GroupMemberResponse.class;
            }

            @Override
            public void onSuccess(GroupMemberResponse resp) {
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

    public static void deleteGroup(Context context, String httpTag, String userId, String groupId,
                                   final EntityCallBack<BaseResp> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.DELETE_GROUP, httpTag)
                .addBody("userId", userId)
                .addBody("groupId", groupId)
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

    public static void quietGroup(Context context, String httpTag, String userId, String groupId,
                                  final EntityCallBack<BaseResp> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.QUIET_GROUP, httpTag)
                .addBody("userId", userId)
                .addBody("groupId", groupId)
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

    public static void updateGroup(Context context, String httpTag, String userId, String groupName, String groupNote, String groupId,
                                   final EntityCallBack<BaseResp> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.UPDATE_GROUP, httpTag)
                .addBody("userId", userId)
                .addBody("groupName", groupName)
                .addBody("groupNote", groupNote)
                .addBody("groupId", groupId)
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

    public static void joinGroup(Context context, String httpTag, String userId, String groupId, String applyNote,
                                 final EntityCallBack<BaseResp> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.ASKJION_GROUP, httpTag)
                .addBody("userId", userId)
                .addBody("applyNote", applyNote)
                .addBody("groupId", groupId)
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

    public static void upDateUserGroupName(Context context, String httpTag, String userId, String groupId, String noteName,
                                           final EntityCallBack<BaseResp> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.UPDATE_GROUPNOTE, httpTag)
                .addBody("userId", userId)
                .addBody("noteName", noteName)
                .addBody("groupId", groupId)
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

    public static void getGroupMessages(Context context, String httpTag, String userId,
                                        final EntityCallBack<GetGroupMessageResponse> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.GET_GROUPMESSAGE, httpTag)
                .addBody("userId", userId)
                .build(), new EntityCallBack<GetGroupMessageResponse>() {
            @Override
            public Class<GetGroupMessageResponse> getEntityClass() {
                return GetGroupMessageResponse.class;
            }

            @Override
            public void onSuccess(GetGroupMessageResponse resp) {
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
    public static void dealGroupMessages(Context context, String httpTag, String userId,String groupId,String joinUserId,String state,
                                        final EntityCallBack<BaseResp> callBack) {
        request(context, getCommonReqBuilder(ApiMgr.DEAL_GROUPMESSAGE, httpTag)
                .addBody("userId", userId)
                .addBody("groupId", groupId)
                .addBody("joinUserId", joinUserId)
                .addBody("state", state)
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
