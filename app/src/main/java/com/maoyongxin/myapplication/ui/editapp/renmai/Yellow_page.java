package com.maoyongxin.myapplication.ui.editapp.renmai;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.FuwuList;
import com.maoyongxin.myapplication.entity.grideInfo;
import com.maoyongxin.myapplication.entity.tuijianInfo;
import com.maoyongxin.myapplication.entity.yellowPage_info;
import com.maoyongxin.myapplication.myapp.AppActivity;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.ui.adapter.Fuwu_gride_adapter;
import com.maoyongxin.myapplication.ui.adapter.Yellow_pageAdapter;
import com.maoyongxin.myapplication.ui.editapp.editadapter.SquareAdapter;
import com.maoyongxin.myapplication.ui.news.activity.BannerLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Yellow_page extends AppActivity {


    @BindView(R.id.yellow_page_list)
    ListView hangyefenleiList;//行业大类细分列表


    @BindView(R.id.companylist)
    GridView companylist;

    private List<yellowPage_info> commonFunList = new ArrayList<>();
    private List<FuwuList> fuwuList = new ArrayList<>();
    private List<grideInfo> grideFuwulist = new ArrayList<>();
    private List<tuijianInfo> tuijianBanner= new ArrayList<>();
    private List<tuijianInfo> tuijianList = new ArrayList<>();

    private JSONArray fwdata;

    private static final int UPDATE = 0;
    private static final int UPDATFWDETAIL = 1;
    private Handler handler;
    private String fenleiId;
    private String nextPage = "1";

    private String title;
    private String subtitle;
    private String imgurl;
    private String tuijianurl;
    private String share_img,communityname;
    private String hostId="";
    private List<String> res = new ArrayList<>();
    private List<String> titles = new ArrayList<>();

    Fuwu_gride_adapter fwgrideAdapter;
    Yellow_pageAdapter shangjiafenleiAdapter;


    @Override
    protected int bindLayout() {

        return (R.layout.activity_yellow_page);
    }

    @Override
    protected void initView() {


        companylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final TextView tv_id = (TextView) view.findViewById(R.id.gridId);
                Intent intent = new Intent(getActivity(), shangjiaList.class);
                intent.putExtra("fenleiId", tv_id.getText().toString());
                startActivity(intent);
            }
        });

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        GettuijianList();

        getfirstList();


    }

    @Override
    protected void initData() {

        GetFuwulist();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);


        hangyefenleiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                shangjiafenleiAdapter.setCurrentItem(i);
                shangjiafenleiAdapter.notifyDataSetChanged();
                updateGrideList(i);
            }
        });


        handler = new Handler() {


            @Override
            public void handleMessage(Message msg) {
                if (msg.what == UPDATE) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            shangjiafenleiAdapter = new Yellow_pageAdapter(Yellow_page.this, fuwuList);

                            hangyefenleiList.setAdapter(shangjiafenleiAdapter);

                            shangjiafenleiAdapter.setCurrentItem(0);
                            shangjiafenleiAdapter.notifyDataSetChanged();

                            updateGrideList(0);

                        }
                    }, 200);
                } else if (msg.what == UPDATFWDETAIL) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            fwgrideAdapter = new Fuwu_gride_adapter(Yellow_page.this, grideFuwulist, tuijianList,tuijianBanner,res,titles);

                            companylist.setAdapter(fwgrideAdapter);
                            fwgrideAdapter.notifyDataSetChanged();
                        }
                    }, 200);
                }
                super.handleMessage(msg);
            }

        };



    }

    private void getfirstList() {

        updateGrideList(0);
    }



    private String parse_Value(JSONObject data, String value) {
        String com_value = "暂无数据";
        if (data.has(value)) {
            try {
                com_value = data.getString(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return com_value;
    }

    private void updateGrideList(int position) {
        try {
            JSONObject gridedate = fwdata.getJSONObject(position);
            JSONArray fuwulist = gridedate.getJSONArray("sublevel");
            grideFuwulist.clear();
            for (int i = 0; i < fuwulist.length(); i++) {
                grideInfo fwDetail = new grideInfo();
                fenleiId = fuwulist.getJSONObject(i).getString("id");
                fwDetail.setfuwuInfo(fuwulist.getJSONObject(i).getString("name"), fuwulist.getJSONObject(i).getString("img"), fenleiId);
                grideFuwulist.add(fwDetail);


            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void GettuijianList() {

        OkHttpUtils.post().url("http://st.3dgogo.com/index/advertising/get_advertising_list_api.html")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                try
                {

                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray data = jsonObject.getJSONArray("info");
                    JSONObject tuijianinfo;


                    tuijianInfo  tuijanBanner =new tuijianInfo();
                    for (int i = 0; i < 2; i++)
                    {
                        tuijianinfo = data.getJSONObject(i);
                        title = parse_Value(tuijianinfo, "title");
                        subtitle = parse_Value(tuijianinfo, "secondary_title");
                        imgurl = parse_Value(tuijianinfo, "img");
                        tuijianurl = parse_Value(tuijianinfo, "url");
                        share_img=parse_Value(tuijianinfo,"pub_share_img");


                        res.add(imgurl);
                        titles.add(title);

                        tuijanBanner.settuijianInfo(title, subtitle, tuijianurl, imgurl,share_img,hostId);
                        tuijianBanner.add(tuijanBanner);
                    }
                    for (int j = 2; j < 6; j++) {

                        tuijianInfo shangping = new tuijianInfo();
                        tuijianinfo = data.getJSONObject(j);
                        title = parse_Value(tuijianinfo, "title");
                        subtitle = parse_Value(tuijianinfo, "secondary_title");
                        imgurl = parse_Value(tuijianinfo, "img");
                        tuijianurl = parse_Value(tuijianinfo, "url");
                        share_img=parse_Value(tuijianinfo,"pub_share_img");
                       // communityname=parse_Value(tuijianinfo,"pub_share_img");
                        shangping.settuijianInfo(title, subtitle, tuijianurl, imgurl,share_img,hostId);
                        tuijianList.add(shangping);


                    }

                    Message msg = new Message();
                    msg.what = UPDATFWDETAIL;
                    handler.sendMessage(msg);


                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


            }
        });

    }
    private void GetFuwulist() {

        OkHttpUtils.post().url("http://st.3dgogo.com/index/enterprise_grouping/get_enterprise_grouping_list_api.html")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                try
                {

                    parseFuwuList(response);


                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


            }
        });

    }


    private void parseFuwuList(String fuwulist) {
        try {
            JSONObject jsonObject = new JSONObject(fuwulist);
            fwdata = jsonObject.getJSONArray("info");
            fuwuList.clear();
            for (int i = 0; i < fwdata.length(); i++) {
                FuwuList datas = new FuwuList();
                String name = fwdata.getJSONObject(i).getString("name");
                String id = fwdata.getJSONObject(i).getString("id");
                datas.setfuwuInfo(name, id);

                fuwuList.add(datas);
            }

            Message msg = new Message();
            msg.what = UPDATE;
            handler.sendMessage(msg);
        } catch (Exception e) {

        }


    }

    private void getMingpian(final String type, final String Keyword, final String page) {

        final String XkOne = "http://st.3dgogo.com/index/enterprise_info/enterprise_info_search/type/";

        final String There = ".html";
        final String usrId;

        usrId = AppApplication.getCurrentUserInfo().getUserId().toString();


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder()
                            .url(XkOne + type + "/search/" + Keyword + "/page/" + page + There).build();

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

            count = jsonObject.length();

            UPui(count, jsonObject);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void UPui(final int count, final JSONObject jsonObject) {


        String company;
        String type;

        try {
            commonFunList.clear();
            nextPage = jsonObject.getJSONObject("info").getInt("next_page") + "";
            for (int i = 0; i < jsonObject.getJSONObject("info").getJSONArray("data").length(); i++) {
                JSONObject info = jsonObject.getJSONObject("info").getJSONArray("data").getJSONObject(i);
                company = info.getString("name");
                type = info.getString("regLocation");

                yellowPage_info yellowPage = new yellowPage_info();
                yellowPage.setCompanyInfo(company, type);
                commonFunList.add(yellowPage);

            }
            Collections.reverse(commonFunList);
            Message msg = new Message();
            msg.what = UPDATE;
            handler.sendMessage(msg);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}





