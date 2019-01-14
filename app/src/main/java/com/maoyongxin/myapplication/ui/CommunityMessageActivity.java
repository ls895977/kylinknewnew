package com.maoyongxin.myapplication.ui;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jky.baselibrary.widget.TitleBar;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqCommunity;
import com.maoyongxin.myapplication.http.response.CommunityJoinResponse;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.myapp.AppTitleBarActivity;
import com.maoyongxin.myapplication.ui.adapter.CommunityRequestListAdapter;

import butterknife.BindView;

public class CommunityMessageActivity extends AppTitleBarActivity {

    @BindView(R.id.TitleBar_CMD)
    TitleBar TitleBarCMD;
    @BindView(R.id.tv_noManagerRequest)
    TextView tvNoManagerRequest;
    @BindView(R.id.lv_ManagerMessage)
    ListView lvManagerMessage;
    @BindView(R.id.line_managerRequest)
    LinearLayout lineManagerRequest;
    @BindView(R.id.tv_noJoinRequest)
    TextView tvNoJoinRequest;
    @BindView(R.id.lv_joinMessage)
    ListView lvJoinMessage;
    @BindView(R.id.line_Request)
    LinearLayout lineRequest;
    private boolean isSuper;
    private String communityId;

    @Override
    protected int bindLayout() {
        return R.layout.activity_community_message;
    }

    @Override
    protected void initData() {
        super.initData();
        communityId = getIntent().getStringExtra("communityId");
        if (getIntent().getStringExtra("type").equals("superManager")) {
            isSuper = true;
        } else if (getIntent().getStringExtra("type").equals("manager")) {
            isSuper = false;
        }
    }

    @Override
    protected void handlerPassMsg(int target, int target1, Object obj) {

    }

    @Override
    protected void initView() {
        super.initView();
        if (isSuper) {
            lineManagerRequest.setVisibility(View.VISIBLE);
        } else {
            lineManagerRequest.setVisibility(View.GONE);
        }
    }

    @Override
    protected void doBusiness() {
        super.doBusiness();
        if (isSuper) {
            getSuperRequest();
        }
        getJoinRequest();
    }

    private void getSuperRequest() {
        ReqCommunity.getSuperRequest(this, getActivityTag(), AppApplication.getCurrentUserInfo().getUserId(), communityId, new EntityCallBack<CommunityJoinResponse>() {
            @Override
            public Class<CommunityJoinResponse> getEntityClass() {
                return CommunityJoinResponse.class;
            }

            @Override
            public void onSuccess(CommunityJoinResponse resp) {
                if (resp.is200()) {
                    if (resp.obj.getRequestList().size() == 0) {
                        tvNoManagerRequest.setVisibility(View.VISIBLE);
                        lvManagerMessage.setVisibility(View.GONE);
                    } else {
                        tvNoManagerRequest.setVisibility(View.GONE);
                        lvManagerMessage.setVisibility(View.VISIBLE);
                    }
                    CommunityRequestListAdapter superAdapter = new CommunityRequestListAdapter(resp.obj.getRequestList(), CommunityMessageActivity.this, false);
                    superAdapter.setOnRefreshListener(new CommunityRequestListAdapter.OnRefreshListener() {
                        @Override
                        public void refresh() {
                            getSuperRequest();
                        }
                    });
                    lvManagerMessage.setAdapter(superAdapter);
                } else {
                    showToastShort(resp.msg);
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

    private void getJoinRequest() {
        ReqCommunity.getAskRequest(this, getActivityTag(), AppApplication.getCurrentUserInfo().getUserId(), communityId, new EntityCallBack<CommunityJoinResponse>() {
            @Override
            public Class<CommunityJoinResponse> getEntityClass() {
                return CommunityJoinResponse.class;
            }

            @Override
            public void onSuccess(CommunityJoinResponse resp) {
                if (resp.is200()) {
                    if (resp.obj.getRequestList().size() == 0) {
                        tvNoJoinRequest.setVisibility(View.VISIBLE);
                        lvJoinMessage.setVisibility(View.GONE);
                    } else {
                        tvNoJoinRequest.setVisibility(View.GONE);
                        lvJoinMessage.setVisibility(View.VISIBLE);
                    }
                    CommunityRequestListAdapter joinAdapter = new CommunityRequestListAdapter(resp.obj.getRequestList(), CommunityMessageActivity.this, true);
                    joinAdapter.setOnRefreshListener(new CommunityRequestListAdapter.OnRefreshListener() {
                        @Override
                        public void refresh() {
                            getJoinRequest();
                        }
                    });
                    lvJoinMessage.setAdapter(joinAdapter);
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
