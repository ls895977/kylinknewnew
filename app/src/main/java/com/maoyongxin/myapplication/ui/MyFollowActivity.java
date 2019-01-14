package com.maoyongxin.myapplication.ui;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.FollowListInfo;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqGetFollowList;
import com.maoyongxin.myapplication.http.response.FollowListResponse;
import com.maoyongxin.myapplication.myapp.AppActivity;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.ui.adapter.FollowListAdapter;
import com.maoyongxin.myapplication.ui.adapter.recycle_myfollowlist;
import com.maoyongxin.myapplication.ui.widget.mylistview.LoadListView;

import java.util.List;

import butterknife.BindView;

public class MyFollowActivity extends AppActivity {
    @BindView(R.id.lv_myLike_list)
    RecyclerView lvMyLikeList;
    @BindView(R.id.tv_returnBack)
    TextView tvReturnBack;
    private List<FollowListInfo.FollowList> followLists;
    private recycle_myfollowlist adapter;

    @Override
    protected int bindLayout() {
        return R.layout.activity_my_follow;
    }

    @Override
    protected void initView() {
      //  super.initView();

    }



    @Override
    protected void doBusiness() {

    }

    @Override
    protected void initEvent() {
        super.initEvent();
        tvReturnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getMyFollowList() {
        ReqGetFollowList.getList(this, getActivityTag(), AppApplication.getCurrentUserInfo().getUserId(), new EntityCallBack<FollowListResponse>() {
            @Override
            public Class<FollowListResponse> getEntityClass() {
                return FollowListResponse.class;
            }

            @Override
            public void onSuccess(final FollowListResponse resp) {
                if (resp.is200()) {

                    followLists = resp.obj.getFollowList();

                    adapter = new recycle_myfollowlist(followLists, MyFollowActivity.this);

                    lvMyLikeList.setAdapter(adapter);


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

}
