package com.maoyongxin.myapplication.ui.groupchat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.ui.CommentDialogFragment;
import com.maoyongxin.myapplication.ui.EventMessage;
import com.maoyongxin.myapplication.ui.adapter.Secend_HuatihuifuRecycleAdapter;
import com.maoyongxin.myapplication.ui.bean.HuatihuifuRecyclePinlun;
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

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class SecendPinlunFragment extends HeaderViewPagerFragment implements Secend_HuatihuifuRecycleAdapter.LongClickListener, OnRefreshListener, OnLoadMoreListener {
    private RecyclerView lv_huati;
    SmartRefreshLayout refreshLayout;
    private String gamitId, groupId, parentUserId, parentRespId, parentRespIDCode;
    private CommentDialogFragment dialogFragment;
    List<HuatihuifuRecyclePinlun.InfoBean.DataBeanX> datas = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pinlun, container, false);
        EventBus.getDefault().register(this);
        refreshLayout = (SmartRefreshLayout) view.findViewById(R.id.refreshLayout);
        lv_huati = (RecyclerView) view.findViewById(R.id.lv_huati);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        lv_huati.setLayoutManager(mLinearLayoutManager);
        initView();
        initData();
        return view;
    }
    private void initView() {
        gamitId = getActivity().getIntent().getStringExtra("gambit_id");
        groupId = getActivity().getIntent().getStringExtra("GroupId");
        parentUserId = getActivity().getIntent().getStringExtra("parentUserId");
        parentRespId = getActivity().getIntent().getStringExtra("parentRespId");
        parentRespIDCode = getActivity().getIntent().getStringExtra("parentRespId");
        refreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));  //设置 Header 为 贝塞尔雷达 样式
        refreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale));//设置 Footer 为 球脉冲 样式
        refreshLayout.setEnableRefresh(true);//启用刷新
        refreshLayout.setEnableAutoLoadMore(true);//启用加载
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
    }
    private void initData() {
        gethuatiList(gamitId, parentRespIDCode);
    }

    @Subscribe
    public void onEventMainThread(EventMessage eventMessage) {
        Log.e("aa","--SecendPinlunFragment-----onEventMainThread----"+eventMessage.getType());
        switch (eventMessage.getType()) {
            case 10:
                datas.clear();
                page = 1;
                gethuatiList(gamitId, parentRespIDCode);
                break;
        }
    }

    @Override
    public View getScrollableView() {
        return lv_huati;
    }

    Gson gson = new Gson();
    private Secend_HuatihuifuRecycleAdapter adapter;
    int page = 1;

    private void gethuatiList(final String gamitId, final String parentRespId) {
        OkHttpUtils.post()
                .addParams("page", page + "")
                .addParams("gambit_id", gamitId)
                .addParams("parent_id", parentRespId)
                .url("http://st.3dgogo.com/index/chatgroup_gambit/get_respond/").build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        HuatihuifuRecyclePinlun huatiInfo1 = gson.fromJson(response, HuatihuifuRecyclePinlun.class);
                        if (huatiInfo1.getCode() != 200) {
                            return;
                        }
                        for (int i = 0; i < huatiInfo1.getInfo().getData().size(); i++) {
                            datas.add(huatiInfo1.getInfo().getData().get(i));
                        }
                        if (adapter == null) {
                            adapter = new Secend_HuatihuifuRecycleAdapter(parentUserId, datas, getContext(), SecendPinlunFragment.this);
                            lv_huati.setAdapter(adapter);
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public void onItemLongClick(int position) {
        dialogFragment = CommentDialogFragment.newInstance(datas.get(position));
        dialogFragment.show(getFragmentManager(), "comment");
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        page++;
        gethuatiList(gamitId, parentRespIDCode);
        refreshLayout.finishLoadmore();
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        datas.clear();
        page = 1;
        gethuatiList(gamitId, parentRespIDCode);
        refreshLayout.finishRefresh();
    }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                OkHttpClient okHttpClient = new OkHttpClient();
//                try {
//                    RequestBody requestBody = new FormBody.Builder()
//                            .add("gambit_id", gamitId)
//                            .add("parent_id", parentRespId)
//                            .build();
//
//                    Request request = new Request.Builder()
//                            .url(" http://st.3dgogo.com/index/chatgroup_gambit/get_respond.html")
//                            .post(requestBody)
//                            .build();
//
//
//                    try {
//                        Call call = okHttpClient.newCall(request);
//                        //判断请求是否成功
//                        try {
//                            Response response = call.execute();
//
//                            try {
//                                String responseData = response.body().string();
//                                JSONObject jsonObject = new JSONObject(responseData);
//
//                                if (jsonObject.getInt("code") == 200) {
//                                    JSONArray data = jsonObject.getJSONObject("info").getJSONArray("data");
//                                    huifuInfoList.clear();
//                                    for (int i = 0; i < data.length(); i++) {
//                                        HuatiInfo huifuinfo = new HuatiInfo();
//                                        JSONObject hfJsong = data.getJSONObject(i);
//                                        huifuUser = parse_Value(hfJsong, "userName");
//                                        huifuCoutent = parse_Value(hfJsong, "content");
//                                        huifuTime = parse_Value(hfJsong, "create_time");
//                                        huifuHead = parse_Value(hfJsong, "headImg");
//                                        huifuId = parse_Value(hfJsong, "id");
//                                        gambitid = parse_Value(hfJsong, "gambit_id");
//                                        huifuUserId = parse_Value(hfJsong, "uid");
//                                        //   huifuinfo.sethuifuInfo("respond_id", huifuHead, huifuUser, huifuId, huifuTime, huifuCoutent, huifuCai, huifuZan, huifuUserId,"","");
//                                        huifuinfo.setHuatiUser(huifuUser);
//                                        huifuinfo.settopparam("0");//均不置顶
//                                        huifuinfo.setHuatiTime(huifuTime);
//                                        huifuinfo.setHeadImg(huifuHead);
//                                        huifuinfo.setContentImg("");
//                                        huifuinfo.setHolderId(huifuUserId);
//                                        huifuinfo.setHuatiIp(huifuId);
//                                        huifuinfo.setGambitId(gambitid);
//                                        huifuinfo.setGroupId(parse_Value(hfJsong, "group_id"));
//                                        huifuinfo.setparentUserId(parentUserId);
//                                        huifuinfo.setHuatiName(huifuCoutent);
//                                            huifuInfoList.add(huifuinfo);
//                                    }
//                                    Message msg = new Message();
//                                    msg.what = UPDATEONE;
//                                    handler.sendMessage(msg);
//
//                                } else if (jsonObject.getInt("code") == 500) {
//                                    Message msg = new Message();
//                                    msg.what = UPDATEONE;
//                                    handler.sendMessage(msg);
//                                    //  isUseable=false;
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                                //  showMessagefail("深度解析失败3");
//
//
//                            }
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//
//                    }
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//
//                }
//            }
//        }).start();
//
//    }//标准post
//
//    private String parse_Value(JSONObject data, String value) {
//        String com_value = "";
//        if (data.has(value)) {
//            try {
//                com_value = data.getString(value);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        return com_value;
//    }
}
