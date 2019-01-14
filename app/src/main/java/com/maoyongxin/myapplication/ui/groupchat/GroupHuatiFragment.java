package com.maoyongxin.myapplication.ui.groupchat;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.gson.Gson;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.HuatiInfo1;
import com.maoyongxin.myapplication.ui.adapter.HuatiRecycleAdapter;
import com.maoyongxin.myapplication.ui.fragment.HeaderViewPagerFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
public class GroupHuatiFragment extends HeaderViewPagerFragment implements OnRefreshListener, OnLoadMoreListener {
    private RecyclerView lv_huati;
    private String Groupid, Groupname, groupimg;
    SmartRefreshLayout refreshLayout;
    Gson gson = new Gson();
    List<HuatiInfo1.InfoBean.DataBean> datas = new ArrayList<>();
    private HuatiRecycleAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_chat_huati, container, false);
        refreshLayout = (SmartRefreshLayout) view.findViewById(R.id.refreshLayout);
        lv_huati = (RecyclerView) view.findViewById(R.id.lv_huati);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        lv_huati.setLayoutManager(mLinearLayoutManager);
//        lv_huati.setItemAnimator(null);
//        lv_huati.setHasFixedSize(true);
//        lv_huati.setNestedScrollingEnabled(false);
        initData();
        return view;
    }
    private void initData() {
        refreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));  //设置 Header 为 贝塞尔雷达 样式
        refreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale));//设置 Footer 为 球脉冲 样式
        refreshLayout.setEnableRefresh(true);//启用刷新
        refreshLayout.setEnableAutoLoadMore(true);//启用加载
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        Groupid = getActivity().getIntent().getStringExtra("groupNum");
        Groupname = getActivity().getIntent().getStringExtra("groupName");
        groupimg = getActivity().getIntent().getStringExtra("picUrl");
        getHuattiList(Groupid);
    }
    int page = 1;
    private void getHuattiList(String GroupId) {
        OkHttpUtils.post().addParams("page", page + "")
                .url("http://st.3dgogo.com/index/chatgroup_gambit/get_gambit/group_id/" + GroupId).build()
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
    @Override
    public void onRefresh(RefreshLayout refreshLayout1) {
        page=1;
        datas.clear();
        getHuattiList(Groupid);
        refreshLayout.finishRefresh();
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout1) {
        page++;
        getHuattiList(Groupid);
        refreshLayout.finishLoadmore();
    }
}
