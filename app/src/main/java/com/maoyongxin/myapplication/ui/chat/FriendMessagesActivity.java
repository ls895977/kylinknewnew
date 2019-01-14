package com.maoyongxin.myapplication.ui.chat;

import android.view.View;
import android.widget.TextView;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.RequestListInfo;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqGetRequestList;
import com.maoyongxin.myapplication.http.response.RequestListResponse;
import com.maoyongxin.myapplication.myapp.AppActivity;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.ui.adapter.RequestListAdapter;
import com.maoyongxin.myapplication.ui.widget.mylistview.LoadListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class FriendMessagesActivity extends AppActivity {


    @BindView(R.id.tv_returnBack)
    TextView tvReturnBack;
    @BindView(R.id.lv_friendsMsg)
    LoadListView lvFriendsMsg;

    @Override
    protected int bindLayout() {
        return R.layout.activity_friend_messages;
    }

    @Override
    protected void initView() {
        super.initView();
        getRequestList();
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        tvReturnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getRequestList() {
        ReqGetRequestList.getList(this, getActivityTag(), AppApplication.getCurrentUserInfo().getUserId(), new EntityCallBack<RequestListResponse>() {
            @Override
            public Class<RequestListResponse> getEntityClass() {
                return RequestListResponse.class;
            }

            @Override
            public void onSuccess(RequestListResponse resp) {
                if (resp.is200()) {
                    List<RequestListInfo.RequestList> requestListInfos = new ArrayList<RequestListInfo.RequestList>();
                    requestListInfos = resp.obj.getRequestList();
                    if (requestListInfos.size() != 0) {
                        RequestListAdapter adapter = new RequestListAdapter(FriendMessagesActivity.this, requestListInfos);
                        lvFriendsMsg.setAdapter(adapter);
                        lvFriendsMsg.setVisibility(View.VISIBLE);
                        adapter.setOnFreshListener(new RequestListAdapter.OnFreshListener() {
                            @Override
                            public void fresh() {
                                getRequestList();
                            }
                        });
                        lvFriendsMsg.setReflashInterface(new LoadListView.RLoadListener() {
                            @Override
                            public void onRefresh() {
                                getRequestList();
                            }
                        });
                        lvFriendsMsg.setInterface(new LoadListView.ILoadListener() {
                            @Override
                            public void onLoad() {
                                return;
                            }
                        });
                    } else {
                        lvFriendsMsg.setVisibility(View.GONE);
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
}
