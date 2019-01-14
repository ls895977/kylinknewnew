package com.maoyongxin.myapplication.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqAddFriends;
import com.maoyongxin.myapplication.http.request.ReqDeleteFriend;
import com.maoyongxin.myapplication.http.request.ReqFindUserById;
import com.maoyongxin.myapplication.http.request.ReqUpdateUserName;
import com.maoyongxin.myapplication.http.response.BaseResp;
import com.maoyongxin.myapplication.http.response.LoginResponse;
import com.maoyongxin.myapplication.myapp.AppActivity;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.server.widget.SelectableRoundedImageView;

import butterknife.BindView;
import io.rong.imkit.RongIM;

public class UserDetailActivity extends AppActivity {


    @BindView(R.id.ac_iv_user_portrait)
    SelectableRoundedImageView acIvUserPortrait;
    @BindView(R.id.contact_top)
    TextView contactTop;
    @BindView(R.id.contact_phone)
    TextView contactPhone;
    @BindView(R.id.contact_below)
    TextView contactBelow;
    @BindView(R.id.user_online_status)
    TextView userOnlineStatus;
    @BindView(R.id.group_info)
    LinearLayout groupInfo;
    @BindView(R.id.ac_ll_note_name)
    LinearLayout acLlNoteName;
    @BindView(R.id.ac_ll_chat_button_group)
    LinearLayout acLlChatButtonGroup;
    @BindView(R.id.ac_bt_add_friend)
    Button acBtAddFriend;
    @BindView(R.id.ll_bar)
    LinearLayout llBar;
    @BindView(R.id.btn_chat)
    Button btnChat;
    @BindView(R.id.btn_phone)
    Button btnPhone;
    @BindView(R.id.btn_chat_video)
    Button btnChatVideo;
    @BindView(R.id.tv_myFriendNoteName)
    TextView tvMyFriendNoteName;
    @BindView(R.id.btn_delete_friend)
    Button btnDeleteFriend;
    private String userId;
    private String userName;
    private String headImg;
    private final static String FROM_ADD_FRIENDS = "add_friends";
    private final static String FROM_MY_FRIENDS = "my_friends";

    @Override
    protected int bindLayout() {
        return R.layout.activity_user_detail;
    }

    @Override
    protected void initData() {
        super.initData();
        userId = getIntent().getStringExtra("userId");

        searchUser();
    }
    private void searchUser(){
        ReqFindUserById.findUser(UserDetailActivity.this, getActivityTag(),userId, new EntityCallBack<LoginResponse>() {
            @Override
            public Class<LoginResponse> getEntityClass() {
                return LoginResponse.class;
            }

            @Override
            public void onSuccess(LoginResponse resp) {
                if (resp.is200()) {
                    userName=resp.obj.getUserName();
                    headImg=resp.obj.getHeadImg();
                    contactPhone.setText(userId);
                    contactTop.setText(userName);
                    if (getIntent().getStringExtra("inType")==null||getIntent().getStringExtra("inType").equals(FROM_ADD_FRIENDS)) {//来自于加好友或者空
                        acLlNoteName.setVisibility(View.GONE);
                        acLlChatButtonGroup.setVisibility(View.VISIBLE);
                        acBtAddFriend.setVisibility(View.VISIBLE);
                        btnDeleteFriend.setVisibility(View.GONE);
                    } else {
                        acLlNoteName.setVisibility(View.VISIBLE);
                        acLlChatButtonGroup.setVisibility(View.VISIBLE);
                        acBtAddFriend.setVisibility(View.GONE);
                        btnDeleteFriend.setVisibility(View.VISIBLE);
                    }
                    if (headImg.equals(R.mipmap.user_head_img + "")) {
                        Glide.with(UserDetailActivity.this).load(Integer.parseInt(headImg)).into(acIvUserPortrait);
                    } else {
                        Glide.with(UserDetailActivity.this).load(headImg).into(acIvUserPortrait);
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
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        acIvUserPortrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserDetailActivity.this,StrangerDetailActivity.class);
                intent.putExtra("personId",userId);
                startActivity(intent);
            }
        });
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RongIM.getInstance().startPrivateChat(UserDetailActivity.this, userId, userName);
            }
        });
        btnDeleteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReqDeleteFriend.deleteFriend(UserDetailActivity.this, getActivityTag(), AppApplication.getCurrentUserInfo().getUserId(), userId, new EntityCallBack<BaseResp>() {
                    @Override
                    public Class<BaseResp> getEntityClass() {
                        return BaseResp.class;
                    }

                    @Override
                    public void onSuccess(BaseResp resp) {
                        if (resp.is200()) {
                            showToastShort(resp.msg);
                            finish();
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
        acLlNoteName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText inputServer = new EditText(UserDetailActivity.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(UserDetailActivity.this);
                builder.setTitle("请输入备注").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                        .setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ReqUpdateUserName.updateUserName(UserDetailActivity.this, getActivityTag(), AppApplication.getCurrentUserInfo().getUserId(), userId, inputServer.getText().toString(), new EntityCallBack<BaseResp>() {
                            @Override
                            public Class<BaseResp> getEntityClass() {
                                return BaseResp.class;
                            }

                            @Override
                            public void onSuccess(BaseResp resp) {
                                showToastShort(resp.msg);
                                tvMyFriendNoteName.setText("修改备注：" + inputServer.getText().toString());
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

        acBtAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText inputServer = new EditText(UserDetailActivity.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(UserDetailActivity.this);
                builder.setTitle("说句话呗").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                        .setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        ReqAddFriends.addFriends(UserDetailActivity.this, getActivityTag(), AppApplication.getCurrentUserInfo().getUserId(), userId, inputServer.getText().toString(), new EntityCallBack<BaseResp>() {
                            @Override
                            public Class<BaseResp> getEntityClass() {
                                return BaseResp.class;
                            }

                            @Override
                            public void onSuccess(BaseResp resp) {
                                if (resp.is200()) {
                                    showToastShort("好友请求发送成功,请等待回复");
                                    finish();
                                } else {
                                    showToastShort(resp.msg);
                                    finish();
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
    }
}
