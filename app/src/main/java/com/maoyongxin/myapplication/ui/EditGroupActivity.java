package com.maoyongxin.myapplication.ui;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.jky.baselibrary.widget.TitleBar;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqGroup;
import com.maoyongxin.myapplication.http.response.BaseResp;
import com.maoyongxin.myapplication.http.response.GroupResponse;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.myapp.AppTitleBarActivity;
import com.maoyongxin.myapplication.server.widget.ClearWriteEditText;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditGroupActivity extends AppTitleBarActivity {


    @BindView(R.id.edit_groupName)
    EditText editGroupName;
    @BindView(R.id.edit_group_content)
    EditText editGroupContent;
    @BindView(R.id.btn_save_group)
    Button btnSaveGroup;
    @BindView(R.id.TitleBar_group)
    TitleBar TitleBarGroup;
    private String groupId;
    private boolean isCreater;

    @Override
    protected int bindLayout() {
        return R.layout.activity_edit_group;
    }

    @Override
    protected void initData() {
        super.initData();
        groupId = getIntent().getStringExtra("groupId");
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        btnSaveGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editGroupName.getText().equals("")) {
                    if (!editGroupContent.getText().equals("")) {
                        doSaveGroup();
                    } else {
                        showToastShort("请输入群公告");
                    }
                } else {
                    showToastShort("请输入群组名");
                }
            }
        });
    }

    private void doSaveGroup() {
        ReqGroup.updateGroup(this, getActivityTag(), AppApplication.getCurrentUserInfo().getUserId(), editGroupName.getText().toString(), editGroupContent.getText().toString(), groupId, new EntityCallBack<BaseResp>() {
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
                    editGroupName.setText(resp.obj.getGroupName());
                    editGroupContent.setText(resp.obj.getGroupNote());
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
