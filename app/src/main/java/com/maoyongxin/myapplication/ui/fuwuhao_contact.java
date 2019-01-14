package com.maoyongxin.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqCommunity;
import com.maoyongxin.myapplication.http.response.CommunityUsersResponse;
import com.maoyongxin.myapplication.myapp.AppActivity;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.ui.adapter.CommunityPersonListAdapter;
import com.maoyongxin.myapplication.ui.adapter.fuwuhao_ListAdapter;

import butterknife.BindView;

public class fuwuhao_contact extends AppActivity {


    @BindView(R.id.lv_comPersonList)
    ListView lvComPersonList;

    private fuwuhao_ListAdapter adapter;
    private String communityId, targetUserId;

    @Override
    protected int bindLayout() {
        return (R.layout.activity_fuwuhao_contact);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//白底黑字+沉浸式导航栏


    }

    @Override
    protected void initData() {
        //super.initData();

    }

    @Override
    protected void initView() {
        communityId = getIntent().getStringExtra("communityId");
        targetUserId = getIntent().getStringExtra("targetUserId");
        getMyCommunityPerson();
    }


    private void getMyCommunityPerson() {

        ReqCommunity.getMyCommunityPersons(this, getActivityTag(), targetUserId, communityId, new EntityCallBack<CommunityUsersResponse>() {
            @Override
            public Class<CommunityUsersResponse> getEntityClass() {
                return CommunityUsersResponse.class;
            }

            @Override
            public void onSuccess(final CommunityUsersResponse resp) {

                if (resp.is200()) {
                    if (resp.obj.getSuperManagerUserId().equals(AppApplication.getCurrentUserInfo().getUserId())) {//超管

                    } else {
                        if (resp.obj.getManagerUserId().size() != 0) {
                            for (int i = 0; i < resp.obj.getManagerUserId().size(); i++) {
                                if (resp.obj.getManagerUserId().get(i).equals(AppApplication.getCurrentUserInfo().getUserId())) {

                                    break;
                                } else {

                                }
                            }
                        } else {

                        }
                    }
                    adapter = new fuwuhao_ListAdapter(communityId, resp.obj.getSuperManagerUserId(), resp.obj.getManagerUserId(), resp.obj.getUserList(), getActivity(), false);
                    lvComPersonList.setAdapter(adapter);
                    adapter.setOnRefreshListener(new fuwuhao_ListAdapter.OnRefreshListener() {
                        @Override
                        public void refresh() {
                            getMyCommunityPerson();
                        }
                    });
                    lvComPersonList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                    });
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
