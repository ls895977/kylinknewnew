package com.maoyongxin.myapplication.ui.groupchat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.HuatiInfo;
import com.maoyongxin.myapplication.entity.HuatiInfo1;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.server.widget.SelectableRoundedImageView;
import com.maoyongxin.myapplication.ui.adapter.HuatiRecycle;
import com.maoyongxin.myapplication.ui.adapter.HuatiRecycleAdapter;
import com.maoyongxin.myapplication.ui.editapp.findfragment.EndLessOnScrollListener;
import com.maoyongxin.myapplication.ui.editapp.findfragment.New_comment;
import com.maoyongxin.myapplication.ui.fragment.HeaderViewPagerFragment;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.header.WaveSwipeHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 我的
 */
public class GroupHuatiFragmentMine extends HeaderViewPagerFragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView lv_huati;
    private static String Groupid, Groupname, groupimg;
    private SmartRefreshLayout swipefreshlayout;
    private HuatiRecycleAdapter adapter;
    SmartRefreshLayout refreshLayout;

    public static GroupHuatiFragment newInstance() {
        return new GroupHuatiFragment();
    }

    @Override
    public void onRefresh() {
        Toast.makeText(getActivity(), "刷新中", Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_chat_huati, container, false);
        swipefreshlayout = (SmartRefreshLayout) view.findViewById(R.id.swipefreshlayout1111);
        refreshLayout = (SmartRefreshLayout) view.findViewById(R.id.refreshLayout);
        lv_huati = (RecyclerView) view.findViewById(R.id.lv_huati);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        lv_huati.setLayoutManager(mLinearLayoutManager);
        lv_huati.setItemAnimator(null);
        lv_huati.setHasFixedSize(true);
        lv_huati.setNestedScrollingEnabled(false);
        initView();
        initData();
        return view;
    }

    private void initView() {
        refreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        refreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale));
        refreshLayout.setEnableRefresh(true);//启用刷新
        refreshLayout.setEnableAutoLoadMore(true);//启用加载
        refreshLayout.setEnableAutoLoadMore(true);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 1;
                datas.clear();
                getHuattiList(Groupid);
                refreshlayout.finishRefresh();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                Log.e("aa","---onLoadMore------");
                page++;
                getHuattiList(Groupid);
                refreshlayout.finishLoadMore();
            }
        });
    }
    private void initData() {
        Groupid = getActivity().getIntent().getStringExtra("groupNum");
        Groupname = getActivity().getIntent().getStringExtra("groupName");
        groupimg = getActivity().getIntent().getStringExtra("picUrl");
        getHuattiList(Groupid);
    }
    int page = 1;
    Gson gson = new Gson();
    List<HuatiInfo1.InfoBean.DataBean> datas = new ArrayList<>();
    private void getHuattiList(String GroupId) {
        OkHttpUtils.post()
                .addParams("page", page + "")
                .addParams("group_id",GroupId)
                .addParams("uid", AppApplication.getCurrentUserInfo().getUserId())
                .url("http://st.3dgogo.com/index/chatgroup_gambit/getUidGambitList/").build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        HuatiInfo1 huatiInfo1 = gson.fromJson(response, HuatiInfo1.class);
                        if (huatiInfo1.getCode() != 200) {
                            return;
                        }
                        for (int i = 0; i < huatiInfo1.getInfo().getData().size(); i++) {
                                datas.add(huatiInfo1.getInfo().getData().get(i));
                        }
                        if (adapter == null) {
                            adapter = new HuatiRecycleAdapter(datas, getContext(), Groupname, Groupid, groupimg);
                            lv_huati.setAdapter(adapter);
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public View getScrollableView() {
        return lv_huati;
    }
}
