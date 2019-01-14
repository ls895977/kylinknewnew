package com.maoyongxin.myapplication.ui.groupchat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.huifuInfo;
import com.maoyongxin.myapplication.myapp.AppActivity;
import com.maoyongxin.myapplication.server.widget.SelectableRoundedImageView;
import com.maoyongxin.myapplication.ui.adapter.huifuAdapter;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

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

import static android.view.View.GONE;

public class huatiDetailByid extends AppActivity {


    @BindView(R.id.shoucanghuati)
    TextView shoucanghuati;


    @BindView(R.id.huifu_list)
    ListView huifuList;

    @BindView(R.id.host_name)
    TextView hostName;
    @BindView(R.id.fabu_time)
    TextView fabuTime;
    @BindView(R.id.bt_huifu)
    CardView btHuifu;
    @BindView(R.id.usr_head)
    SelectableRoundedImageView usrHead;

    @BindView(R.id.im_bad)
    ImageView imBad;
    @BindView(R.id.numBad)
    TextView numBad;
    @BindView(R.id.im_good)
    ImageView imGood;




    private TextView huati_title, huati_content;


    private String img_head;
    private String fabutime;
    private String hostname;
    private String gamitId;
    private String huaticontent;
    private String huifuUser;
    private String huifuTime;
    private String huifuCoutent;
    private String huifuHead;
    private String huifuId;
    private int huifuZan;
    private int huifuCai;
    private int UPDATEONE = 0;
    private int UPDATETWO = 1;
    private List<huifuInfo> huifuInfoList = new ArrayList<>();
    private huifuAdapter huifuAdapter;
    private Handler handler;
    private String holdername;
    private String groupId;
    private String holderId;
    private String username,userId;
    private String huifuUserId;
    private TextView fabu_ren, fabu_time,huatititle,numZan,numcomment;
    private ImageView contentImg;
    private String contentImgurl,parentUserId;
    private String num_zan,num_comment,huifutitle;
    private SelectableRoundedImageView user_Img;

    @Override
    protected int bindLayout() {
        return (R.layout.activity_huati_content);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//白底黑字+沉浸式导航栏


        huifuList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(huatiDetailByid.this, huatihuifu.class);
                final TextView tvName = (TextView) view.findViewById(R.id.tv_huifu_user);
                final TextView tvuserId = (TextView) view.findViewById(R.id.userid);
                username = tvName.getText().toString();
                userId=tvuserId.getText().toString();
                intent.putExtra("gambit_id", getIntent().getStringExtra("gambit_id"));
                intent.putExtra("username", "@" + username);
                intent.putExtra("groupId", groupId);
                intent.putExtra("parentUserId",userId);
                startActivityForResult(intent, 0);
                return false;
            }
        });
        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == UPDATEONE) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {




                            huati_content.setText( new String(Base64.decode(huaticontent.getBytes(), Base64.DEFAULT)));


                            if (img_head.equals("")) {
                                user_Img.setVisibility(GONE);

                            } else {
                                Glide.with(getActivity()).load(img_head).into(user_Img);
                            }
                            if(contentImgurl.equals(""))
                            {
                                contentImg.setVisibility(GONE);
                            }else {
                                Glide.with(getActivity()).load(contentImgurl).into(contentImg);
                            }

                            huatititle.setText(huifutitle);
                            fabu_time.setText(fabutime);
                            fabu_ren.setText(hostname);

                            numZan.setText(num_zan);
                            numcomment.setText(num_comment);



                        }
                    }, 10);


                } else if (msg.what == UPDATETWO) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            numcomment.setText(num_comment);
                            huifuAdapter = new huifuAdapter(getActivity(), huifuInfoList);
                            huifuList.setAdapter(huifuAdapter);
                        }
                    }, 50);

                }
            }
        };
        //contentImgurl = getIntent().getStringExtra("contentImg").toString();

    }

    @Override
    protected void initData() {


        gamitId = getIntent().getStringExtra("gambit_id");
        groupId=getIntent().getStringExtra("groupId");
        parentUserId=getIntent().getStringExtra("parentUserId");
        GetHuatiByid(gamitId);
        gethuatiList();

    }

    private void GetHuatiByid(String GroupId)
    {
        OkHttpUtils.get().url("http://st.3dgogo.com/index/chatgroup_gambit/get_gambit_details")
                .addParams("id",GroupId)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("info");

                    huaticontent = (data.getString("content"));
                    img_head = (parseUrl(data.getString("headImg")));
                    fabutime = (data.getString("create_time"));
                    hostname = (data.getString("userName"));
                    contentImgurl=(data.getString("img"));
                    num_zan=(data.getString("praise_num"));
                    num_comment=(data.getString("post_num"));
                    huifutitle=(data.getString("title"));

                    Message msg = new Message();
                    msg.what = UPDATEONE;
                    handler.sendMessage(msg);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    protected void initView() {
        View huatiHead = View.inflate(getApplicationContext(), R.layout.head_huati, null);
         huati_content = (TextView) huatiHead.findViewById(R.id.huati_content);
         huatititle=(TextView) huatiHead.findViewById(R.id.huati_title);
         fabu_ren = (TextView) huatiHead.findViewById(R.id.host_name);
         fabu_time = (TextView) huatiHead.findViewById(R.id.fabu_time);
         contentImg = (ImageView) huatiHead.findViewById(R.id.contentImg);
         user_Img = (SelectableRoundedImageView) huatiHead.findViewById(R.id.usrhead);
        numZan=(TextView)huatiHead.findViewById(R.id.numZan);
        numcomment=(TextView)huatiHead.findViewById(R.id.numcomment);


        //contentImgurl = getIntent().getStringExtra("contentImg").toString();

        Glide.with(getActivity()).load(img_head).into(user_Img);


        huati_content.setText(huaticontent);
        huifuList.addHeaderView(huatiHead);




    }

    @Override
    protected void initEvent() {
        super.initEvent();
        btHuifu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(huatiDetailByid.this, huatihuifu.class);
                intent.putExtra("gambit_id", getIntent().getStringExtra("gambit_id"));
                intent.putExtra("username", "");
                intent.putExtra("groupId", groupId);
                intent.putExtra("parentUserId",parentUserId);
                startActivityForResult(intent, 0);
            }
        });


    }






    private String parseUrl(String imgUrl) {
        String rep = "\\";
        final String ImgUrl = imgUrl.replace(rep, "");


        return ImgUrl;
    }

    private void gethuatiList() {

            OkHttpUtils.post().url("http://st.3dgogo.com/index/chatgroup_gambit/get_respond")
                    .addParams("gambit_id", gamitId)
                    .build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {

                }

                @Override
                public void onResponse(String response, int id) {
                    try {

                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.getInt("code") == 200) {
                            JSONArray data = jsonObject.getJSONObject("info").getJSONArray("data");
                            huifuInfoList.clear();
                            for (int i = 0; i < data.length(); i++) {
                                huifuInfo huifuinfo = new huifuInfo();
                                JSONObject hfJsong = data.getJSONObject(i);

                                huifuUser = parse_Value(hfJsong, "userName");
                                huifuCoutent = parse_Value(hfJsong, "content");
                                huifuTime = parse_Value(hfJsong, "create_time");
                                huifuHead = parse_Value(hfJsong, "headImg");
                                huifuId = parse_Value(hfJsong, "id");

                                huifuCai = hfJsong.getInt("tread_num");
                                huifuZan = hfJsong.getInt("praise_num");

                                huifuUserId = parse_Value(hfJsong, "userId");
                                num_comment=i+1+"";
                                huifuinfo.sethuifuInfo("respond_id", huifuHead, huifuUser, huifuId, huifuTime, huifuCoutent, huifuCai, huifuZan, huifuUserId,"","");
                                huifuInfoList.add(huifuinfo);


                            }


                            Message msg = new Message();
                            msg.what = UPDATETWO;
                            handler.sendMessage(msg);

                        } else if (jsonObject.getInt("code") == 500) {

                            //  isUseable=false;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        //  showMessagefail("深度解析失败3");
                    }
                }



                }
            );
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

    @Override
    protected void onResume() {
       // GetHuatiByid(gamitId);
        gethuatiList();

        super.onResume();

    }
}
