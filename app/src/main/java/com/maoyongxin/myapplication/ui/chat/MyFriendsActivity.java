package com.maoyongxin.myapplication.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.myapp.AppActivity;
import com.maoyongxin.myapplication.ui.GroupListActivity;
import com.maoyongxin.myapplication.ui.fragment.ContactsFragment;


import butterknife.BindView;
import butterknife.ButterKnife;

public class MyFriendsActivity extends AppActivity {

    @BindView(R.id.tv_returnBack)
    TextView tvReturnBack;
    @BindView(R.id.img_friendMessage)
    TextView imgFriendMessage;
    @BindView(R.id.toMygroup)
    LinearLayout toMygroup;

    @Override
    protected int bindLayout() {
        return R.layout.activity_my_friends;
    }

    @Override
    protected void initView() {
        super.initView();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//白底黑字+沉浸式导航栏
        addFragment(new ContactsFragment());
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        imgFriendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyFriendsActivity.this, FriendMessagesActivity.class));
            }
        });
        tvReturnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toMygroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), GroupListActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void addFragment(Fragment ft) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ftraTransaction = fm.beginTransaction();
        if (ft != null) {
            for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
                fm.popBackStack();
            }
        }
        ftraTransaction.addToBackStack(null);
        ftraTransaction.add(R.id.fram_friendsList, ft);
        ftraTransaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
