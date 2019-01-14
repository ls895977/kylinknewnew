package com.maoyongxin.myapplication.ui.groupchat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.HuatiInfo;
import com.maoyongxin.myapplication.server.widget.SelectableRoundedImageView;
import com.maoyongxin.myapplication.ui.adapter.HuatiRecycle;
import com.maoyongxin.myapplication.ui.fragment.HeaderViewPagerFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class GoverFragment extends HeaderViewPagerFragment implements  SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView lv_huati;
    private List<HuatiInfo> huatiInfos= new ArrayList<>();;
    private String huati;
    private String post_num;
    private String usrname;
    private String huatiIp;
    private static final int UPDATE = 0;
    private  String Groupid,Groupname,groupimg;
    private SwipeRefreshLayout swipefreshlayout;
    private HuatiRecycle adapter;

    private View view;
    public static GoverFragment newInstance() {
        return new GoverFragment();
    }
    //话题列表
    private  Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            if(msg.what==UPDATE)
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

        View view = inflater.inflate(R.layout.fragment_group_chat_huati, container, false);
        swipefreshlayout=(SwipeRefreshLayout)view.findViewById(R.id.swipefreshlayout);
        swipefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getActivity(),"刷新中",Toast.LENGTH_SHORT).show();
            }
        });

        lv_huati = (RecyclerView) view.findViewById(R.id.lv_huati);
        LinearLayoutManager   mLinearLayoutManager=new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        lv_huati.setLayoutManager(mLinearLayoutManager);
        lv_huati.setItemAnimator(null);
        lv_huati.setHasFixedSize(true);
        lv_huati.setNestedScrollingEnabled(false);


        initData();
        initView();

        swipefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            public void onRefresh() {

                getHuattiList(Groupid);
            }
        });


        return view;





    }

    private void initView() {


    }


    private void initData() {

        Groupid=getActivity().getIntent().getStringExtra("groupNum");
        Groupname=getActivity().getIntent().getStringExtra("groupName");
        groupimg=getActivity().getIntent().getStringExtra("picUrl");
        getHuattiList(Groupid);

    }



    public class HuatiAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        @Override
        public int getCount() {
            return huatiInfos.size();
        }

        @Override
        public Object getItem(int i) {
            return huatiInfos.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                inflater = LayoutInflater.from(getActivity());
                view = inflater.inflate(R.layout.item_group_huati, null);

                 holder.content_img = (ImageView) view.findViewById(R.id.content_img);
                holder.tv_huati_content = (TextView) view.findViewById(R.id.tv_huati_content);
                holder.tv_pinglun_num = (TextView) view.findViewById(R.id.tv_pinglun_num);
                holder.tv_huati_user = (TextView) view.findViewById(R.id.tv_huati_user);
                holder.tv_huati_time = (TextView) view.findViewById(R.id.tv_huati_time);

                holder.head_img = (SelectableRoundedImageView) view.findViewById(R.id.img_huatiuser_head);
                holder.huatiIp=(TextView)view.findViewById(R.id.huatiId);
                holder.holderID=(TextView)view.findViewById(R.id.holderID);

                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            ViewGroup parent=(ViewGroup)view.getParent();
            if(parent!=null)
            {
                parent.removeView(view);
            }
            else {
                holder.tv_huati_content.setText(huatiInfos.get(i).getHuatiName());
                holder.tv_huati_time.setText(huatiInfos.get(i).getHuatiTime());
                holder.tv_huati_user.setText(huatiInfos.get(i).getHuatiUser());
                holder.tv_pinglun_num.setText(huatiInfos.get(i).getHuatiPinglun());
                holder.huatiIp.setText(huatiInfos.get(i).getHuatiIp());
                holder.holderID.setText(huatiInfos.get(i).getHolderId());
                if(!huatiInfos.get(i).getContentImg().equals("")){
                    holder.content_img.setVisibility(View.VISIBLE);
                    Glide.with(getContext()).load(huatiInfos.get(i).getContentImg()).into(holder.content_img);
                }


                String headurl=huatiInfos.get(i).getHeadImg().toString();

                Glide.with(getContext()).load(headurl).into(holder.head_img);
            }

            return view;
        }

        class ViewHolder {
            TextView tv_huati_content, tv_huati_user, tv_pinglun_num, tv_huati_time,holderID;
            SelectableRoundedImageView img_huatiuser_head;
            TextView huatiIp;
             ImageView content_img;
            ImageView head_img;

        }
    }
    private void getHuattiList( final String GroupId){

        new Thread(new Runnable() {
            @Override
            public void run() {
                GetHuati(GroupId);
            }
        }).start();
    }
    private void GetHuati(String GroupId){
        String apiUrl="http://st.3dgogo.com/index/chatgroup_gambit/get_gambit/group_id/";
        try{
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(apiUrl + GroupId).build();
            Response response = client.newCall(request).execute();
            String responseData = response.body().string();
            parseJson(responseData);

        }
        catch (Exception e)
        { e.printStackTrace();}


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

                huatiInfo.setHuatiName(huatidata.getString("title"));
                huatiInfo.setHuatiPinglun(huatidata.getString("post_num"));
                huatiInfo.setHuatiTime(huatidata.getString("create_time"));
                huatiInfo.setHuatiUser(huatidata.getString("userName"));
                huatiInfo.setNumZan(huatidata.getString("praise_num"));//话题点赞数量
                huatiInfo.setHeadImg(parseUrl(huatidata.getString("headImg")));
                huatiInfo.setHuatiIp( huatidata.getString("id"));
                huatiInfo.setHolderId(huatidata.getString("uid"));
                huatiInfo.setContentImg(huatidata.getString("img"));
                huatiInfo.setGroupId(Groupid);
                huatiInfo.setGroupName(Groupname);
                huatiInfo.setHuatiContent(huatidata.getString("content"));
                huatiInfo.setgrouppicurl(groupimg);

                huatiInfos.add(huatiInfo);

                  //todo
                //Log.e("/////", "parseJson: "+huatiInfos.size() );

            }
            Message msg=new Message();
            msg.what=UPDATE;
            handler.sendMessage(msg);

        }
        catch (Exception e)
        {e.printStackTrace();}

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
