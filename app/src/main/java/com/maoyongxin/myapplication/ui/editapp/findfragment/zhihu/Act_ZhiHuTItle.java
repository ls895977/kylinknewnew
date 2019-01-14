package com.maoyongxin.myapplication.ui.editapp.findfragment.zhihu;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.shanghuiInfo;
import com.maoyongxin.myapplication.ui.editapp.findfragment.zhihu.adapter.GrdivewAdater;
import com.maoyongxin.myapplication.ui.editapp.findfragment.zhihu.adapter.HeaderViewAdapter;
import com.maoyongxin.myapplication.ui.editapp.findfragment.zhihu.adapter.ZhiHuTitlePagerAdapter;
import com.maoyongxin.myapplication.ui.editapp.findfragment.zhihu.bean.MessageEvent;
import com.maoyongxin.myapplication.ui.editapp.findfragment.zhihu.bean.ZhiHuBean;
import com.maoyongxin.myapplication.ui.editapp.findfragment.zhihu.bean.ZhiHuGridBean;
import com.maoyongxin.myapplication.ui.editapp.findfragment.zhihu.dialog.Dialog_ZhiHuButoom;
import com.maoyongxin.myapplication.ui.editapp.findfragment.zhihu.view.SodukuGridView;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class Act_ZhiHuTItle extends FragmentActivity implements View.OnClickListener {
    private ImageView back, secher, liebiao;
    Dialog_ZhiHuButoom butoom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_zhihutitle);
        initView();
    }
    ViewPager viewPager;
    SlidingTabLayout tabLayout;
    protected void initView() {
        back = findViewById(R.id.zhihutitle_back);
        secher = findViewById(R.id.zhihutitle_secher);
        liebiao = findViewById(R.id.zhihutitle_liebiao);
        viewPager = findViewById(R.id.view_pager);
        tabLayout =findViewById(R.id.tab_layout);
        butoom = new Dialog_ZhiHuButoom(this);
        back.setOnClickListener(this);
        secher.setOnClickListener(this);
        liebiao.setOnClickListener(this);
        viewPager.setAdapter(new ZhiHuTitlePagerAdapter(getSupportFragmentManager()));
        tabLayout.setViewPager(viewPager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zhihutitle_back://返回
                finish();
                break;
            case R.id.zhihutitle_secher://搜索
                startActivity(new Intent(this, Act_ZhiHuTItleSeChar.class));
                break;
            case R.id.zhihutitle_liebiao://列表
                butoom.show();
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
