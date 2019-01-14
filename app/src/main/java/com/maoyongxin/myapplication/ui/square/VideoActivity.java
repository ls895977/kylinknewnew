package com.maoyongxin.myapplication.ui.square;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;


import com.maoyongxin.myapplication.R;

import java.io.File;

/**
 * Created by Administrator on 2018-02-07.
 */

public class VideoActivity extends Activity{
    private VideoView play_video;
    private MediaController mediaController;
    private RelativeLayout close;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_activity);
        initView();
    }

    public void initView() {
        close = (RelativeLayout) findViewById(R.id.close);
        play_video = (VideoView) findViewById(R.id.play_video);
        setView();
    }


    public void setView() {
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mediaController = new MediaController(this);
        File file = new File(getIntent().getStringExtra("path"));
        if(file.exists()){
            play_video.setVideoPath(file.getAbsolutePath());
            play_video.setMediaController(mediaController);
            mediaController.setMediaPlayer(play_video);
            play_video.requestFocus();
            play_video.start();
        }
    }
}
