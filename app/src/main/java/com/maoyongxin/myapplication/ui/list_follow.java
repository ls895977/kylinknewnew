package com.maoyongxin.myapplication.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.FollowListInfo;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqGetFollowList;
import com.maoyongxin.myapplication.http.response.FollowListResponse;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.ui.adapter.recycle_myfollowlist;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class list_follow extends AppCompatActivity {

    @BindView(R.id.ll_bar)
    LinearLayout llBar;
    @BindView(R.id.tv_returnBack)
    TextView tvReturnBack;
    @BindView(R.id.rc_myLike_list)
    RecyclerView rcMyLikeList;
    private List<FollowListInfo.FollowList> followLists;
    private recycle_myfollowlist adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_follow);
        ButterKnife.bind(this);

        LinearLayoutManager mLinearLayoutManager=new LinearLayoutManager(list_follow.this, LinearLayoutManager.VERTICAL, false);
        rcMyLikeList.setLayoutManager(mLinearLayoutManager);
        rcMyLikeList.setItemAnimator(null);
        rcMyLikeList.setHasFixedSize(true);
        rcMyLikeList.setNestedScrollingEnabled(false);
        getMyFollowList();
    }

    private void getMyFollowList() {
        ReqGetFollowList.getList(this,  getClass().getSimpleName(), AppApplication.getCurrentUserInfo().getUserId(), new EntityCallBack<FollowListResponse>() {
            @Override
            public Class<FollowListResponse> getEntityClass() {
                return FollowListResponse.class;
            }

            @Override
            public void onSuccess(final FollowListResponse resp) {
                if (resp.is200()) {

                    followLists = resp.obj.getFollowList();

                    adapter = new recycle_myfollowlist(followLists, list_follow.this);

                    rcMyLikeList.setAdapter(adapter);


                } else {
                    //showToastShort(resp.msg);
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
