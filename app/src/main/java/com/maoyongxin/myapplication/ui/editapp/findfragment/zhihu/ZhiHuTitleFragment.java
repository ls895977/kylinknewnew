package com.maoyongxin.myapplication.ui.editapp.findfragment.zhihu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.ui.editapp.findfragment.zhihu.adapter.GrdivewAdater;
import com.maoyongxin.myapplication.ui.editapp.findfragment.zhihu.adapter.HeaderViewAdapter;
import com.maoyongxin.myapplication.ui.editapp.findfragment.zhihu.base.ViewPagerFragment;
import com.maoyongxin.myapplication.ui.editapp.findfragment.zhihu.bean.MessageEvent;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class ZhiHuTitleFragment extends ViewPagerFragment implements AdapterView.OnItemClickListener, OnRefreshListener, OnLoadMoreListener {
    private SodukuGridView myGridview;
    GrdivewAdater grdivewAdater;
    List<ZhiHuGridBean.InfoBean> gridData = new ArrayList<>();
    private RecyclerView myRecyclerView;
    SmartRefreshLayout refreshLayout;
    List<ZhiHuBean.InfoBean.DataBean> data = new ArrayList<>();
    HeaderViewAdapter adapter;
    private TextView tv_Service_Number, tv_Personal;
    private Gson gson = new Gson();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            data.clear();
            EventBus.getDefault().register(this);
            rootView = inflater.inflate(R.layout.fgt_zhihutitle, container, false);
            myGridview = rootView.findViewById(R.id.my_Gridview);
            grdivewAdater = new GrdivewAdater(getContext(), gridData);
            myGridview.setAdapter(grdivewAdater);
            myGridview.setOnItemClickListener(this);
            postGridViewData();
            init(rootView);
        }
        return rootView;
    }

    public void init(View rootView) {
        isCommunity = getArguments().getString("type");
        myRecyclerView = rootView.findViewById(R.id.zhihutitle_myRecyclerview);
        refreshLayout = rootView.findViewById(R.id.refreshLayout);
//        refreshLayout.setRefreshHeader(new MaterialHeader(this).setShowBezierWave(true));
//        refreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        myRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        postListViewData(isCommunity, classify_id);
    }

    public void postGridViewData() {
        OkHttpUtils.post().url("http://st.3dgogo.com/index/classify/getClassifyApi.html").build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                ZhiHuGridBean gridBean = gson.fromJson(response, ZhiHuGridBean.class);
                for (int i = 0; i < gridBean.getInfo().size(); i++) {
                    ZhiHuGridBean.InfoBean bean = gridBean.getInfo().get(i);
                    if (i == 0) {
                        bean.setIsof(true);
                    }
                    gridData.add(bean);
                }
                grdivewAdater.notifyDataSetChanged();
            }
        });
    }

    int position1 = 0;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        data.clear();
        gridData.get(position1).setIsof(false);
        gridData.get(position).setIsof(true);
        position1 = position;
        grdivewAdater.notifyDataSetChanged();
        classify_id = gridData.get(position).getId();
        postListViewData(isCommunity, classify_id);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXX(MessageEvent messageEvent) {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {   //加载完成
        page++;
        postListViewData(isCommunity, classify_id);
        refreshLayout.finishLoadMore();
        //                refreshLayout.finishLoadMoreWithNoMoreData();  //全部加载完成,没有数据了调用此方法
    }


    @Override
    public void onRefresh(RefreshLayout refreshLayout) { //刷新完成
        page=1;
        data.clear();
        postListViewData(isCommunity, classify_id);
        refreshLayout.finishRefresh();
    }

    private String isCommunity = "0", classify_id = "1";
    private int page=1;
    public void postListViewData(String isCommunity, String classify_id) {
        OkHttpUtils.post().url("http://st.3dgogo.com/index/user_resource/getUserResourceListApi.html")
                .addParams("isCommunity", isCommunity)
                .addParams("classify_id", classify_id)
                .addParams("page",page+"")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                ZhiHuBean gridBean = gson.fromJson(response, ZhiHuBean.class);
                if(gridBean.getCode()!=200){
                    return;
                }
                for (int i = 0; i < gridBean.getInfo().getData().size(); i++) {
                    ZhiHuBean.InfoBean.DataBean bean = gridBean.getInfo().getData().get(i);
                    data.add(bean);
                }
                if (adapter == null) {
                    adapter = new HeaderViewAdapter(getContext());
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
