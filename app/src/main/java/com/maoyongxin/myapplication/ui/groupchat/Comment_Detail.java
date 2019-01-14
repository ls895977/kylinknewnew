package com.maoyongxin.myapplication.ui.groupchat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.PictureEntity;
import com.maoyongxin.myapplication.entity.huifuInfo;
import com.maoyongxin.myapplication.http.response.CountInfo;
import com.maoyongxin.myapplication.http.response.DynamicComment;
import com.maoyongxin.myapplication.myapp.AppActivity;
import com.maoyongxin.myapplication.myapp.AppConfig;
import com.maoyongxin.myapplication.server.widget.SelectableRoundedImageView;
import com.maoyongxin.myapplication.ui.JiGuangSharePlatformModel;
import com.maoyongxin.myapplication.ui.StrangerDetailActivity;
import com.maoyongxin.myapplication.ui.adapter.DoubleDynamicPicAdapter;
import com.maoyongxin.myapplication.ui.adapter.GridDynamicPicAdapter;
import com.maoyongxin.myapplication.ui.adapter.HuifuRecycle;
import com.maoyongxin.myapplication.ui.adapter.SingleDynamicPicAdapter;
import com.maoyongxin.myapplication.ui.fragment.ShareDialogFragment;
import com.maoyongxin.myapplication.ui.widget.mylistview.AntGrideVIew;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

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
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;


import static android.view.View.GONE;

public class Comment_Detail extends AppActivity implements ShareDialogFragment.Listener {

    @BindView(R.id.dunamicheader)
    SelectableRoundedImageView dunamicheader;
    @BindView(R.id.dynamic_name)
    TextView dynamicName;
    @BindView(R.id.dynamic_company)
    TextView dynamicCompany;
    @BindView(R.id.tv_creatTime)
    TextView tvCreatTime;
    @BindView(R.id.dynamic_content)
    EmojiconTextView dynamicContent;

    @BindView(R.id.dynamic_pics)
    AntGrideVIew dynamicPics;

    @BindView(R.id.img_delete)
    ImageView imgDelete;
    @BindView(R.id.numplun)
    TextView numplun;
    @BindView(R.id.numdzan)
    TextView numdzan;
    @BindView(R.id.dynamicList)
    RecyclerView dynamicList;

    @BindView(R.id.bt_Dynamic)
    CardView btDynamic;
    @BindView(R.id.cv_toDetail)
    CardView cvToDetail;
    @BindView(R.id.ll_info)
    LinearLayout llInfo;
    @BindView(R.id.two_DynamicPics)
    AntGrideVIew twoDynamicPics;
    @BindView(R.id.single_img)
    AntGrideVIew singleImg;
    @BindView(R.id.collapsingtoolbar)
    CollapsingToolbarLayout collapsingtoolbar;
    @BindView(R.id.appBar)
    AppBarLayout appBar;
    @BindView(R.id.img_zan)
    ImageView imgZan;
    @BindView(R.id.ll_zan)
    LinearLayout llZan;
    @BindView(R.id.ll_share)
    LinearLayout llShare;
    @BindView(R.id.huati_toolbar)
    Toolbar huatiToolbar;
    @BindView(R.id.img_share)
    ImageView imgShare;
    @BindView(R.id.numshare)
    TextView numshare;
    @BindView(R.id.ll_shareview)
    LinearLayout llShareview;
    @BindView(R.id.all_view)
    CoordinatorLayout allView;
    @BindView(R.id.ll_appinfo)
    LinearLayout llAppinfo;
    @BindView(R.id.show_detail)
    TextView showDetail;
    @BindView(R.id.img_pinlun)
    ImageView imgPinlun;

    private String huifuUser;
    private String huifuTime;
    private String huifuCoutent;
    private String huifuId;
    private String huifuUserHead;
    private int huifuZan;
    private int huifuCai;
    private String huifuUserId;
    private int UPDATETWO = 1;
    private List<huifuInfo> huifuInfoList = new ArrayList<>();
    private String usrName, companyName, time, praiseNum, treadNum, commentNum, is_praise_tread, headImg, Contentdetail, CommunityId, videoString, parentUserId,huifuuserid,huifuusername;
    private int nzan, nplun;
    private GridDynamicPicAdapter adapter;

    private ArrayList<String> picsurl = new ArrayList<>();

    private List<PictureEntity> imgPics = new ArrayList<>();


    private SingleDynamicPicAdapter sigleAdapter;
    private DoubleDynamicPicAdapter doubleDynamicPicAdapter;
    private List<DynamicComment> dynamicCommentList;
    private HuifuRecycle huifuAdapter;
    private ArrayList<JiGuangSharePlatformModel> list = new ArrayList<>();
    String shareimg;
    Bitmap shareBit;
    private String DynamicInfo = "http://st.3dgogo.com/index/user_dynamic/get_user_dynamic_post_api.html";
    private String APi_zan = "http://st.3dgogo.com/index/user_dynamic/set_user_praise_num_api.html";
    private Handler handler;
    private int UPDATEZAN = 0;
    private String dynamicId, communityName;
    private int updateone = 5;
    List<String> imglist = new ArrayList<String>();
    private int type = 1;

    @Override
    protected int bindLayout() {
        return (R.layout.activity_dynamic_detail);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//白底黑字+沉浸式导航栏
        ButterKnife.bind(this);
        dynamicList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        dynamicList.setItemAnimator(null);
        dynamicList.setHasFixedSize(true);
        dynamicList.setNestedScrollingEnabled(false);
        huifuAdapter = new HuifuRecycle(huifuInfoList, Comment_Detail.this,dynamicId,CommunityId);

        dynamicList.setAdapter(huifuAdapter);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == UPDATETWO) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            huifuAdapter = new HuifuRecycle(huifuInfoList, Comment_Detail.this,dynamicId,CommunityId);
                            dynamicList.setAdapter(huifuAdapter);

                        }
                    }, 10);


                } else if (msg.what == UPDATEZAN) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            numdzan.setText(nzan + "");
                            dianzna("dynamic_id", dynamicId + "");

                        }
                    }, 10);
                } else if (msg.what == updateone) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            switch (imgPics.get(0).getType()) {
                                case 1:
                                    type = 1;
                                    switch (picsurl.size()) {
                                        case 1:
                                            sigleAdapter = new SingleDynamicPicAdapter(getActivity(), imgPics, picsurl);
                                            singleImg.setAdapter(sigleAdapter);
                                            dynamicPics.setVisibility(GONE);
                                            twoDynamicPics.setVisibility(GONE);

                                            break;
                                        case 2:
                                            doubleDynamicPicAdapter = new DoubleDynamicPicAdapter(getActivity(), imgPics, picsurl);
                                            twoDynamicPics.setAdapter(doubleDynamicPicAdapter);
                                            dynamicPics.setVisibility(GONE);
                                            singleImg.setVisibility(GONE);

                                            break;
                                        default:
                                            adapter = new GridDynamicPicAdapter(getActivity(), imgPics, picsurl);
                                            dynamicPics.setAdapter(adapter);
                                            singleImg.setVisibility(GONE);
                                            twoDynamicPics.setVisibility(GONE);

                                            break;
                                    }
                                    break;

                                case 2:
                                    type = 2;
                                    switch (picsurl.size()) {
                                        case 1:
                                            sigleAdapter = new SingleDynamicPicAdapter(getActivity(), imgPics, picsurl);
                                            getshareImg(imgPics.get(0).getImgUrl());
                                            singleImg.setAdapter(sigleAdapter);
                                            dynamicPics.setVisibility(GONE);
                                            twoDynamicPics.setVisibility(GONE);
                                            break;
                                        case 2:
                                            doubleDynamicPicAdapter = new DoubleDynamicPicAdapter(getActivity(), imgPics, picsurl);
                                            twoDynamicPics.setAdapter(doubleDynamicPicAdapter);
                                            dynamicPics.setVisibility(GONE);
                                            singleImg.setVisibility(GONE);
                                            break;
                                        default:
                                            adapter = new GridDynamicPicAdapter(getActivity(), imgPics, picsurl);
                                            dynamicPics.setAdapter(adapter);
                                            singleImg.setVisibility(GONE);
                                            twoDynamicPics.setVisibility(GONE);
                                            break;
                                    }
                                    break;
                            }


                        }
                    }, 10);
                }


            }
        };


    }


    @Override
    protected void initData() {

        dynamicId = getIntent().getStringExtra("dynamicId");
        communityName = getIntent().getStringExtra("communityName");
        CommunityId = getIntent().getStringExtra("community_id");
        parentUserId = getIntent().getStringExtra("parentUserId");
        getDynamicInfo();
    }

    @Override
    protected void initView() {


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//白底黑字+沉浸式导航栏

        gethuatiList();

        getDynamicCount();


    }

    @Override
    protected void onResume() {
        super.onResume();
        gethuatiList();
        getDynamicCount();
    }

    @Override
    protected void initEvent() {
        btDynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Comment_Detail.this, Dynamic_huifu.class);
                intent.putExtra("DynamicId", getIntent().getStringExtra("dynamicId"));
                intent.putExtra("parentId", "DynamicId");
                intent.putExtra("parentUserId", parentUserId);
                intent.putExtra("CommunityId", CommunityId);
                startActivity(intent);
                //动态回复
            }
        });

        llZan.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                imgZan.setImageResource(R.mipmap.gooded_big);
                dianzna("dynamic_id", dynamicId + "");
            }
        });

        llShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShareDialog();
            }
        });

        showDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), StrangerDetailActivity.class);
               intent.putExtra("personId", parentUserId);
                startActivity(intent);
            }
        });



    }


    private void getDynamicCount() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .addInterceptor(loggingInterceptor).build();

                RequestBody requestBody = new FormBody.Builder()
                        .add("dynamicId", dynamicId)
                        .build();
                final Request request = new Request.Builder()
                        .url("http://st.3dgogo.com/index/user_dynamic/get_user_dynamic_post_num.html")
                        .post(requestBody)
                        .build();

                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String content = response.body().string();
                        //  Log.d("------", content);
                        final CountInfo countInfo = new Gson().fromJson(content, CountInfo.class);
                        if (countInfo.getCode() == 200) {
                            Log.d("---", "请求点赞数量失败");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    nzan = countInfo.getInfo().getPraiseNum();
                                    nplun = countInfo.getInfo().getCommentNum();
                                    numdzan.setText(nzan + "");
                                    numplun.setText(nplun + "");
                                }
                            });
                        }
                    }
                });
            }
        }).start();
    }

    private void gethuatiList() {//post服务器数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                try {
                    RequestBody requestBody = new FormBody.Builder()
                            .add("dynamicId", dynamicId)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://st.3dgogo.com/index/user_dynamic/get_user_dynamic_post_api.html")
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
                                    JSONArray data = jsonObject.getJSONArray("info");
                                    huifuInfoList.clear();
                                    for (int i = 0; i < data.length(); i++) {
                                        huifuInfo huifuinfo = new huifuInfo();
                                        JSONObject hfJsong = data.getJSONObject(i);

                                        huifuUser = parse_Value(hfJsong, "userName");
                                        huifuCoutent = parse_Value(hfJsong, "postContent");
                                        huifuTime = parse_Value(hfJsong, "times");
                                        huifuId = parse_Value(hfJsong, "id");
                                        huifuUserHead = parse_Value(hfJsong, "userImg");
                                        huifuUserId = parse_Value(hfJsong, "userId");

                                        huifuCai = hfJsong.getInt("treadNum");
                                        huifuZan = hfJsong.getInt("praiseNum");

                                        huifuuserid = parse_Value(hfJsong, "parentUserId");
                                        huifuusername= parse_Value(hfJsong, "parentUserName");

                                        huifuinfo.sethuifuInfo("dynamic_post_id", huifuUserHead, huifuUser, huifuId, huifuTime, huifuCoutent, huifuCai, huifuZan, huifuUserId,huifuusername,huifuuserid);
                                        huifuInfoList.add(huifuinfo);
                                        Log.d("---", huifuInfoList.size() + "");

                                    }

                                    Message msg = new Message();
                                    msg.what = UPDATETWO;
                                    handler.sendMessage(msg);

                                } else if (jsonObject.getInt("code") == 500) {

                                    //  isUseable=false;
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
                            .add("user_id", "")
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
                                    getDynamicCount();

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

    private void getDynamicInfo() {
        OkHttpUtils.get().url("http://st.3dgogo.com/index/user_dynamic/getUserDynamicDetailsApi.html")
                .addParams("dynamicId", dynamicId)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject date = new JSONObject(response).getJSONObject("info");


                    usrName = parse_Value(date, "userName");
                    companyName = parse_Value(date, "companyName");
                    time = parse_Value(date, "createTime");
                    praiseNum = parse_Value(date, "praiseNum");
                    treadNum = parse_Value(date, "companyName");
                    commentNum = parse_Value(date, "commentNum");
                    is_praise_tread = parse_Value(date, "is_praise_tread");
                    headImg = parse_Value(date, "headImg");
                    Contentdetail = parse_Value(date, "dynamicContent");
                    videoString = parse_Value(date, "video");

                    if(headImg.startsWith("http"))
                    {
                        Glide.with(getActivity()).load(headImg).into(dunamicheader);
                    }
                    else
                    {
                      //  headImg= AppConfig.sRootUrl+"/logincontroller/getHeadImg/"+headImg;
                        Glide.with(getActivity()).load(AppConfig.sRootUrl+"/logincontroller/getHeadImg/"+headImg).into(dunamicheader);
                    }
                    dynamicName.setText(usrName);
                    tvCreatTime.setText(time);


                    dynamicContent.setText(new String (Base64.decode(Contentdetail.getBytes(), android.util.Base64.DEFAULT)));
                    dynamicCompany.setText(communityName);


                    String img = date.getString("img");

                    String[] a = img.split(",");

                    for (int i = 0; i < a.length; i++) {
                        String b = a[i].trim();
                        imglist.add(b);
                    }


                    if (imglist != null) {

                        for (int j = 0; j < imglist.size(); j++) {
                            PictureEntity pictureEntity = new PictureEntity(1);
                            pictureEntity.setImgUrl(imglist.get(j));
                            picsurl.add(imglist.get(j));
                            if (videoString != null && !videoString.equals("")) {
                                pictureEntity.setType(2);
                                pictureEntity.setVideoUrl(videoString);
                                pictureEntity.setTitle(Contentdetail);
                            }
                            imgPics.add(pictureEntity);
                        }

                        Message msg = new Message();
                        msg.what = updateone;
                        handler.sendMessage(msg);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onSharePlatformClicked(int position) {
        Toast.makeText(this, "正在准备分享...", Toast.LENGTH_SHORT).show();
        JiGuangSharePlatformModel model = list.get(position);

        switch (model.getPlatFormType()) {
            case WE_CHAT:
                shareWeChat(type);
                break;
            case WE_CHAT_MOMNETS:
                shareWeChatMoments(type);
                break;
        }
    }

    private void shareWeChat(int type) {
        try {
            JShareInterface.share(Wechat.Name, generateParams(type), new PlatActionListener() {
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

    private void shareWeChatMoments(int type) {
        JShareInterface.share(WechatMoments.Name, generateParams(type), null);
    }

    private void showShareDialog() {
        list.clear();
        List<String> successPlatform = JShareInterface.getPlatformList();

        for (String s : successPlatform) {

            JShareInterface.isClientValid(s);
            if (s.equals("WechatFavorite")) {
                continue;
            }
            list.add(new JiGuangSharePlatformModel(s));

        }
        ShareDialogFragment dialogFragment = ShareDialogFragment.newInstance(list);
        dialogFragment.show(getSupportFragmentManager(), "tag");
    }

    private ShareParams generateParams(int sharetype) {

        ShareParams shareParams = new ShareParams();
        switch (sharetype) {
            case 1:

                shareParams.setShareType(Platform.SHARE_IMAGE);
                shareBit = convertViewToBitmap(allView);
                shareParams.setImageData(shareBit);


                break;
            case 2:
                shareParams.setShareType(Platform.SHARE_VIDEO);
                shareParams.setTitle(dynamicContent.getText().toString());
                shareParams.setText("加入彼信商业社区，跟踪实时商业动态");
                shareParams.setUrl(imgPics.get(0).getVideoUrl());
                shareParams.setImageData(shareBit);
                break;

        }


        return shareParams;
    }

    private void getshareImg(final String share_img) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    shareBit = Glide.with(getActivity())
                            .load(share_img)
                            .asBitmap()
                            .centerCrop()
                            .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
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


