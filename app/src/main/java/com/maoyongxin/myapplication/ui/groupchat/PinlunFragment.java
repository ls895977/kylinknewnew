package com.maoyongxin.myapplication.ui.groupchat;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.HuatiInfo;
import com.maoyongxin.myapplication.entity.HuatiInfo1;
import com.maoyongxin.myapplication.entity.huifuInfo;
import com.maoyongxin.myapplication.ui.CommentDialogFragment;
import com.maoyongxin.myapplication.ui.EventMessage;
import com.maoyongxin.myapplication.ui.adapter.HuatiRecycle;
import com.maoyongxin.myapplication.ui.adapter.HuatiRecycleAdapter;
import com.maoyongxin.myapplication.ui.adapter.HuatihuifuRecycle;
import com.maoyongxin.myapplication.ui.adapter.HuatihuifuRecyclerAdapter;
import com.maoyongxin.myapplication.ui.adapter.huifuAdapter;
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
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class PinlunFragment extends HeaderViewPagerFragment implements OnRefreshListener, OnLoadMoreListener, HuatihuifuRecyclerAdapter.LongClickListener {
    private RecyclerView lv_huati;
    SmartRefreshLayout refreshLayout;
    private SmartRefreshLayout swipefreshlayout;
    private Handler handler;
    private final int UPDATEONE = 0;
    private String huaticontent, img_head, fabutime, hostname;
    private String huifuUser, huifuCoutent, huifuTime, huifuHead, huifuId, huifuZan, huifuCai, huifuUserId, huatiId, gambitid;
    private HuatihuifuRecycle huifuAdapter;
    private String gamitId, groupId, parentUserId;
    private CommentDialogFragment dialogFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_pinlun, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        swipefreshlayout = (SmartRefreshLayout) view.findViewById(R.id.swipefreshlayout);
        refreshLayout = (SmartRefreshLayout) view.findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));  //设置 Header 为 贝塞尔雷达 样式
        refreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale));//设置 Footer 为 球脉冲 样式
        refreshLayout.setEnableRefresh(true);//启用刷新
        refreshLayout.setEnableAutoLoadMore(true);//启用加载
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        lv_huati = (RecyclerView) view.findViewById(R.id.lv_huati);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        lv_huati.setLayoutManager(mLinearLayoutManager);
        lv_huati.setItemAnimator(null);
        lv_huati.setHasFixedSize(true);
        lv_huati.setNestedScrollingEnabled(false);
    }

    public void initData() {
        gamitId = getActivity().getIntent().getStringExtra("gambit_id");
        groupId = getActivity().getIntent().getStringExtra("GroupId");
        parentUserId = getActivity().getIntent().getStringExtra("parentUserId");
        gethuatiList(gamitId);
    }

    @Subscribe
    public void onEventMainThread(EventMessage eventMessage) {
        Log.e("aa","-----------"+eventMessage.getType());
        switch (eventMessage.getType()) {
            case 5:
            case 2:
                 page=1;
                datas.clear();
                gethuatiList(gamitId);
                break;
        }
    }

    @Override
    public View getScrollableView() {
        return lv_huati;
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout1) {
        page=1;
        datas.clear();
        gethuatiList(gamitId);
        refreshLayout.finishRefresh();
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout1) {
        page++;
        gethuatiList(gamitId);
        refreshLayout.finishLoadmore();
    }

    Gson gson = new Gson();
    List<HuatihuifuRecyclePinlun.InfoBean.DataBeanX> datas = new ArrayList<>();
    HuatihuifuRecyclerAdapter adapter;
    int page=1;
    private void gethuatiList(final String gamitId) {
        OkHttpUtils.post()
                .addParams("gambit_id", gamitId)
                .addParams("page", page+"")
                .url("http://st.3dgogo.com/index/chatgroup_gambit/get_respond").build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }
                    @Override
                    public void onResponse(String response, int id) {
                        HuatihuifuRecyclePinlun ben = gson.fromJson(response, HuatihuifuRecyclePinlun.class);
                        if (ben.getCode() != 200) {
                            return;
                        }
                        for (int i = 0; i < ben.getInfo().getData().size(); i++) {
                            datas.add(ben.getInfo().getData().get(i));
                        }
                        if (adapter == null) {
                            if(datas.size()==0){
                                return;
                            }
                            adapter = new HuatihuifuRecyclerAdapter(datas,parentUserId, getContext(), PinlunFragment.this);
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
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                OkHttpClient okHttpClient = new OkHttpClient();
//                try {
//                    RequestBody requestBody = new FormBody.Builder()
//                            .add("gambit_id", gamitId)
//                            .build();
//                    Request request = new Request.Builder()
//                            .url("http://st.3dgogo.com/index/chatgroup_gambit/get_respond")
//                            .post(requestBody)
//                            .build();
//                    try {
//                        Call call = okHttpClient.newCall(request);
//                        //判断请求是否成功
//                        try {
//                            Response response = call.execute();
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
//                                        JSONObject row = hfJsong.getJSONObject("row");
//                                        huifuUser = parse_Value(hfJsong, "userName");
//                                        huifuCoutent = parse_Value(hfJsong, "content");
//                                        huifuTime = parse_Value(hfJsong, "create_time");
//                                        huifuHead = parse_Value(hfJsong, "headImg");
//                                        huifuId = parse_Value(hfJsong, "id");
//                                        gambitid = parse_Value(hfJsong, "gambit_id");
//                                        //  huifuCai = hfJsong.getInt("tread_num");
//                                        //  huifuZan = hfJsong.getInt("praise_num");
//
//                                        huifuUserId = parse_Value(hfJsong, "uid");
//
//                                        //   huifuinfo.sethuifuInfo("respond_id", huifuHead, huifuUser, huifuId, huifuTime, huifuCoutent, huifuCai, huifuZan, huifuUserId,"","");
//
//                                        huifuinfo.setHuatiUser(huifuUser);
//                                        huifuinfo.settopparam("0");//均不置顶
//                                        huifuinfo.setHuatiTime(huifuTime);
//                                        huifuinfo.setHeadImg(huifuHead);
//                                        huifuinfo.setContentImg("");
//                                        huifuinfo.setHolderId(huifuUserId);
//                                        huifuinfo.setHuatiIp(huifuId);
//                                        huifuinfo.setGambitId(gambitid);
//                                        huifuinfo.setGroupId(parse_Value(hfJsong, "group_id"));
//
//
//                                        huifuinfo.setparentUserId(parentUserId);
//                                        huifuinfo.setHuatiName(huifuCoutent);
//
//
//                                        if (row.getInt("num") > 0) {
//                                            huifuinfo.setFirstPerson(row.getString("userName"));
//                                            huifuinfo.setres_count(row.getInt("num"));
//                                            huifuinfo.setlaset_response(row.getString("content"));
//                                            huifuinfo.set(row.getString("headImg"));
//
//                                        } else {
//                                            huifuinfo.setres_count(0);
//                                        }
//
//                                        if (parse_Value(hfJsong, "parent_id").equals("0")) {
//                                            huifuInfoList.add(huifuinfo);
//                                        }
//
//
//                                    }
//
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

//    private String parse_Value(JSONObject data, String value) {
//        String com_value = "";
//        if (data.has(value)) {
//            try {
//                com_value = data.getString(value);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return com_value;
//    }
}
