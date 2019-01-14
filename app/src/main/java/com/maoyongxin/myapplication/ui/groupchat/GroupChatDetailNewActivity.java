package com.maoyongxin.myapplication.ui.groupchat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
//import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.lzy.widget.tab.PagerSlidingTabStrip;
import com.maoyongxin.myapplication.R;

import com.maoyongxin.myapplication.server.widget.HeaderViewPager;

import com.maoyongxin.myapplication.server.widget.SelectableRoundedImageView;
import com.maoyongxin.myapplication.ui.fragment.HeaderViewPagerFragment;

import com.maoyongxin.myapplication.ui.huati.Fabu;
import com.maoyongxin.myapplication.ui.widget.BaseActivity;
import com.maoyongxin.myapplication.ui.widget.GroupMore;

import com.maoyongxin.myapplication.ui.widget.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.rockerhieu.emojicon.EmojiconTextView;
import io.rong.imkit.RongIM;

/**
 * Created by qiuzy on 2018/7/25.
 */

public class GroupChatDetailNewActivity extends BaseActivity {
    @BindView(R.id.fbht)
    Button fbht;
    @BindView(R.id.jrql)
    Button jrql;
    @BindView(R.id.tv_groupjianjie)
    TextView tvgroupjianjie;
    FrameLayout lineGroupDetail;
    private String groupName = "";
    private String picUrl = "";
    private String hostId = "", groupNote;
    public List<HeaderViewPagerFragment> fragments;
    private HeaderViewPager scrollableLayout;
    private ImageView pagerHeader, back, toMygroup;
    private View titleBar_Bg;
    private EmojiconTextView titleBar_title, tv_groupName;
    private View status_bar_fix;
    private View titleBar;
    private SelectableRoundedImageView roudimg_head;
    private CardView cardroudimg_head;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//白底黑字+沉浸式导航栏
        setContentView(R.layout.activity_group_chat_detail_new);
        ButterKnife.bind(this);
        picUrl = getIntent().getStringExtra("picUrl");
        groupName = getIntent().getStringExtra("groupName");
        hostId = getIntent().getStringExtra("hostId");
        groupNote = getIntent().getStringExtra("groupNote");
        fragments = new ArrayList<>();
        fragments.add(new GroupHuatiFragment());//话题列表，
        fragments.add(new GroupHuatiFragmentMine());//我的话题
        toMygroup = (ImageView) findViewById(R.id.toMygroup);
        tv_groupName = (EmojiconTextView) findViewById(R.id.tv_groupName);
        scrollableLayout = (HeaderViewPager) findViewById(R.id.scrollableLayout);
        titleBar = findViewById(R.id.titleBar);
        titleBar_Bg = titleBar.findViewById(R.id.bg);
        back = (ImageView) findViewById(R.id.back);
        //当状态栏透明后，内容布局会上移，这里使用一个和状态栏高度相同的view来修正内容区域
        status_bar_fix = titleBar.findViewById(R.id.status_bar_fix);
        status_bar_fix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.getStatusHeight(this)));
        titleBar_title = (EmojiconTextView) titleBar.findViewById(R.id.title);
        roudimg_head = (SelectableRoundedImageView) titleBar.findViewById(R.id.roudimg_head);
        cardroudimg_head = (CardView) titleBar.findViewById(R.id.cardroudimg_head);
        cardroudimg_head.setAlpha(0);
        titleBar_Bg.setAlpha(0);
        status_bar_fix.setAlpha(0);
        titleBar_title.setAlpha(0);
        pagerHeader = (ImageView) findViewById(R.id.pagerHeader);
        Glide.with(getApplication()).load(picUrl).into(pagerHeader);
        Glide.with(getApplication()).load(picUrl).into(roudimg_head);
        tv_groupName.setText(groupName);
        tvgroupjianjie.setText(groupNote);
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        ViewPager viewPager = (ViewPager) findViewById(R.id.vp_viewPager);
        viewPager.setAdapter(new ContentAdapter(getSupportFragmentManager()));
        tabs.setViewPager(viewPager);
        scrollableLayout.setCurrentScrollableContainer(fragments.get(0));
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                scrollableLayout.setCurrentScrollableContainer(fragments.get(position));
            }
        });
        scrollableLayout.setOnScrollListener(new HeaderViewPager.OnScrollListener() {
            @Override
            public void onScroll(int currentY, int maxY) {
                //让头部具有差速动画,如果不需要,可以不用设置
                pagerHeader.setTranslationY(currentY / 2);
                //动态改变标题栏的透明度,注意转化为浮点型
                float alpha = 1.0f * currentY / maxY;
                titleBar_Bg.setAlpha(alpha);
                //注意头部局的颜色也需要改变
                status_bar_fix.setAlpha(alpha);
                titleBar_title.setAlpha(alpha);
                cardroudimg_head.setAlpha(alpha);
                titleBar_title.setText(groupName);
                tv_groupName.setText(groupName);
            }
        });
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //当前窗口获取焦点时，才能正真拿到titlebar的高度，此时将需要固定的偏移量设置给scrollableLayout即可
        scrollableLayout.setTopOffset(titleBar.getHeight());
    }

    /**
     * 内容页的适配器
     */
    private class ContentAdapter extends FragmentPagerAdapter {
        public ContentAdapter(FragmentManager fm) {
            super(fm);
        }

        public String[] titles = new String[]{"热门话题", "我的"};

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    @OnClick(R.id.fbht)
    public void onViewClicked() {
        Intent intent = new Intent(getApplication(), Fabu.class);
        intent.putExtra("GroupName", "百乐交流群");
        intent.putExtra("groupId", getIntent().getStringExtra("groupNum"));
        startActivity(intent);
    }

    @OnClick({R.id.jrql})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.jrql:
                String Groupid = getIntent().getStringExtra("groupNum");
                String groupName = getIntent().getStringExtra("groupName");
                RongIM.getInstance().startGroupChat(this, Groupid, groupName);
                break;
            case R.id.toMygroup:
                GroupMore groupMore = new GroupMore(GroupChatDetailNewActivity.this);
                groupMore.showPopupWindow(toMygroup);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finishAfterTransition();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_left_out);
    }
}
