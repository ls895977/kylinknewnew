package com.maoyongxin.myapplication.ui.news.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.server.widget.SelectableRoundedImageView;
import com.maoyongxin.myapplication.ui.editapp.editadapter.SquareAdapter;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jiguang.share.android.api.JShareInterface;
import cn.jiguang.share.android.api.PlatActionListener;
import cn.jiguang.share.android.api.Platform;
import cn.jiguang.share.android.api.ShareParams;
import cn.jiguang.share.wechat.Wechat;
import cn.jiguang.share.wechat.WechatMoments;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.maoyongxin.myapplication.ui.news.activity.Contant.MeUrl2;

public class fragment1Activity extends AppCompatActivity {

    @BindView(R.id.finish)
    ImageView finish;

    @BindView(R.id.touxiang)
    ImageView touxiang;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.peice)
    TextView peice;
    @BindView(R.id.contant)
    TextView contant;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.pinglun)
    TextView pinglun;
    @BindView(R.id.newsTitle)
    TextView newsTitle;

    @BindView(R.id.news_img)
    SelectableRoundedImageView newsImg;


    @BindView(R.id.liuyana)
    TextView liuyana;
    @BindView(R.id.recycle)
    RecyclerView recycle;
    @BindView(R.id.kuang)
    EditText kuang;
    @BindView(R.id.send)
    Button send;
    @BindView(R.id.activity_detali_azh)
    LinearLayout activityDetaliAzh;
    @BindView(R.id.idk)
    WebView idk;

    @BindView(R.id.sv_all)
    ScrollView svall;

    @BindView(R.id.icon_back)
    ImageView iconback;

    @BindView(R.id.fenxiang)
    LinearLayout fenxiang;

    private String id1;
    private String uid, news_Title;
    private String news_Img;
    private List<plbean.ObjBean.NewsCommentListBean> newsCommentList;
    private String content;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        readed(id1 + "");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment1);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//白底黑字+沉浸式导航栏
        ButterKnife.bind(this);
        initview();
        init1();

        pinglun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                svall.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        iconback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initview() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id1 = bundle.getString("id1");
        uid = bundle.getString("uid");
        news_Title = bundle.getString("newstitle");
        content = bundle.getString("content");
        news_Img = bundle.getString("news_Img");
        //Log.e("content", "initview: " + content);
        WebSettings webSettings = idk.getSettings();
        webSettings.setJavaScriptEnabled(true);


        idk.loadDataWithBaseURL(null, "<html><head><style>img {width:100%;height:auto;margin:auto;}</style></head><body>" + content + "</body></html>", "text/html", "utf-8", null);

        fenxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(fragment1Activity.this);
                builder.setTitle("分享")
                        .setIcon(R.mipmap.wechat_moment)
                        .setNegativeButton("微信好友", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                request(0);
                            }
                        });
                builder.setPositiveButton("朋友圈", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        request(1);
                    }
                });
                builder.show();
            }
        });
    }

    public String getURLEncoderString(String str) {//url编码
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void request(final int type) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id1);
        map.put("content", getURLEncoderString("<img src= '"+news_Img+"'/>"+content));


        Log.e("wwwwwwwww", map + "");
        OkHttpUtils.post().url("http://360.d1.natapp.cc/Index/AddIntex").params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                Log.e("dddddddddddddddd", response);
                String url = "http://360.d1.natapp.cc/Index/index?id=" + response;
                switch (type) {
                    case 0:
                        shareWeChat(url);
                        break;
                    case 1:
                        shareWeChatMoments(url);
                        break;
                }

            }
        });
    }

    private void shareWeChat(String url) {
        try {
            JShareInterface.share(Wechat.Name, generateParams(url), new PlatActionListener() {
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

    private void shareWeChatMoments(String url) {
        JShareInterface.share(WechatMoments.Name, generateParams(url), null);
    }

    private ShareParams generateParams(String url) {
        ShareParams shareParams = new ShareParams();
        shareParams.setShareType(Platform.SHARE_WEBPAGE);
        shareParams.setTitle(news_Title);
        shareParams.setUrl(url);
        shareParams.setText(getIntent().getStringExtra("source"));
        Resources res = fragment1Activity.this.getResources();
        Bitmap bmp = BitmapFactory.decodeResource(res, R.mipmap.app_icon);
        shareParams.setImageData(bmp);
//        shareParams.setImagePath(news_Img);

        return shareParams;
    }

    public void send(View v) {

        String s = kuang.getText().toString();

        if (s.isEmpty()) {
            Toast.makeText(fragment1Activity.this, "输入内容不能为空", Toast.LENGTH_LONG).show();
        }
        String userId = AppApplication.getCurrentUserInfo().getUserId();
        String ss = "http://118.24.2.164:8089/";
        String url = ss + "newscommentcontroller/submitNewsComment";

        OkHttpUtils.post().url(url).addParams("newsId", id1)
                .addParams("uid", userId + "")
                .addParams("content", AppApplication.getCurrentUserInfo().getUserName() + "\n" + "\n" + s + "")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                // Log.e("///acp", "onResponse: " + e);
            }

            @Override
            public void onResponse(String response, int id) {
                // Log.e("///acp", "onResponse: " + response);
                Toast.makeText(fragment1Activity.this, "留言成功", Toast.LENGTH_LONG).show();
                kuang.setText("");
                init1();
            }
        });


    }

    private void init1() {
        String outward = MeUrl2;
        String url = outward + "newscommentcontroller/getNewsComment";
        OkHttpUtils.post().url(url).addParams("newsId", id1 + "")
                .build().execute(new StringCallback() {


            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                //Log.e("/pl", "onResponse: " + response);
                Gson gson = new Gson();
                plbean liuyanBean = gson.fromJson(response, plbean.class);
                newsCommentList = liuyanBean.getObj().getNewsCommentList();

                liuyana.setText("评论总数" + newsCommentList.size());
                pinglun.setText("" + newsCommentList.size());
                newsTitle.setText(news_Title);
                Glide.with(fragment1Activity.this).load(news_Img).into(newsImg);
                recy();

            }
        });
    }

    private void recy() {
        recycle.setLayoutManager(new LinearLayoutManager(fragment1Activity.this));
        manager roomAdapter = new manager();
        recycle.setAdapter(roomAdapter);
        roomAdapter.notifyDataSetChanged();
    }


    public class manager extends RecyclerView.Adapter {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(fragment1Activity.this).inflate(R.layout.manage, parent, false);
            return new ViewHolder(view);

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder myViewHolder = (ViewHolder) holder;

            myViewHolder.content.setText(newsCommentList.get(position).getContent());
            String headimg = newsCommentList.get(position).getHeadimg();
            Log.e("sssssssss", headimg + "===" + newsCommentList.get(position).getContent() + "===" + newsCommentList.get(position).getCreateTime());
            Glide.with(getApplicationContext())
                    .load("http://118.24.2.164:8083" + "/headImg/" + headimg)
                    .into(myViewHolder.touxaing);
//            myViewHolder.time.setText(newsCommentList.get(position).getCreateTime() + "");

            myViewHolder.pl_time.setVisibility(View.VISIBLE);
            myViewHolder.pl_time.setText(simpleDateFormat.format(newsCommentList.get(position).getCreateTime()));
        }

        @Override
        public int getItemCount() {
            return newsCommentList == null ? 0 : newsCommentList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.touxaing)
            ImageView touxaing;
            @BindView(R.id.name)
            TextView name;
            @BindView(R.id.time)
            TextView time;
            @BindView(R.id.content)
            TextView content;
            //            @BindView(R.id.pl_time)
            TextView pl_time;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
                pl_time = (TextView) view.findViewById(R.id.pl_time);
            }
        }
    }


    private void deletele() {
        OkHttpUtils.post().url(MeUrl2 + "newscontroller/getNewsDetail")
                .addParams("newsId", id1 + "")

                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                //Log.e("//ac", "onError: " + e);
            }

            @Override
            public void onResponse(String response, int id) {
                //Log.e("//ac", "onError: " + response);
                Gson gson = new Gson();
                activityBean activityBean = gson.fromJson(response, activityBean.class);
                com.maoyongxin.myapplication.ui.news.activity.activityBean.ObjBean.NewsDetailBean newsDetail = activityBean.getObj().getNewsDetail();
                title.setText(newsDetail.getTitle());
                time.setText(activityBean.getObj().getNewsDetail().getCreateTime() + "");
                peice.setText(newsDetail.getAbstractc());
//                String image1 = newsDetail.getNewsClassify().getImage();

                if (image != null) {
//                    Glide
//                            .with(fragment1Activity.this)
//                            .load(Contant.MeUrl2 + image1)
//                            .into(image);
                    Resources resources = getBaseContext().getResources();
                    Drawable imageDrawable = resources.getDrawable(R.drawable.lunbo1); //图片在drawable文件夹下
                    image.setBackgroundDrawable(imageDrawable);
                } else {
                    Resources resources = getBaseContext().getResources();
                    Drawable imageDrawable = resources.getDrawable(R.drawable.lunbo1); //图片在drawable文件夹下
                    image.setBackgroundDrawable(imageDrawable);

                }

            }
        });


    }

    private void readed(final String newId) {

        final String One = "http://st.3dgogo.com/index/news/set_read_post_num_api.html";

        //get阅读量
        new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClient okHttpClient = new OkHttpClient();
                try {

                    RequestBody requestBody = new FormBody.Builder()
                            .add("id", newId)
                            .add("status", "3")
                            .build();

                    Request request = new Request.Builder()
                            .url(One)
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

                                } else if (jsonObject.getInt("code") == 500) {


                                }
                            } catch (Exception e) {
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
    }

}
