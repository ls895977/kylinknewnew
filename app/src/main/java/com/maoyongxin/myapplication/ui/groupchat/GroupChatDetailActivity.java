package com.maoyongxin.myapplication.ui.groupchat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.indecator.ViewPagerIndicator;
import com.maoyongxin.myapplication.myapp.AppActivity;
import com.maoyongxin.myapplication.ui.editapp.findfragment.MyDiscoveryFragment;
import com.maoyongxin.myapplication.ui.huati.Fabu;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;

public class GroupChatDetailActivity extends AppActivity {

    @BindView(R.id.ll_bar)
    LinearLayout llBar;
    @BindView(R.id.tv_groupName)
    TextView tvGroupName;
   // @BindView(R.id.tv_personNum)
   // TextView tvPersonNum;
    @BindView(R.id.btn_jianjie)
    Button btnJianjie;
    @BindView(R.id.btn_huati)
    Button btnHuati;
    @BindView(R.id.btn_faxian)
    Button btnFaxian;
    @BindView(R.id.indicator_group)
    ViewPagerIndicator indicatorGroup;
    @BindView(R.id.vp_viewPager)
    ViewPager vpViewPager;
    @BindView(R.id.groupPic)
    ImageView groupPic;
   // @BindView(R.id.join_group)
   // TextView joinGroup;
  //  @BindView(R.id.line_groupDetail)
    FrameLayout lineGroupDetail;
    private String picUrl = "";


    @BindView(R.id.fbht)
    Button fbht;
    @BindView(R.id.jrql)
    Button jrql;


    private List<Button> btnList;
    private List<Fragment> fragList;
    private int prePosition = 2;
    private static final int UPDATE = 0;
    private int screenWidth;
    private int screenHight;

    @Override
    protected int bindLayout() {
        return R.layout.activity_group_chat_detail;
    }


    @Override
    protected void initData() {
        super.initData();

        picUrl=getIntent().getStringExtra("picUrl");
        fragList = new ArrayList<Fragment>();
        GroupJianjieFragment jianjieFragment = new GroupJianjieFragment();
        fragList.add(jianjieFragment);

        GroupHuatiFragment huatiFragment = new GroupHuatiFragment();
        fragList.add(huatiFragment);

        GroupFaxianFragment groupFaxianFragment = new GroupFaxianFragment();
        fragList.add(groupFaxianFragment);



    }

    @Override
    protected void initView() {
        super.initView();

        btnList = new ArrayList<Button>();
        btnList.add(btnJianjie);
        btnList.add(btnHuati);
        btnList.add(btnFaxian);
        llBar.setBackgroundColor(Color.parseColor("#7ECEF3"));
        Glide.with(getActivity()).load(picUrl).into(groupPic);
        showMessage();
        showFragment();


    }

    public void showMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                tvGroupName.setText(getIntent().getStringExtra("groupName"));
       //         tvPersonNum.setText(getIntent().getStringExtra("peopleNum"));
                vpViewPager.setCurrentItem(1);
            }
        }).start();


    }

    @OnClick({R.id.btn_jianjie, R.id.btn_huati, R.id.btn_faxian, R.id.jrql})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_jianjie:
                vpViewPager.setCurrentItem(0);
                break;
            case R.id.btn_huati:
                vpViewPager.setCurrentItem(1);
                break;
            case R.id.btn_faxian:
                vpViewPager.setCurrentItem(2);
                break;
            case R.id.jrql:
                RongIM.getInstance().startGroupChat(getActivity(), getIntent().getStringExtra("groupNum"), getIntent().getStringExtra("groupName"));

                break;
        }
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in,R.anim.slide_left_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation

        ButterKnife.bind(this);



    }

    @OnClick(R.id.fbht)
    public void onViewClicked() {
        Intent intent = new Intent(getActivity(), Fabu.class);
        intent.putExtra("GroupName", tvGroupName.getText());
     //   intent.putExtra("GroupId", tvPersonNum.getText());
        startActivity(intent);
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> list;

        public ViewPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return list.get(0);
            } else if (position == 1) {
                return list.get(1);
            } else {
                return list.get(2);
            }
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

    @Override
    public void onBackPressed() {



        getActivity().finishAfterTransition();

    }

    /**
     * 展示fragment
     */
    private void showFragment() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        final ViewPagerAdapter vpAdapter = new ViewPagerAdapter(fm, fragList);
        vpAdapter.notifyDataSetChanged();
        vpViewPager.setAdapter(vpAdapter);

        /**
         * 指示器联动
         */
        vpViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                indicatorGroup.scroll(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                btnList.get(prePosition).setTextColor(Color.parseColor("#ACACAC"));
                btnList.get(position).setTextColor(Color.parseColor("#949494"));
                prePosition = position;
                if (prePosition == 0) {
                    fragList.remove(2);
                    MyDiscoveryFragment myDiscoveryFragment = new MyDiscoveryFragment();
                    fragList.add(myDiscoveryFragment);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


}
