package com.maoyongxin.myapplication.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
import com.maoyongxin.myapplication.entity.huifuInfo;
import com.maoyongxin.myapplication.myapp.AppActivity;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.server.widget.HeaderViewPager;
import com.maoyongxin.myapplication.server.widget.SelectableRoundedImageView;
import com.maoyongxin.myapplication.ui.fragment.HeaderViewPagerFragment;
import com.maoyongxin.myapplication.ui.groupchat.GroupHuatiFragmentMine;
import com.maoyongxin.myapplication.ui.groupchat.PinlunFragment;
import com.maoyongxin.myapplication.ui.groupchat.SecendPinlunFragment;
import com.maoyongxin.myapplication.ui.groupchat.huatiDetail_new;
import com.maoyongxin.myapplication.ui.huati.Fabu;
import com.maoyongxin.myapplication.ui.widget.BaseActivity;
import com.maoyongxin.myapplication.ui.widget.GroupMore;
import com.maoyongxin.myapplication.ui.widget.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import io.github.rockerhieu.emojicon.EmojiconEditText;
import io.github.rockerhieu.emojicon.EmojiconTextView;
import io.rong.imkit.RongIM;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class Secent_HuifuList extends BaseActivity {
    public List<HeaderViewPagerFragment> fragments;
    private SecendPinlunFragment groupHuatiFragment = new SecendPinlunFragment();
    private GroupHuatiFragmentMine groupHuatiFragmentmine = new GroupHuatiFragmentMine();
    private HeaderViewPager scrollableLayout;
    private EmojiconTextView titleBar_title, emo_holdername, emotv_title, etvcontent;
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
    private String huatiTile, huaticontent, picUrl, first_headimg;
    private String huifuUser;
    private String huifuTime;
    private String huifuCoutent;
    private String huifuHead;
    private String huifuId;
    private int huifuZan;
    private int huifuCai;
    private String groupId, parentUserId, holdername, contentImgurl,parent_id;
    private SelectableRoundedImageView roundeimguserhead, roudimg_head;
    private Handler handler;
    private TextView FabuTime;
    private Button bt_response_holer;
    private EmojiconEditText emtv_response_holer;
    private CardView cardroudimg_head;
    private FrameLayout myFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//白底黑字+沉浸式导航栏
        setContentView(R.layout.activity_secent__huifu_list);
        gamitId = getIntent().getStringExtra("gambit_id");
        groupId = getIntent().getStringExtra("GroupId");
        groupId = getIntent().getStringExtra("groupNum");
        parentUserId = getIntent().getStringExtra("parentUserId");
        holdername = getIntent().getStringExtra("holdername");
        huatiTile = getIntent().getStringExtra("huatiTitle");
        huaticontent = getIntent().getStringExtra("holdercontent");
        picUrl = getIntent().getStringExtra("img_userHead");
        first_headimg = getIntent().getStringExtra("first_headimg");
        parent_id=getIntent().getStringExtra("parent_id");
        fragments = new ArrayList<>();
        fragments.add(groupHuatiFragment);//话题列表，
        fragments.add(groupHuatiFragmentmine);//我的话题
        //    im_userhead=(SelectableRoundedImageView)findViewById(R.id.im_userhead);
        bt_response_holer = (Button) findViewById(R.id.bt_response_holer);
        FabuTime = (TextView) findViewById(R.id.fabutime);
        etvcontent = (EmojiconTextView) findViewById(R.id.etvcontent);
        toMygroup = (ImageView) findViewById(R.id.toMygroup);
        emo_holdername = (EmojiconTextView) findViewById(R.id.emo_holdername);
        scrollableLayout = (HeaderViewPager) findViewById(R.id.scrollableLayout);
        titleBar = findViewById(R.id.titleBar);
        titleBar_Bg = titleBar.findViewById(R.id.bg);
        emotv_title = (EmojiconTextView) findViewById(R.id.emotv_title);
        back = (ImageView) findViewById(R.id.back);
        roundeimguserhead = (SelectableRoundedImageView) findViewById(R.id.roundeimguserhead);
        content_img = (ImageView) findViewById(R.id.content_img);
        status_bar_fix = titleBar.findViewById(R.id.status_bar_fix);
        status_bar_fix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.getStatusHeight(this)));
        titleBar_title = (EmojiconTextView) titleBar.findViewById(R.id.title);
        emtv_response_holer = (EmojiconEditText) findViewById(R.id.emtv_response_holer);
        roudimg_head = (SelectableRoundedImageView) titleBar.findViewById(R.id.roudimg_head);
        cardroudimg_head = (CardView) titleBar.findViewById(R.id.cardroudimg_head);
        // titleBar_Bg.setAlpha(0);
        //   status_bar_fix.setAlpha(0);
        cardroudimg_head.setAlpha(0);
        titleBar_title.setAlpha(0);
        pagerHeader = (ImageView) findViewById(R.id.pagerHeader);
        myFragment = findViewById(R.id.huifu_list_fragmetn);
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        ViewPager viewPager = (ViewPager) findViewById(R.id.vp_viewPager);
        viewPager.setAdapter(new ContentAdapter(getSupportFragmentManager()));
        tabs.setViewPager(viewPager);
        scrollableLayout.setCurrentScrollableContainer(fragments.get(0));
        emo_holdername.setText(holdername);
        try {
            etvcontent.setText(new String(Base64.decode(getIntent().getStringExtra("holdercontent").getBytes(), Base64.DEFAULT)));
        } catch (IllegalArgumentException exception) {
            etvcontent.setText(getIntent().getStringExtra("holdercontent"));
        }
        Glide.with(Secent_HuifuList.this).load(first_headimg).into(roundeimguserhead);
        Glide.with(Secent_HuifuList.this).load(first_headimg).into(roudimg_head);
        contentImgurl = getIntent().getStringExtra("contentImg") + "";
        Glide.with(Secent_HuifuList.this).load(contentImgurl).into(content_img);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                scrollableLayout.setCurrentScrollableContainer(fragments.get(position));
                if (position == 0) {
                    myFragment.setVisibility(View.VISIBLE);
                } else {
                    myFragment.setVisibility(View.GONE);
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
                //  titleBar_Bg.setAlpha(alpha);
                //注意头部局的颜色也需要改变
                //      status_bar_fix.setAlpha(alpha);
                cardroudimg_head.setAlpha(alpha);
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
                if (msg.what == UPDATETWO) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {


                            FabuTime.setText(fabutime);
                            // fabu_ren.setText(hostname);


                        }
                    }, 50);


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
        OkHttpUtils.post().url("http://st.3dgogo.com/index/chatgroup_gambit/set_respond")
                .addParams("gambit_id", gamitId)
                .addParams("group_id", groupId)
                .addParams("uid", AppApplication.getCurrentUserInfo().getUserId() + "")
                .addParams("parent_id", parent_id)
                .addParams("parentUserId", parentUserId)
                .addParams("content", str)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            //无线程post请求
            @Override
            public void onResponse(String response, int id) {
                Toast.makeText(Secent_HuifuList.this, "回复成功", Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new EventMessage<String>(10, ""));
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
    }

    private void getHuattiList(final String GroupId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Message msg = new Message();
                    msg.what = UPDATETWO;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

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
                GroupMore groupMore = new GroupMore(Secent_HuifuList.this);
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
