package com.maoyongxin.myapplication.ui.groupchat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.FuwuList;
import com.maoyongxin.myapplication.entity.GroupInfo;
import com.maoyongxin.myapplication.entity.grideInfo;
import com.maoyongxin.myapplication.entity.huifuInfo;
import com.maoyongxin.myapplication.ui.adapter.Fuwu_gride_adapter;
import com.maoyongxin.myapplication.ui.adapter.Yellow_pageAdapter;
import com.maoyongxin.myapplication.ui.adapter.group_gride_adapter;
import com.maoyongxin.myapplication.ui.editapp.minefragment.fwDetail;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GroupAllList extends AppCompatActivity {

    @BindView(R.id.grouptypeList)
    ListView grouptypeList;

    @BindView(R.id.gride_grouplist)
    GridView grideGrouplist;

    private List<FuwuList> hangyelists = new ArrayList<>();

    private List<grideInfo> grideFuwulist=new ArrayList<>();

    private Yellow_pageAdapter mingpianAdaper;
    private int UPDATE=1;
    private int  UPDATETWO=2;
    private Handler handler;
    private String groupName,groupId,groupImg;
    private group_gride_adapter groupGrideAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_all_list);
        ButterKnife.bind(this);
        getgroupttypeList();
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==UPDATE)
                {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mingpianAdaper = new Yellow_pageAdapter(GroupAllList.this, hangyelists);
                            mingpianAdaper.setCurrentItem(0);
                            mingpianAdaper.notifyDataSetChanged();
                            gethuatiList("1");
                            grouptypeList.setAdapter(mingpianAdaper);
                        }
                    },1);


                }
              else  if(msg.what==UPDATETWO)
                {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            groupGrideAdapter = new group_gride_adapter(GroupAllList.this, grideFuwulist);
                            grideGrouplist.setAdapter(groupGrideAdapter);
                        }
                    },1);


                }

            }
        };
        grouptypeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                mingpianAdaper.setCurrentItem(i);
                mingpianAdaper.notifyDataSetChanged();
                String grouptypeId=hangyelists.get(i).getFuwuId()+"";

                gethuatiList(grouptypeId);
            }
        });
    }


    private void getgroupttypeList() {
        final String grouptypeList = "http://st.3dgogo.com/index/chatgroup_gambit/get_chatgroup_classify";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(grouptypeList).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();

                    try {

                        JSONObject jsonObject = new JSONObject(responseData);

                        JSONArray data = jsonObject.getJSONArray("info");


                        for (int i = 0; i < data.length(); i++) {


                            JSONObject newList = data.getJSONObject(i);
                            FuwuList datas = new FuwuList();
                            groupName = parse_Value(newList, "group_classify_name");
                            groupId = parse_Value(newList, "group_classify_id");

                            datas.setfuwuInfo(groupName,groupId);

                            hangyelists.add(datas);



                        }

                        Message msg = new Message();
                        msg.what = UPDATE;
                        handler.sendMessage(msg);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    //  showMessagefail("解析失败");
                }
            }
        }).start();
    }
    private String parse_Value(JSONObject data, String value) {
        String com_value = "";
        if (data.has(value)) {
            try {
                com_value = data.getString(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return com_value;
    }

    private void gethuatiList(final String grouptypeId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();


                try {

                    RequestBody requestBody = new FormBody.Builder()
                            .add("groupClassifyId", grouptypeId)
                            .build();

                    Request request = new Request.Builder()
                            .url("http://st.3dgogo.com/index/group_header/get_group_header_all.html")
                            .post(requestBody)
                            .build();


                    try {
                        Call call = okHttpClient.newCall(request);
                        //判断请求是否成功
                        try {
                            Response response = call.execute();

                            try {

                                JSONObject jsonObject = new JSONObject(response.body().string());
                                grideFuwulist.clear();
                                grideFuwulist.removeAll(grideFuwulist);
                                if (jsonObject.getInt("code") == 200) {
                                    JSONArray data = jsonObject.getJSONArray("info");

                                    for (int i = 0; i < data.length(); i++) {
                                        grideInfo groupinfo = new grideInfo();

                                        JSONObject hfJsong = data.getJSONObject(i);

                                        groupName=hfJsong.getString("groupName");
                                        groupId=hfJsong.getString("groupId");
                                        groupImg=hfJsong.getString("image");


                                        groupinfo.setfuwuInfo(groupName,groupImg,groupId);
                                        grideFuwulist.add(groupinfo);


                                    }

                                    Message msg = new Message();
                                    msg.what = UPDATETWO;
                                    handler.sendMessage(msg);

                                } else if (jsonObject.getInt("code") == 500) {


                                }
                            } catch (Exception e) {
                                e.printStackTrace();



                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }).start();

    }


    @Override
    protected void onResume() {
        super.onResume();
       // getgroupttypeList();
    }
}
