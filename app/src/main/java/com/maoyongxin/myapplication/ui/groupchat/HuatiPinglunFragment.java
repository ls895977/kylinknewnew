package com.maoyongxin.myapplication.ui.groupchat;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.HuatiInfo;
import com.maoyongxin.myapplication.ui.adapter.HuatiRecycle;
import com.maoyongxin.myapplication.ui.fragment.HeaderViewPagerFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class HuatiPinglunFragment  extends HeaderViewPagerFragment implements  SwipeRefreshLayout.OnRefreshListener  {
    private RecyclerView lv_huati;
    private SwipeRefreshLayout swipefreshlayout;
    SmartRefreshLayout refreshLayout;
    private  String GroupId,gambit_id;
    private int UPDATEONE = 0;
    private int UPDATETWO = 1;
    private HuatiRecycle adapter;
    private List<HuatiInfo> huatiInfos= new ArrayList<>();;
    public static HuatiPinglunFragment newInstance(String param1, String param2) {

        return new HuatiPinglunFragment();
    }
    private Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            if(msg.what==UPDATEONE)
            {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter=new HuatiRecycle(huatiInfos,getContext());
                        lv_huati.setAdapter(adapter);
                    }
                },10); }
            super.handleMessage(msg);     }
    };


    @Override
    public void onRefresh() {

        Toast.makeText(getActivity(),"刷新中",Toast.LENGTH_SHORT).show();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragmenthuati_pinglun, container, false);
        swipefreshlayout=(SwipeRefreshLayout)view.findViewById(R.id.swipefreshlayout);
        refreshLayout = (SmartRefreshLayout) view.findViewById(R.id.smartrefreshLayout);
        swipefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getActivity(),"刷新中",Toast.LENGTH_SHORT).show();
            }
        });

        lv_huati = (RecyclerView) view.findViewById(R.id.lv_huati);
        LinearLayoutManager mLinearLayoutManager=new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        lv_huati.setLayoutManager(mLinearLayoutManager);
        lv_huati.setItemAnimator(null);
        lv_huati.setHasFixedSize(true);
        lv_huati.setNestedScrollingEnabled(false);



        swipefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            public void onRefresh() {

                getHuattiList("15");
            }
        });
        setRefresh();

        return view;
    }

    private void setRefresh() {
        //设置 Header 为 贝塞尔雷达 样式

        refreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
//设置 Footer 为 球脉冲 样式
        refreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale));
        refreshLayout.setDisableContentWhenRefresh(true);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getHuattiList("15");
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {

            }
        });
    }
    private void initData() {

        gambit_id=getActivity().getIntent().getStringExtra("gambit_id");
        GroupId=getActivity().getIntent().getStringExtra("GroupId");



    }


    private void getHuattiList(final  String GroupId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                GetHuati("15");
            }
        }).start();
    }


    private void GetHuati(String GroupId) {
        String apiUrl = "http://st.3dgogo.com/index/chatgroup_gambit/get_gambit_details/id/";
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(apiUrl + GroupId).build();
            Response response = client.newCall(request).execute();
            String responseData = response.body().string();
            parseJson(responseData);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private  void parseJson(final String Jsondata)
    {

        try{
            JSONObject jsonObject = new JSONObject(Jsondata);
            JSONArray data = jsonObject.getJSONObject("info").getJSONArray("data");
            huatiInfos.clear();
            for (int i = 0; i < data.length(); i++) {

                JSONObject huatidata=data.getJSONObject(i);
                HuatiInfo huatiInfo = new HuatiInfo();


                huatiInfos.add(huatiInfo);



            }
            Message msg=new Message();
            msg.what=UPDATEONE;
            handler.sendMessage(msg);

        }
        catch (Exception e)
        {e.printStackTrace();}finally {
            refreshLayout.finishRefresh(true);//传入false表示刷新失败
        }

    }

    private String parseUrl(String imgUrl){
        String rep = "\\";
        final String ImgUrl=imgUrl.replace(rep,"");


        return ImgUrl;
    }
    @Override
    public View getScrollableView() {
        return lv_huati;
    }
}
