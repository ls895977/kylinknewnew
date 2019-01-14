package com.maoyongxin.myapplication.ui.groupchat;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;


import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
//import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.HuatiInfo;
import com.maoyongxin.myapplication.myapp.AppActivity;
import com.maoyongxin.myapplication.ui.adapter.HuatiRecycle;
import com.maoyongxin.myapplication.ui.huati.Fabu;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class GroupChatHuatiDetial extends AppCompatActivity {
    private ImageView groupImg;
    private List<HuatiInfo> huatiInfos= new ArrayList<>();
    private int UPDATE=0;
    private String GroupImgUrl,Groupid,GroupName;
    private RecyclerView huati_list;
    private HuatiRecycle adapter;
    private TextView tv_groupName;
    private Button bt_toGroup,bt_fabuhuati;
    private Toolbar toolbar;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==UPDATE)
            {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(GroupChatHuatiDetial.this).load(GroupImgUrl).into(groupImg);
                        tv_groupName.setText(GroupName);
                        adapter=new HuatiRecycle(huatiInfos,GroupChatHuatiDetial.this);
                        huati_list.setAdapter(adapter);

                    }
                },100);

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_group_chat_huati_detial);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//白底黑字+沉浸式导航栏

        toolbar=(Toolbar)findViewById(R.id.huati_toolbar);
        huati_list=(RecyclerView)findViewById(R.id.huatiList);
        tv_groupName=(TextView)findViewById(R.id.tv_groupName);
        groupImg=(ImageView)findViewById(R.id.grouphead_img);
        bt_toGroup=(Button)findViewById(R.id.togroup);
        bt_fabuhuati=(Button)findViewById(R.id.tofabu) ;
        GroupImgUrl=getIntent().getStringExtra("picUrl");
        Groupid=getIntent().getStringExtra("groupNum");
        GroupName=getIntent().getStringExtra("groupName");
        huati_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        huati_list.setItemAnimator(null);
        huati_list.setHasFixedSize(true);
        huati_list.setNestedScrollingEnabled(false);
        getHuattiList(Groupid);


        bt_toGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RongIM.getInstance().startGroupChat(GroupChatHuatiDetial.this, getIntent().getStringExtra("groupNum"), getIntent().getStringExtra("groupName"));
            }
        });

        bt_fabuhuati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupChatHuatiDetial.this, Fabu.class);
                intent.putExtra("GroupName", GroupName);
                intent.putExtra("GroupId", Groupid);
                startActivity(intent);
            }
        });
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

            for (int i = 0; i < data.length(); i++) {

                JSONObject huatidata=data.getJSONObject(i);
                HuatiInfo huatiInfo = new HuatiInfo();

                huatiInfo.setHuatiName(huatidata.getString("title"));
                huatiInfo.setHuatiPinglun(huatidata.getString("post_num"));
                huatiInfo.setHuatiTime(huatidata.getString("create_time"));
                huatiInfo.setHuatiUser(huatidata.getString("userName"));

                huatiInfo.setHeadImg(parseUrl(huatidata.getString("headImg")));
                huatiInfo.setHuatiIp( huatidata.getString("id"));
                huatiInfo.setHolderId(huatidata.getString("uid"));
                huatiInfo.setContentImg(huatidata.getString("img"));
                huatiInfo.setGroupName(huatidata.getString("img"));
                huatiInfos.add(huatiInfo);

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

}
