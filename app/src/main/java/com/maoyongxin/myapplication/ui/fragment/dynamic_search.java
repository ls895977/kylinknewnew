package com.maoyongxin.myapplication.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.hotDynamicInfo;
import com.maoyongxin.myapplication.myapp.AppActivity;
import com.maoyongxin.myapplication.ui.adapter.dynamic_gride_adapter;
import com.maoyongxin.myapplication.ui.widget.mylistview.LoadListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class dynamic_search extends AppActivity {


    @BindView(R.id.line_top)
    LinearLayout lineTop;
    @BindView(R.id.tuijian_title)
    LinearLayout tuijianTitle;
    @BindView(R.id.two_DynamicPics)
    GridView twoDynamicPics;
    @BindView(R.id.searchaEdit)
    EditText searchaEdit;
    @BindView(R.id.square_find)
    LoadListView squareFind;
    @BindView(R.id.tv_Cancel)
    TextView tvCancel;

    private List<hotDynamicInfo> dynamicList = new ArrayList<>();
    private int UPDATE = 1;
    private Handler handler;
    private dynamic_gride_adapter dynamicGrideAdapter;
    private String content;

    @Override
    protected int bindLayout() {
        return (R.layout.activity_dynamic_search);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//白底黑字+沉浸式导航栏
        ButterKnife.bind(this);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == UPDATE) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            dynamicGrideAdapter = new dynamic_gride_adapter(getActivity(), dynamicList);
                            twoDynamicPics.setAdapter(dynamicGrideAdapter);
                        }
                    }, 10);
                }
            }
        };
    }

    @Override
    protected void initView() {
        //  super.initView();
        content = getIntent().getStringExtra("content");

        gethotList();
        searchDynamic(content);
    }

    @Override
    protected void initData() {
        //    super.initData();

    }

    @Override
    protected void initEvent() {
        //super.initEvent();
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void searchDynamic(final String keyword) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get().url("http://st.3dgogo.com/index/user_dynamic/search_user_dynamic_api.html")
                        .addParams("search", keyword)
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {

                    }
                });


            }
        }).start();
    }

    private void gethotList() {
//get自身服务器数据
        final String XkOne = "http://st.3dgogo.com/index/user_dynamic/get_hot_user_dynamic_api.html";

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(XkOne).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJsonXK(responseData);

                } catch (Exception e) {
                    // showMessagefail("解析失败");
                }
            }
        }).start();
    }

    private void parseJsonXK(String responseData) {
//post自身服务器数据
        int Result_code = 0;

        try {


            JSONObject jsonObject = new JSONObject(responseData);
            Result_code = jsonObject.getInt("code");
            if (Result_code == 200) {

                JSONArray data = jsonObject.getJSONArray("info");
                for (int i = 0; i < 4; i++) {
                    JSONObject recordData = data.getJSONObject(i);
                    hotDynamicInfo rcdInfo = new hotDynamicInfo();

                    rcdInfo.setCreateTime(recordData.getString("createTime"));
                    rcdInfo.setDynamicId(recordData.getString("dynamicId"));
                    rcdInfo.setDynamicContent(recordData.getString("dynamicContent"));
                    rcdInfo.setUserId(recordData.getString("userId"));
                    rcdInfo.setPraiseNum(recordData.getString("praiseNum"));
                    rcdInfo.setCommentNum(recordData.getString("commentNum"));
                    rcdInfo.setIs_praise_tread(recordData.getString("is_praise_tread"));
                    dynamicList.add(rcdInfo);

                }


                Message msg = new Message();
                msg.what = UPDATE;
                handler.sendMessage(msg);


            } else if (Result_code == 500) {


            }
        } catch (Exception e) {


        }


    }

}
