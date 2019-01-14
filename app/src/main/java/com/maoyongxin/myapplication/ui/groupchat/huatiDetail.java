package com.maoyongxin.myapplication.ui.groupchat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
//import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.request.target.Target;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.huifuInfo;
import com.maoyongxin.myapplication.myapp.AppActivity;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.server.widget.SelectableRoundedImageView;
import com.maoyongxin.myapplication.ui.JiGuangSharePlatformModel;
import com.maoyongxin.myapplication.ui.adapter.huifuAdapter;
import com.maoyongxin.myapplication.ui.fragment.ShareDialogFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jiguang.share.android.api.JShareInterface;
import cn.jiguang.share.android.api.PlatActionListener;
import cn.jiguang.share.android.api.Platform;
import cn.jiguang.share.android.api.ShareParams;
import cn.jiguang.share.wechat.Wechat;
import cn.jiguang.share.wechat.WechatMoments;
import io.github.rockerhieu.emojicon.EmojiconTextView;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import static android.view.View.GONE;
import static com.maoyongxin.myapplication.ui.JiGuangSharePlatformModel.PlatFormType.WE_CHAT;
import static com.maoyongxin.myapplication.ui.JiGuangSharePlatformModel.PlatFormType.WE_CHAT_MOMNETS;

public class huatiDetail extends AppActivity  implements ShareDialogFragment.Listener{


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
    @BindView(R.id.ll_top)
    LinearLayout llTop;

    private TextView huati_title;

    private EmojiconTextView message_text_r,huati_content;
    private String img_head;
    private String fabutime;
    private String hostname;
    private String gamitId;
    private String huatiTile, huaticontent;
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
    private String username, userId;
    private int picCode;
    private String huifuUserId;
    private TextView fabu_ren, fabu_time, huatititle, numcomment, numZan;
    private ImageView contentImg,img_share;
    private String contentImgurl, parentUserId;
    private LinearLayout ll_zan;
    private SelectableRoundedImageView user_Img;
    private String APi_zan = "http://st.3dgogo.com/index/user_dynamic/set_user_praise_num_api.html";
    private ImageView im_good;
    private  int colorcode;
    private Bitmap shareBit;
    private ArrayList<JiGuangSharePlatformModel> list = new ArrayList<>();
    private LinearLayout llAppinfo,all_view;
    @Override
    protected int bindLayout() {
        return (R.layout.activity_huati_content);
    }
    //话题详情
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
                Intent intent = new Intent(huatiDetail.this, huatihuifu.class);
                final TextView tvName = (TextView) view.findViewById(R.id.tv_huifu_user);
                final TextView tvuserId = (TextView) view.findViewById(R.id.userid);
                username = tvName.getText().toString();
                userId = tvuserId.getText().toString();
                intent.putExtra("gambit_id", getIntent().getStringExtra("gambit_id"));
                intent.putExtra("username", "@" + username);
                intent.putExtra("groupId", groupId);
                intent.putExtra("parentUserId", userId);
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
                            huati_content.setText(new String (Base64.decode(huaticontent.getBytes(), Base64.DEFAULT)));//64解码

                            if (img_head.equals("")) {
                                user_Img.setVisibility(GONE);

                            } else {
                                Glide.with(getActivity()).load(img_head).into(user_Img);
                            }

                            fabu_time.setText(fabutime);

                            fabu_ren.setText(hostname);
                        }
                    }, 50);


                } else if (msg.what == UPDATETWO) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {


                            huifuAdapter = new huifuAdapter(getActivity(), huifuInfoList);
                            huifuList.setAdapter(huifuAdapter);
                        }
                    }, 1);

                }
            }
        };
        contentImgurl = getIntent().getStringExtra("contentImg").toString();

    }

    @Override
    protected void initData() {

        gamitId = getIntent().getStringExtra("gambit_id");
        groupId = getIntent().getStringExtra("GroupId");
        parentUserId = getIntent().getStringExtra("parentUserId");
        holdername = getIntent().getStringExtra("holdername");
        huatiTile = getIntent().getStringExtra("huatiTitle");
        huaticontent = getIntent().getStringExtra("huatiContent");





    }

    private int colorFromBitmap(final String string) {

        final int NUMBER_OF_PALETTE_COLORS = 24;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap myBitmap = Glide.with(huatiDetail.this)
                            .load(string)
                            .asBitmap()
                            .centerCrop()
                            .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return colorcode;
    }

    @Override
    protected void initView() {
        View huatiHead = View.inflate(getApplicationContext(), R.layout.head_huati, null);
        huati_content = (EmojiconTextView) huatiHead.findViewById(R.id.huati_content);
        huati_content.setVisibility(GONE);
        huatititle = (TextView) huatiHead.findViewById(R.id.huati_title);
        fabu_ren = (TextView) huatiHead.findViewById(R.id.host_name);
        fabu_time = (TextView) huatiHead.findViewById(R.id.fabu_time);
        contentImg = (ImageView) huatiHead.findViewById(R.id.contentImg);
        message_text_r = (EmojiconTextView) huatiHead.findViewById(R.id.message_text_r);
        numcomment = (TextView) huatiHead.findViewById(R.id.numcomment);
        user_Img = (SelectableRoundedImageView) huatiHead.findViewById(R.id.usrhead);
        ll_zan = (LinearLayout) huatiHead.findViewById(R.id.ll_zan);
        im_good = (ImageView) huatiHead.findViewById(R.id.im_good);
        numZan = (TextView) huatiHead.findViewById(R.id.numZan);
        img_share=(ImageView)huatiHead.findViewById(R.id.img_share);
        llAppinfo=(LinearLayout)huatiHead.findViewById(R.id.llAppinfo);
        contentImgurl = getIntent().getStringExtra("contentImg").toString();
        all_view=(LinearLayout)huatiHead.findViewById(R.id.all_view);
        Glide.with(getActivity()).load(img_head).into(user_Img);




        numcomment.setText(getIntent().getStringExtra("postNum"));
        numZan.setText(getIntent().getStringExtra("zanNum"));

        huatititle.setText(huatiTile);
//        huati_content.setText(huaticontent);
        message_text_r.setVisibility(View.VISIBLE);
        Log.e("sssssssssssssss",huaticontent+"================");

        String str2 = new String(Base64.decode(huaticontent.getBytes(), Base64.DEFAULT));
        message_text_r.setText(str2);
        if (!contentImgurl.equals("")) {
            Glide.with(getActivity()).load(contentImgurl).into(contentImg);
        } else {
            contentImg.setVisibility(GONE);
        }

        huifuList.addHeaderView(huatiHead);


        gethuatiList();
        getHuattiList(gamitId);


    }

    @Override
    protected void initEvent() {
        super.initEvent();
        btHuifu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(huatiDetail.this, huatihuifu.class);
                intent.putExtra("gambit_id", getIntent().getStringExtra("gambit_id"));
                intent.putExtra("username", "");
                intent.putExtra("groupId", groupId);
                intent.putExtra("parentUserId", parentUserId);

                startActivityForResult(intent, 0);
            }
        });

        ll_zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dianzna("gambit_id", gamitId + "");
                im_good.setImageResource(R.mipmap.gooded_big);
                int numzan = Integer.valueOf(numZan.getText() + "") + 1;
                numZan.setText(numzan + "");
                im_good.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.zan_anim));


            }
        });

        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showShareDialog();

            }
        });
    }

    private void dianzna(final String zanType, final String dianzanID) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                        .build();
                try {

                    RequestBody requestBody = new FormBody.Builder()
                            .add("type", "1")
                            .add("user_id", AppApplication.getCurrentUserInfo().getUserId())
                            .add("zdm", zanType)
                            .add(zanType, dianzanID)
                            .build();

                    Request request = new Request.Builder()
                            .url(APi_zan)
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
                                    //getDynamicCount();

                                } else if (jsonObject.getInt("code") == 500) {


                                }
                            } catch (Exception e) {
                                e.printStackTrace();


                            }
                        } catch (IOException e) {
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

    private void getHuattiList(final String GroupId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                GetHuati(GroupId);
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

    private void parseJson(final String Jsondata) {

        try {
            JSONObject jsonObject = new JSONObject(Jsondata);
            JSONObject data = jsonObject.getJSONObject("info");

            huaticontent = (data.getString("content"));
            img_head = (parseUrl(data.getString("headImg")));
            fabutime = (data.getString("create_time"));
            hostname = (data.getString("userName"));

            Message msg = new Message();
            msg.what = UPDATEONE;
            handler.sendMessage(msg);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String parseUrl(String imgUrl) {
        String rep = "\\";
        final String ImgUrl = imgUrl.replace(rep, "");


        return ImgUrl;
    }

    private void gethuatiList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                try {

                    RequestBody requestBody = new FormBody.Builder()
                            .add("gambit_id", gamitId)
                            .build();

                    Request request = new Request.Builder()
                            .url("http://st.3dgogo.com/index/chatgroup_gambit/get_respond")
                            .post(requestBody)
                            .build();


                    try {
                        Call call = okHttpClient.newCall(request);
                        //判断请求是否成功
                        try {
                            Response response = call.execute();

                            try {

                                JSONObject jsonObject = new JSONObject(response.body().string());

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

                                        huifuinfo.sethuifuInfo("respond_id", huifuHead, huifuUser, huifuId, huifuTime, huifuCoutent, huifuCai, huifuZan, huifuUserId,"","");
                                        huifuInfoList.add(huifuinfo);


                                    }

                                    Message msg = new Message();
                                    msg.what = UPDATETWO;
                                    handler.sendMessage(msg);

                                } else if (jsonObject.getInt("code") == 500) {
                                    Message msg = new Message();
                                    msg.what = UPDATETWO;
                                    handler.sendMessage(msg);
                                    //  isUseable=false;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                //  showMessagefail("深度解析失败3");


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

    }//标准post

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
        gethuatiList();
        super.onResume();

    }


    @Override
    public void onSharePlatformClicked(int position) {
        Toast.makeText(this, "正在准备分享...", Toast.LENGTH_SHORT).show();
        JiGuangSharePlatformModel model = list.get(position);

        switch (model.getPlatFormType()) {
            case WE_CHAT:
                shareWeChat();
                break;
            case WE_CHAT_MOMNETS:
                shareWeChatMoments();
                break;
        }
    }

    private void shareWeChat() {
        try {
            JShareInterface.share(Wechat.Name, generateParams(), new PlatActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                    Log.d("---", "onComplete:" + i);
                }

                @Override
                public void onError(Platform platform, int i, int i1, Throwable throwable) {
                    Log.e("----", "platform:" + i + "____" + platform.getName() + throwable.getMessage());
                }

                @Override
                public void onCancel(Platform platform, int i) {
                    Log.e("---", "onCancel:" + i);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void shareWeChatMoments() {
        JShareInterface.share(WechatMoments.Name, generateParams(), null);
    }

    private void showShareDialog() {
        list.clear();
        List<String> successPlatform = JShareInterface.getPlatformList();

        for (String s : successPlatform) {
            //Log.e("---", "===>>" + s + "===>>" + JShareInterface.isClientValid(s));
            JShareInterface.isClientValid(s);
            if (s.equals("WechatFavorite")) {
                continue;
            }
            list.add(new JiGuangSharePlatformModel(s));

        }
        ShareDialogFragment dialogFragment = ShareDialogFragment.newInstance(list);
        dialogFragment.show(getSupportFragmentManager(), "tag");
    }

    private ShareParams generateParams() {
        ShareParams shareParams = new ShareParams();
        shareParams.setShareType(Platform.SHARE_IMAGE);
        shareBit=convertViewToBitmap(all_view);
        shareParams.setImageData(shareBit);

        return shareParams;
    }

    private Bitmap convertViewToBitmap(View view) {

        llAppinfo.setVisibility(View.VISIBLE);
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();  //启用DrawingCache并创建位图
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache()); //创建一个DrawingCache的拷贝，因为DrawingCache得到的位图在禁用后会被回收
        view.setDrawingCacheEnabled(false);
        llAppinfo.setVisibility(View.INVISIBLE);
        return bitmap;
    }

}
