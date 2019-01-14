package com.maoyongxin.myapplication.ui;

import android.widget.ListView;

import com.jky.baselibrary.widget.TitleBar;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.GroupMessageInfo;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqGroup;
import com.maoyongxin.myapplication.http.response.GetGroupMessageResponse;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.myapp.AppTitleBarActivity;
import com.maoyongxin.myapplication.ui.adapter.GroupMessageAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class GroupMessageActivity extends AppTitleBarActivity {

    @BindView(R.id.TitleBar_GPMSG)
    TitleBar TitleBarGPMSG;
    @BindView(R.id.lv_groupMessage)
    ListView lvGroupMessage;
    private String groupId;
    private List<GroupMessageInfo.UserList> messages;
    private GroupMessageAdapter adapter;

    @Override
    protected int bindLayout() {
        return R.layout.activity_group_message;
    }

    @Override
    protected void initData() {
        super.initData();
        groupId = getIntent().getStringExtra("groupId");
        messages = new ArrayList<>();
    }

    @Override
    protected void handlerPassMsg(int target, int target1, Object obj) {

    }

    @Override
    protected void initView() {
        super.initView();
        getGroupMessage();
    }

    private void getGroupMessage() {
        ReqGroup.getGroupMessages(this, getActivityTag(), AppApplication.getCurrentUserInfo().getUserId(), new EntityCallBack<GetGroupMessageResponse>() {
            @Override
            public Class<GetGroupMessageResponse> getEntityClass() {
                return GetGroupMessageResponse.class;
            }

            @Override
            public void onSuccess(GetGroupMessageResponse resp) {
                if (resp.is200()) {
                    for (int i = 0; i < resp.obj.getUserList().size(); i++) {
                        String addGroupId = resp.obj.getUserList().get(i).getGroupId() + "";
                        if (addGroupId.equals(groupId)) {
                            GroupMessageInfo.UserList message = resp.obj.getUserList().get(i);
                            messages.add(message);
                        }
                    }
                    adapter = new GroupMessageAdapter(GroupMessageActivity.this, messages);
                    adapter.setOnfreshListener(new GroupMessageAdapter.OnfreshListener() {
                        @Override
                        public void fresh() {
                            getGroupMessage();
                        }
                    });
                    lvGroupMessage.setAdapter(adapter);

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
}
