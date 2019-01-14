package com.maoyongxin.myapplication.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.ui.adapter.VpAdapter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

public class ShowPictureDialogActivity extends AppCompatActivity {

    private String url;
    private String thumbUrl = "";
    private String title = "";
   private ArrayList<String> arrayimgs;

    private int type;//1--图片,2--视频


    @BindView(R.id.img_picture)
    ImageView imgPicture;

    @BindView(R.id.activity_show_picture_dialog)
    RelativeLayout activityShowPictureDialog;

    @BindView(R.id.videocontroller1)
    JCVideoPlayer videoPlayer;

    @BindView(R.id.img_switcher)
    ViewPager viewPager;





    private List<String> urlList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_picture_dialog);
        ButterKnife.bind(this);



        getIntentData();
        if (type == 1) {
            initPicEnv();
        } else if (type == 2) {
            initVideoEnv();
        }
    }




    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    private void getIntentData() {
         arrayimgs = getIntent().getStringArrayListExtra("imagList");
         url = getIntent().getStringExtra("resource");
         type = getIntent().getIntExtra("type", 0);
         thumbUrl = getIntent().getStringExtra("thumb");
         title = getIntent().getStringExtra("title");


    }


    private void initPicEnv() {
        ArrayList<Fragment> list = new ArrayList<>();
        for (int i=0;i < arrayimgs.size();i++)
        {
            ItemFragment itemFragment = new ItemFragment();
            Bundle bundle = new Bundle();
            bundle.putString("key",arrayimgs.get(i));
            itemFragment.setArguments(bundle);
            list.add(itemFragment);
        }

        VpAdapter vpAdapter = new VpAdapter(getSupportFragmentManager(),list );
        viewPager.setAdapter(vpAdapter);
        videoPlayer.setVisibility(View.GONE);
        imgPicture.setVisibility(View.GONE);



        activityShowPictureDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initVideoEnv() {
        videoPlayer.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.GONE);
        imgPicture.setVisibility(View.GONE);
       // Log.e("+++++++++", url);
        videoPlayer.setThumbImageViewScalType(ImageView.ScaleType.FIT_XY);
        videoPlayer.setSkin();
        videoPlayer.setUp(url, thumbUrl, title);
        videoPlayer.onClick(videoPlayer.ivStart);

    }
}
