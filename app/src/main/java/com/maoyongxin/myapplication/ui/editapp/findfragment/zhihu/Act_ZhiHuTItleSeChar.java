package com.maoyongxin.myapplication.ui.editapp.findfragment.zhihu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.ui.editapp.findfragment.zhihu.adapter.GrdivewAdater;
import com.maoyongxin.myapplication.ui.editapp.findfragment.zhihu.adapter.HeaderViewAdapter;
import com.maoyongxin.myapplication.ui.editapp.findfragment.zhihu.bean.ZhiHuBean;
import com.maoyongxin.myapplication.ui.editapp.findfragment.zhihu.bean.ZhiHuGridBean;
import com.maoyongxin.myapplication.ui.editapp.findfragment.zhihu.dialog.Dialog_ZhiHuButoom;
import com.maoyongxin.myapplication.ui.editapp.findfragment.zhihu.view.SodukuGridView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class Act_ZhiHuTItleSeChar extends Activity implements View.OnClickListener, OnRefreshListener, OnLoadMoreListener {
    private ImageView back;
    private RecyclerView myRecyclerView;
    SmartRefreshLayout refreshLayout;
    List<ZhiHuBean.InfoBean.DataBean> data = new ArrayList<>();
    HeaderViewAdapter adapter;
    private Gson gson = new Gson();
    private EditText sechaertv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_zhihutitlesechar);
        initView();
    }

    protected void initView() {

        back = findViewById(R.id.zhihutitle_back);
        back.setOnClickListener(this);
        myRecyclerView = findViewById(R.id.zhihutitle_myRecyclerview);
        refreshLayout = findViewById(R.id.refreshLayout);
        sechaertv = findViewById(R.id.zhihu_sechar);
//        refreshLayout.setRefreshHeader(new MaterialHeader(this).setShowBezierWave(true));
//        refreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        myRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        sechaertv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(sechaertv.getText().toString().length()>0){
                    postsecharViewData(sechaertv.getText().toString());
                }else {
                    postsecharViewData("gg");
                }
            }
        });
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
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) { //刷新完成
        refreshLayout.finishRefresh();
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {   //加载完成
        refreshLayout.finishLoadMore();
//                refreshLayout.finishLoadMoreWithNoMoreData();  //全部加载完成,没有数据了调用此方法
    }

    public void postsecharViewData(String search) {
        data.clear();
        OkHttpUtils.post().url("http://st.3dgogo.com/index/user_resource/searchUserResourceApi.html")
                .addParams("search", search)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                ZhiHuBean gridBean = gson.fromJson(response, ZhiHuBean.class);
                for (int i = 0; i < gridBean.getInfo().getData().size(); i++) {
                    ZhiHuBean.InfoBean.DataBean bean = gridBean.getInfo().getData().get(i);
                    data.add(bean);
                }
                if (adapter == null) {
                    adapter = new HeaderViewAdapter(Act_ZhiHuTItleSeChar.this);
                    adapter.setNewData(data);
                    adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                            Log.e("aa", "----主item-------" + position);
                        }
                    });
                    myRecyclerView.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}
