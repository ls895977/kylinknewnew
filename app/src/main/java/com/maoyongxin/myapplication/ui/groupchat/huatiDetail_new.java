package com.maoyongxin.myapplication.ui.groupchat;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lzy.widget.tab.PagerSlidingTabStrip;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.HuatiInfo1;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.server.widget.HeaderViewPager;
import com.maoyongxin.myapplication.server.widget.SelectableRoundedImageView;
import com.maoyongxin.myapplication.ui.EventMessage;
import com.maoyongxin.myapplication.ui.adapter.HuatiRecycleAdapter;
import com.maoyongxin.myapplication.ui.fragment.HeaderViewPagerFragment;
import com.maoyongxin.myapplication.ui.huati.Fabu;
import com.maoyongxin.myapplication.ui.widget.BaseActivity;
import com.maoyongxin.myapplication.ui.widget.GroupMore;
import com.maoyongxin.myapplication.ui.widget.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import io.github.rockerhieu.emojicon.EmojiconEditText;
import io.github.rockerhieu.emojicon.EmojiconTextView;
import io.rong.imkit.RongIM;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class huatiDetail_new extends BaseActivity {
    public List<HeaderViewPagerFragment> fragments;
    private PinlunFragment groupHuatiFragment = new PinlunFragment();
    private GroupHuatiFragmentMine groupHuatiFragmentmine = new GroupHuatiFragmentMine();
    private HeaderViewPager scrollableLayout;
    private EmojiconTextView titleBar_title, emo_holdername, emotv_title, etv_content;
    private View status_bar_fix;
    private View titleBar_Bg;
    private View titleBar;
    private ImageView pagerHeader, back, toMygroup, content_img;
    private int UPDATEONE = 0;
    private int UPDATETWO = 1;
    private String img_head;
    private String fabutime;
    private String hostname;
    private String gamitId;
    private String huatiTile, huaticontent, picUrl;
    private String groupId, parentUserId, holdername, contentImgurl, postNum, zanNum;
    private SelectableRoundedImageView userheadimg, roudimg_head;
    private Handler handler;
    private TextView FabuTime;
    private Button bt_response_holer;
    private EmojiconEditText emtv_response_holer;
    private CardView cardroudimg_head;
    private TextView numcomment, numZan;
    private FrameLayout msg_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//白底黑字+沉浸式导航栏
        setContentView(R.layout.activity_huati_detail_new);
        gamitId = getIntent().getStringExtra("gambit_id");
        groupId = getIntent().getStringExtra("GroupId");
        parentUserId = getIntent().getStringExtra("parentUserId");
        holdername = getIntent().getStringExtra("holdername");
        huatiTile = getIntent().getStringExtra("huatiTitle");
        huaticontent = getIntent().getStringExtra("huatiContent");
        picUrl = getIntent().getStringExtra("img_userHead");
        postNum = getIntent().getStringExtra("postNum");
        zanNum = getIntent().getStringExtra("zanNum");
        msg_show = findViewById(R.id.huatiDetail_Message);
        numcomment = findViewById(R.id.numcomment);
        numcomment.setText(postNum);
        numZan = findViewById(R.id.numZan);
        numZan.setText(zanNum);
        fragments = new ArrayList<>();
        fragments.add(groupHuatiFragment);//话题列表，
        fragments.add(groupHuatiFragmentmine);//我的话题
        bt_response_holer = (Button) findViewById(R.id.bt_response_holer);
        FabuTime = (TextView) findViewById(R.id.fabutime);
        etv_content = (EmojiconTextView) findViewById(R.id.etv_content);
        toMygroup = (ImageView) findViewById(R.id.toMygroup);
        emo_holdername = (EmojiconTextView) findViewById(R.id.emo_holdername);
        scrollableLayout = (HeaderViewPager) findViewById(R.id.scrollableLayout);
        titleBar = findViewById(R.id.titleBar);
        titleBar_Bg = titleBar.findViewById(R.id.bg);
        emotv_title = (EmojiconTextView) findViewById(R.id.emotv_title);
        back = (ImageView) findViewById(R.id.back);
        userheadimg = (SelectableRoundedImageView) findViewById(R.id.im_userhead);
        content_img = (ImageView) findViewById(R.id.content_img);
        status_bar_fix = titleBar.findViewById(R.id.status_bar_fix);
        status_bar_fix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.getStatusHeight(this)));
        titleBar_title = (EmojiconTextView) titleBar.findViewById(R.id.title);
        roudimg_head = (SelectableRoundedImageView) titleBar.findViewById(R.id.roudimg_head);
        emtv_response_holer = (EmojiconEditText) findViewById(R.id.emtv_response_holer);
        cardroudimg_head = (CardView) titleBar.findViewById(R.id.cardroudimg_head);
        cardroudimg_head.setAlpha(0);
        titleBar_Bg.setAlpha(0);
        status_bar_fix.setAlpha(0);
        titleBar_title.setAlpha(0);
        pagerHeader = (ImageView) findViewById(R.id.pagerHeader);
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        ViewPager viewPager = (ViewPager) findViewById(R.id.vp_viewPager);
        viewPager.setAdapter(new ContentAdapter(getSupportFragmentManager()));
        tabs.setViewPager(viewPager);
        scrollableLayout.setCurrentScrollableContainer(fragments.get(0));
        // Glide.with(huatiDetail_new.this).load(picUrl).into(userheadimg);
        //  Glide.with(huatiDetail_new.this).load(picUrl).into(roudimg_head);
        emotv_title.setText(huatiTile);
        emo_holdername.setText(holdername);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                scrollableLayout.setCurrentScrollableContainer(fragments.get(position));
                switch (position) {
                    case 0:
                        msg_show.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        msg_show.setVisibility(View.GONE);
                        break;
                }
            }
        });

        scrollableLayout.setOnScrollListener(new HeaderViewPager.OnScrollListener() {
            @Override
            public void onScroll(int currentY, int maxY) {
                //让头部具有差速动画,如果不需要,可以不用设置
                pagerHeader.setTranslationY(currentY / 2);
                //动态改变标题栏的透明度,注意转化为浮点型
                float alpha = 1.0f * currentY / maxY;
                titleBar_Bg.setAlpha(alpha);
                //注意头部局的颜色也需要改变
                cardroudimg_head.setAlpha(alpha);
                status_bar_fix.setAlpha(alpha);

                titleBar_title.setAlpha(alpha);
                titleBar_title.setText(holdername);
                //  emotv_title.setText(holdername);

            }
        });
        viewPager.setCurrentItem(0);
        getHuattiList(gamitId);
        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == UPDATEONE) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            etv_content.setText(new String(Base64.decode(huaticontent.getBytes(), Base64.DEFAULT)));

                            if (img_head.equals("")) {
                                // user_Img.setVisibility(GONE);

                            } else {
                                Glide.with(huatiDetail_new.this).load(img_head).into(userheadimg);
                                contentImgurl = getIntent().getStringExtra("contentImg") + "";
                                Glide.with(huatiDetail_new.this).load(contentImgurl).into(content_img);
                                Glide.with(huatiDetail_new.this).load(img_head).into(roudimg_head);
                            }

                            FabuTime.setText(fabutime);
                            // fabu_ren.setText(hostname);


                        }
                    }, 50);


                } else if (msg.what == UPDATETWO) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {


                            //  huifuAdapter = new huifuAdapter(huatiDetail_new.this, huifuInfoList);
                            //  huifuList.setAdapter(huifuAdapter);
                        }
                    }, 1);

                }
            }
        };
        bt_response_holer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIbQuerenhuifu();
            }
        });
    }

    private void setIbQuerenhuifu() {
        String s = emtv_response_holer.getText().toString();
        String str = Base64.encodeToString(s.getBytes(), Base64.DEFAULT);
        OkHttpUtils.post()
                .addParams("gambit_id", gamitId)
                .addParams("group_id", groupId)
                .addParams("uid", AppApplication.getCurrentUserInfo().getUserId() + "")
                .addParams("parent_id", "0")
                .addParams("parentUserId", parentUserId)
                .addParams("content", str)
                .url("http://st.3dgogo.com/index/chatgroup_gambit/set_respond").build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Toast.makeText(huatiDetail_new.this, "回复成功", Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(new EventMessage<String>(5, ""));
                        View view = getWindow().peekDecorView();
                        if (view != null) {
                            emtv_response_holer.setText("");
                            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        //刷新评论列表
                        //  finish();
                    }
                });
//        OkHttpUtils.post().url("http://st.3dgogo.com/index/chatgroup_gambit/set_respond")
//                .addParams("gambit_id", gamitId)
//                .addParams("group_id", groupId)
//                .addParams("uid", AppApplication.getCurrentUserInfo().getUserId()+"")
//                .addParams("parent_id","0")
//                .addParams("parentUserId",parentUserId)
//                .addParams("content",str )
//                .build().execute(new StringCallback() {
//            @Override
//            public void onError(Call call, Exception e, int id) {
//
//            }
//            //无线程post请求
//
//            @Override
//            public void onResponse(String response, int id) {
//                Toast.makeText(huatiDetail_new.this,"回复成功",Toast.LENGTH_SHORT).show();
//                EventBus.getDefault().post(new EventMessage<String>(5,""));
//                View view = getWindow().peekDecorView();
//                if (view != null) {
//                    emtv_response_holer.setText("");
//                    InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                }
//                //刷新评论列表
//              //  finish();
//            }
//        });
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

    /**
     * 内容页的适配器
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //当前窗口获取焦点时，才能正真拿到titlebar的高度，此时将需要固定的偏移量设置给scrollableLayout即可
        scrollableLayout.setTopOffset(titleBar.getHeight());
    }

    private class ContentAdapter extends FragmentPagerAdapter {

        public ContentAdapter(FragmentManager fm) {
            super(fm);
        }

        public String[] titles = new String[]{"全部", "我的"};

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }


    @OnClick(R.id.fbht)
    public void onViewClicked() {
        Intent intent = new Intent(getApplication(), Fabu.class);
        intent.putExtra("GroupName", "百乐交流群");
        intent.putExtra("groupId", getIntent().getStringExtra("groupNum"));
        startActivity(intent);
    }

    @OnClick({R.id.jrql})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.jrql:
                String Groupid = getIntent().getStringExtra("groupNum");
                String groupName = getIntent().getStringExtra("groupName");
                RongIM.getInstance().startGroupChat(this, Groupid, groupName);
                break;
            case R.id.toMygroup:
                GroupMore groupMore = new GroupMore(huatiDetail_new.this);
                groupMore.showPopupWindow(toMygroup);
                break;
        }
    }

    @Override
    public void onBackPressed() {

        finishAfterTransition();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_left_out);
    }
}
