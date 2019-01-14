package com.maoyongxin.myapplication.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.myapp.AppHandlerActivity;
import com.maoyongxin.myapplication.ui.adapter.mingpianAdaper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Mingpianshoucangjia extends AppCompatActivity {
    private ListView listView;

    private List<mingpianInfo> commonFunList=new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mingpianshoucangjia);
        listView=(ListView)findViewById(R.id.mingpianshoucangjia);
        mingpianAdaper mingpianAdaper=new mingpianAdaper(this, R.layout.item_shoucangjia,commonFunList);
        listView.setAdapter(mingpianAdaper);
        initConmapy();



    }

    private void initConmapy(){
        mingpianInfo mp=new mingpianInfo("facebook","余晟睿","经理","13198511889","yusr@3d.top","yusr3d.top","3D打印、三维建模");
        commonFunList.add(mp);

        getMingpian();
    }
    private void showMessage(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }
    private void getMingpian(){

        final String XkOne = "http://st.3dgogo.com/index/enterprise_info/get_collect_info/uid/";
        final String usrId;
        usrId= AppApplication.getCurrentUserInfo().getUserId().toString();

        new Thread(new Runnable(){
            @Override
            public void run() {
                showMessage("开始解析");
                try {
                    showMessage("解析中");
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(XkOne + usrId).build();

                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();

                    parseJsonMinpian(responseData);

                } catch (Exception e) {
                    showMessage("执行失败1");
                }
            }
        }).start();


    }
    private void parseJsonMinpian(String mingpian){
        int count=0;

        try {
            showMessage("95%");
            JSONObject jsonObject = new JSONObject(mingpian);
            count=jsonObject.getInt  ("count");
            int code=jsonObject.getInt("code");
            UPui(count,jsonObject);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            showMessage("执行失败2");
        }
    }
    public void UPui(final int count ,final JSONObject jsonObject){
        Looper.prepare();

        runOnUiThread(new Runnable() {
            String company;
            String zhiwei="经理";
            String TelNumber;
            String email;
            String webAdress;
            String bossName;
            String zhuying;


            @Override
            public void run(){
                try{
                    for(int i=0;i<count;i++)
                    {
                        JSONObject info=jsonObject.getJSONArray("info").getJSONObject(i);
                        company=info.getString("name");
                        TelNumber=info.getString("phoneNumber");
                        email=info.getString("email");
                        webAdress=info.getString("websiteList");
                        bossName=info.getString("legalPersonName");
                        zhuying=info.getString("businessScope");
                        mingpianInfo mp2=new mingpianInfo(company,bossName,zhiwei,TelNumber,email,webAdress,zhuying);
                        commonFunList.add(mp2);
                        showMessage("总条数="+ commonFunList.size());
                    }
                    Collections.reverse(commonFunList);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        });

        Looper.loop(); }


}