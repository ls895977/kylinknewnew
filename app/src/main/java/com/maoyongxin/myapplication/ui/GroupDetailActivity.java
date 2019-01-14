package com.maoyongxin.myapplication.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.GroupInfo;
import com.maoyongxin.myapplication.entity.GroupMemberInfo;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqGroup;
import com.maoyongxin.myapplication.http.response.BaseResp;
import com.maoyongxin.myapplication.http.response.GroupMemberResponse;
import com.maoyongxin.myapplication.http.response.GroupResponse;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.myapp.AppTitleBarActivity;
import com.maoyongxin.myapplication.server.utils.NToast;
import com.maoyongxin.myapplication.server.utils.OperationRong;
import com.maoyongxin.myapplication.server.widget.SelectableRoundedImageView;
import com.maoyongxin.myapplication.ui.editapp.minefragment.xiangqingye;
import com.maoyongxin.myapplication.ui.widget.DemoGridView;
import com.maoyongxin.myapplication.ui.widget.GroupPopWindow;
import com.maoyongxin.myapplication.ui.widget.switchbutton.SwitchButton;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GroupDetailActivity extends AppTitleBarActivity implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.gridview)
    DemoGridView gridview;
    @BindView(R.id.group_member_size)
    TextView groupMemberSize;
    @BindView(R.id.group_member_size_item)
    RelativeLayout groupMemberSizeItem;
    @BindView(R.id.ac_ll_search_chatting_records)
    LinearLayout acLlSearchChattingRecords;
    @BindView(R.id.group_name)
    TextView groupName;
    @BindView(R.id.ll_group_name)
    LinearLayout llGroupName;
    @BindView(R.id.ac_ll_group_announcement_divider)
    LinearLayout acLlGroupAnnouncementDivider;
    @BindView(R.id.group_announcement)
    LinearLayout groupAnnouncement;
    @BindView(R.id.sw_group_notfaction)
    SwitchButton swGroupNotfaction;
    @BindView(R.id.sw_group_top)
    SwitchButton swGroupTop;
    @BindView(R.id.group_clean)
    LinearLayout groupClean;
    @BindView(R.id.group_quit)
    Button groupQuit;
    @BindView(R.id.group_dismiss)
    Button groupDismiss;
    @BindView(R.id.tv_groupContent)
    TextView tvGroupContent;
    @BindView(R.id.ll_group_edit)
    LinearLayout llGroupEdit;
    @BindView(R.id.group_userNote)
    TextView groupUserNote;
    @BindView(R.id.ll_group_userNote)
    LinearLayout llGroupUserNote;
    @BindView(R.id.icon_back)
    TextView iconBack;
    @BindView(R.id.tv_setting)
    TextView tvSetting;
    @BindView(R.id.group_pic)
    ImageView groupPic;
    @BindView(R.id.tvgroupname)
    TextView tvGroupName;
    @BindView(R.id.group_id)
    TextView tv_groupId;

    private String groupId;
    private GroupInfo mGroup;
    private boolean isCreater;
    private String picurl;
    private Handler handler;
    final private int UPDATESUCCESS=1;
    private String currentUsrid;
    @Override
    protected int bindLayout() {
        return R.layout.activity_group_detail;
    }


    @Override
    protected void initEvent() {
        super.initEvent();
        groupPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),group_headEidt.class);
                intent.putExtra("groupId",groupId);
                intent.putExtra("showImg",picurl);
                if(true)
                {
                    startActivityForResult(intent,1);
                }
                else{
                    Toast.makeText(getActivity(),"只有群主才能设置",Toast.LENGTH_SHORT).show();
                }

            }
        });
        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GroupPopWindow groupPopWindow = new GroupPopWindow(GroupDetailActivity.this, groupId);
                groupPopWindow.showPopupWindow(tvSetting);
            }
        });
        llGroupUserNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText inputServer = new EditText(GroupDetailActivity.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(GroupDetailActivity.this);
                builder.setTitle("请输入备注").setView(inputServer)
                        .setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ReqGroup.upDateUserGroupName(GroupDetailActivity.this, getActivityTag(), AppApplication.getCurrentUserInfo().getUserId(), groupId, inputServer.getText().toString(), new EntityCallBack<BaseResp>() {
                            @Override
                            public Class<BaseResp> getEntityClass() {
                                return BaseResp.class;
                            }

                            @Override
                            public void onSuccess(BaseResp resp) {
                                if (resp.is200()) {
                                    getGroupMembers();
                                }
                            }

                            @Override
                            public void onFailure(Throwable e) {

                            }

                            @Override
                            public void onCancelled(Throwable e) {

                            }

                            @Override
                            public void onFinished() {

                            }
                        });
                    }
                });
                builder.show();
            }
        });
        groupQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GroupDetailActivity.this);
                builder.setTitle("确定退出么？").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ReqGroup.quietGroup(GroupDetailActivity.this, getActivityTag(), AppApplication.getCurrentUserInfo().getUserId(), groupId, new EntityCallBack<BaseResp>() {
                            @Override
                            public Class<BaseResp> getEntityClass() {
                                return BaseResp.class;
                            }

                            @Override
                            public void onSuccess(BaseResp resp) {
                                showToastLong(resp.msg);
                                if (resp.is200()) {
                                    finish();
                                    sendFinishBroad();
                                }
                            }


                            @Override
                            public void onFailure(Throwable e) {

                            }

                            @Override
                            public void onCancelled(Throwable e) {

                            }

                            @Override
                            public void onFinished() {

                            }
                        });
                    }
                }).show();
            }
        });
        tvGroupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupDetailActivity.this, EditGroupActivity.class);
                intent.putExtra("groupId", groupId);
                if(groupId.equals(currentUsrid))
                {
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getActivity(),"只有群主才能设置",Toast.LENGTH_SHORT).show();
                }

            }
        });
        llGroupEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupDetailActivity.this, EditGroupActivity.class);
                intent.putExtra("groupId", groupId);
                if(groupId.equals(currentUsrid))
                {
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getActivity(),"只有群主才能设置",Toast.LENGTH_SHORT).show();
                }
            }
        });



        groupDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GroupDetailActivity.this);
                builder.setTitle("确定要解散这个群么？").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ReqGroup.deleteGroup(GroupDetailActivity.this, getActivityTag(), AppApplication.getCurrentUserInfo().getUserId(), groupId, new EntityCallBack<BaseResp>() {
                            @Override
                            public Class<BaseResp> getEntityClass() {
                                return BaseResp.class;
                            }

                            @Override
                            public void onSuccess(BaseResp resp) {
                                showToastLong(resp.msg);
                                if (resp.is200()) {
                                    finish();
                                    sendFinishBroad();
                                }
                            }

                            @Override
                            public void onFailure(Throwable e) {

                            }

                            @Override
                            public void onCancelled(Throwable e) {

                            }

                            @Override
                            public void onFinished() {

                            }
                        });
                    }
                }).show();
            }
        });

        groupClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GroupDetailActivity.this);
                builder.setTitle("确定清除记录吗？").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (RongIM.getInstance() != null) {
                            if (mGroup != null) {
                                RongIM.getInstance().clearMessages(Conversation.ConversationType.GROUP, mGroup.getGroupId() + "", new RongIMClient.ResultCallback<Boolean>() {
                                    @Override
                                    public void onSuccess(Boolean aBoolean) {
                                        NToast.shortToast(GroupDetailActivity.this, getString(R.string.clear_success));
                                    }

                                    @Override
                                    public void onError(RongIMClient.ErrorCode errorCode) {
                                        NToast.shortToast(GroupDetailActivity.this, getString(R.string.clear_failure));
                                    }
                                });
                            }
                        }
                    }
                }).show();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        groupId = getIntent().getStringExtra("groupId");
        currentUsrid=AppApplication.getCurrentUserInfo().getUserId()+"";
        getgroupurl(groupId);
    }

    private void sendFinishBroad() {
        Intent intent = new Intent();
        intent.setAction("finish");
        GroupDetailActivity.this.sendBroadcast(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getGroupInfo();
        getGroupMembers();
    }

    @Override
    protected void handlerPassMsg(int target, int target1, Object obj) {

    }

    @Override
    protected void initView() {

        super.initView();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//白底黑字+沉浸式导航栏
        getGroupInfo();
        getGroupMembers();
        groupUserNote.setText(AppApplication.getCurrentUserInfo().getUserName());
        tv_groupId.setText(groupId);
    }

    private void initSwichEvent() {
        swGroupTop.setOnCheckedChangeListener(this);
        swGroupNotfaction.setOnCheckedChangeListener(this);
        if (RongIM.getInstance() != null) {
            RongIM.getInstance().getConversation(Conversation.ConversationType.GROUP, mGroup.getGroupId() + "", new RongIMClient.ResultCallback<Conversation>() {
                @Override
                public void onSuccess(Conversation conversation) {
                    if (conversation == null) {
                        return;
                    }
                    if (conversation.isTop()) {
                        swGroupTop.setChecked(true);
                    } else {
                        swGroupTop.setChecked(false);
                    }

                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });

            RongIM.getInstance().getConversationNotificationStatus(Conversation.ConversationType.GROUP, mGroup.getGroupId() + "", new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
                @Override
                public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {

                    if (conversationNotificationStatus == Conversation.ConversationNotificationStatus.DO_NOT_DISTURB) {
                        swGroupNotfaction.setChecked(true);
                    } else {
                        swGroupNotfaction.setChecked(false);
                    }
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }
    }

    private void getGroupMembers() {
        ReqGroup.getGroupMembers(this, getActivityTag(), groupId, new EntityCallBack<GroupMemberResponse>() {
            @Override
            public Class<GroupMemberResponse> getEntityClass() {
                return GroupMemberResponse.class;
            }

            @Override
            public void onSuccess(GroupMemberResponse resp) {
                if (resp.is200()) {
                    for (int i = 0; i < resp.obj.getUserList().size(); i++) {
                        GridAdapter adapter = new GridAdapter(resp.obj.getUserList(), GroupDetailActivity.this);
                        gridview.setAdapter(adapter);
                        String userId = resp.obj.getUserList().get(i).getJoinUserId() + "";
                        if (userId.equals(AppApplication.getCurrentUserInfo().getUserId())) {
                            groupUserNote.setText(resp.obj.getUserList().get(i).getJoinUserName());
                        }
                    }
                    groupMemberSize.setText("全部群成员：" + resp.obj.getUserList().size() + "人");
                }
            }

            @Override
            public void onFailure(Throwable e) {

            }

            @Override
            public void onCancelled(Throwable e) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.sw_group_top:
                if (b) {
                    if (mGroup != null) {
                        OperationRong.setConversationTop(GroupDetailActivity.this, Conversation.ConversationType.GROUP, mGroup.getGroupId() + "", true);
                    }
                } else {
                    if (mGroup != null) {
                        OperationRong.setConversationTop(GroupDetailActivity.this, Conversation.ConversationType.GROUP, mGroup.getGroupId() + "", false);
                    }
                }
                break;
            case R.id.sw_group_notfaction:
                if (b) {
                    if (mGroup != null) {
                        OperationRong.setConverstionNotif(GroupDetailActivity.this, Conversation.ConversationType.GROUP, mGroup.getGroupId() + "", true);
                    }
                } else {
                    if (mGroup != null) {
                        OperationRong.setConverstionNotif(GroupDetailActivity.this, Conversation.ConversationType.GROUP, mGroup.getGroupId() + "", false);
                    }
                }

                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what==UPDATESUCCESS){
                    Glide.with(getActivity()).load(picurl).into(groupPic);
                }
            }
        };
    }

    private class GridAdapter extends BaseAdapter {
        private List<GroupMemberInfo.UserList> lists;
        private Context context;
        private LayoutInflater inflater;

        public GridAdapter(List<GroupMemberInfo.UserList> lists, Context context) {
            this.lists = lists;
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public Object getItem(int i) {
            return lists.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.social_chatsetting_gridview_item, null);
                holder.img_user_head = (SelectableRoundedImageView) view.findViewById(R.id.img_user_head);
                holder.tv_username = (TextView) view.findViewById(R.id.tv_username);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.tv_username.setText(lists.get(i).getJoinUserName());
            return view;
        }

        class ViewHolder {
            SelectableRoundedImageView img_user_head;
            TextView tv_username;
        }
    }

    private void getGroupInfo() {
        ReqGroup.getGroupInfo(this, getActivityTag(), groupId, new EntityCallBack<GroupResponse>() {
            @Override
            public Class<GroupResponse> getEntityClass() {
                return GroupResponse.class;
            }

            @Override
            public void onSuccess(GroupResponse resp) {
                if (resp.is200()) {
                    mGroup = resp.obj;
                    initSwichEvent();
                    if (AppApplication.getCurrentUserInfo().getUserId().equals(resp.obj.getGroupHostId() + "")) {
                        groupQuit.setVisibility(View.GONE);
                        groupDismiss.setVisibility(View.VISIBLE);
                        isCreater = true;
                    } else {
                        groupQuit.setVisibility(View.VISIBLE);
                        groupDismiss.setVisibility(View.GONE);
                        isCreater = false;
                    }
                    groupName.setText(resp.obj.getGroupName());
                    tvGroupName.setText(resp.obj.getGroupName());

                    tvGroupContent.setText("群公告：" + resp.obj.getGroupNote());
                    if (isCreater) {
                        llGroupEdit.setVisibility(View.VISIBLE);
                    } else {
                        llGroupEdit.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Throwable e) {

            }

            @Override
            public void onCancelled(Throwable e) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getActivity().finish();
    }

    private void getgroupurl(final String groupId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                try {

                    RequestBody requestBody = new FormBody.Builder()
                            .add("group_id", groupId)

                            .build();
                    Request request = new Request.Builder()
                            .url("http://st.3dgogo.com/index/chatgroup_gambit/get_chatgroup_image.html")
                            .post(requestBody)
                            .build();
                    try {
                        Call call = okHttpClient.newCall(request);
                        //判断请求是否成功
                        Looper.prepare();
                        try {
                            Response response = call.execute();

                            try {

                                JSONObject jsonObject = new JSONObject(response.body().string());

                                if (jsonObject.getInt("code") == 200) {

                                    picurl=jsonObject.getString("image");
                                    Message msg = new Message();
                                    msg.what = UPDATESUCCESS;
                                    handler.sendMessage(msg);

                                    //Toast.makeText(GroupDetailActivity.this, "恭喜上传成功", Toast.LENGTH_SHORT).show();
                                } else if (jsonObject.getInt("code") == 500) {

                                    Toast.makeText(GroupDetailActivity.this, "抱歉，修改失败！请再次提交", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();


                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Looper.loop();

                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getgroupurl(groupId);
    }
}
