package com.maoyongxin.myapplication.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.myapp.AppActivity;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.ui.adapter.mingpianAdaper;

import org.json.JSONObject;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemLongClick;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.widget.AdapterView.OnItemLongClickListener;
public  class mingpian extends AppActivity {

    @BindView(R.id.mingpianshoucangjia)
    ListView mingpianshoucangjia;
    private List<mingpianInfo> commonFunList = new ArrayList<>();

    private static final int UPDATE = 0;
    private Handler handler;



    @Override
    protected int bindLayout() {
        return (R.layout.activity_mingpian);
    }

    @Override
    protected void initView() {
       // super.initView();
    }

    @Override
    protected void initData() {
     //   super.initData();
        getMingpian();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final mingpianAdaper mingpianAdaper = new mingpianAdaper(this, R.layout.item_shoucangjia, commonFunList);
        ButterKnife.bind(this);
        mingpianshoucangjia.setAdapter(mingpianAdaper);
        initView();
        initData();
        handler = new Handler() {


            @Override
            public void handleMessage(Message msg) {
                if (msg.what == UPDATE) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mingpianAdaper.notifyDataSetInvalidated();
                        }
                    }, 10);


                }
                super.handleMessage(msg);
            }

        };

        mingpianshoucangjia.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                commonFunList.remove(i);
                return false;
            }
        });

    }

    private void getMingpian() {

        final String XkOne = "http://st.3dgogo.com/index/enterprise_info/get_collect_info/uid/";
        final String usrId;

        usrId = AppApplication.getCurrentUserInfo().getUserId().toString();

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(XkOne + usrId).build();

                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();

                    parseJsonMinpian(responseData);

                } catch (Exception e) {

                }
            }
        }).start();


    }

    private void parseJsonMinpian(String mingpian) {
        int count = 0;

        try {

            JSONObject jsonObject = new JSONObject(mingpian);
            count = jsonObject.getInt("count");
            int code = jsonObject.getInt("code");
            UPui(count, jsonObject);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void UPui(final int count, final JSONObject jsonObject) {


        String company;
        String zhiwei = "经理";
        String TelNumber;
        String email;
        String webAdress;
        String bossName;
        String zhuying;


        try {
               commonFunList.clear();
            for (int i = 0; i < count; i++) {
                JSONObject info = jsonObject.getJSONArray("info").getJSONObject(i);
                company = info.getString("name");
                TelNumber = info.getString("phoneNumber");
                email = info.getString("email");
                webAdress = info.getString("websiteList");
                bossName = info.getString("legalPersonName");
                zhuying = info.getString("businessScope");
                mingpianInfo mp2 = new mingpianInfo(company, bossName, zhiwei, TelNumber, email, webAdress, zhuying);
                commonFunList.add(mp2);


            }
            Collections.reverse(commonFunList);
            Message msg = new Message();
            msg.what = UPDATE;
            handler.sendMessage(msg);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
    }

}





