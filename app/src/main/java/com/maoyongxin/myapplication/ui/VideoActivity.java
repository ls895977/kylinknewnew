package com.maoyongxin.myapplication.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.maoyongxin.myapplication.R;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

public class VideoActivity extends AppCompatActivity {
    private String url;
    private String thumbUrl = "";
    private String title = "";
    private int type;//1--图片,2--视频
    private JCVideoPlayer videoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        //隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏状态栏
        //定义全屏参数
        int flag= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);
        setContentView(R.layout.activity_video);
        videoPlayer = (JCVideoPlayer) findViewById(R.id.videoPlayer);
        getIntentData();
        initVideoEnv();
    }

    private void getIntentData() {
        url = getIntent().getStringExtra("resource");
        type = getIntent().getIntExtra("type", 0);
        thumbUrl = getIntent().getStringExtra("thumb");
        title = getIntent().getStringExtra("title");
    }

    private void initVideoEnv() {
        videoPlayer.setVisibility(View.VISIBLE);
        //Log.e("+++++++++", url);
        videoPlayer.setThumbImageViewScalType(ImageView.ScaleType.FIT_XY);
        videoPlayer.setSkin();
        videoPlayer.setUp(url, thumbUrl, title);
        videoPlayer.onClick(videoPlayer.ivStart);
    }
}
