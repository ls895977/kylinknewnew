package com.maoyongxin.myapplication.ui.editapp.renmai;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.fuwuInfo;
import com.maoyongxin.myapplication.ui.adapter.shangjiaList_adapter;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class shangjiaList extends AppCompatActivity {
    private List<fuwuInfo> grideFuwulist=new ArrayList<>();
    private GridView fuwugride;
    shangjiaList_adapter shangjiaListAdapter;
    private Handler handler;
    private String fenleiId,fwsName,fwsIntro,fwsHead,fwsadress,fwsDetail;
    final private int UPDATE=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shangjia_list);
        fuwugride=(GridView)findViewById(R.id.gridlist);

        fenleiId=getIntent().getStringExtra("fenleiId");
        updateGrideList(fenleiId);

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==UPDATE)
                {
                    shangjiaListAdapter=new shangjiaList_adapter(shangjiaList.this,grideFuwulist);
                    fuwugride.setAdapter(shangjiaListAdapter);
                }
            }
        };
    }

    private void updateGrideList(final String position) {
        final String XkOne = "http://st.3dgogo.com/index/enterprise_info/get_enterprise_publicity_details_list_api.html";
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                try {

                    RequestBody requestBody = new FormBody.Builder()
                            .add("classify_id", position)
                            .build();

                    Request request = new Request.Builder()
                            .url(XkOne)
                            .post(requestBody)
                            .build();
                    try {
                        Call call = okHttpClient.newCall(request);
                        //判断请求是否成功
                        Looper.prepare();
                        try {
                            Response response = call.execute();

                            try {

                                JSONObject jsonObject = new JSONObject(response.body().string());

                                if (jsonObject.getInt("code") == 200) {
                                    Toast.makeText(shangjiaList.this,"列表获取成功",Toast.LENGTH_SHORT).show();
                                    parseFuwuList(jsonObject);
                                } else if (jsonObject.getInt("code") == 500) {

                                    Toast.makeText(shangjiaList.this,"列表获取失败",Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();


                            }
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        Looper.loop();

                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }).start();
    }
    private void parseFuwuList(JSONObject fuwulist) {
        try {


            JSONArray grideDate=fuwulist.getJSONObject("info").getJSONArray("data");

            grideFuwulist.clear();
            for(int i=0;i<grideDate.length();i++)
            {
                JSONObject dataDetail=grideDate.getJSONObject(i);
                fuwuInfo fwDetail =new fuwuInfo();
                fwsName=dataDetail.getString("name");
                fwsHead=dataDetail.getString("publicity_img");
                fwsIntro=dataDetail.getString("intro");
                fwsDetail=dataDetail.getString("details_url");
                fwDetail.setfuwuInfo(fwsName,fwsHead,fwsIntro,"四川.成都",fwsDetail);
                grideFuwulist.add(fwDetail);

            }

            Message msg = new Message();
            msg.what = UPDATE;
            handler.sendMessage(msg);
        } catch (Exception e) {

        }


    }
}
