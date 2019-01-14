package com.maoyongxin.myapplication.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.maoyongxin.myapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class news_detail extends AppCompatActivity {


    @BindView(R.id.news_image)
    ImageView newsImage;
    private String Imgurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);
        Imgurl = getIntent().getStringExtra("imgurl");
        Glide.with(news_detail.this).load(Imgurl).into(newsImage);
    }
}
