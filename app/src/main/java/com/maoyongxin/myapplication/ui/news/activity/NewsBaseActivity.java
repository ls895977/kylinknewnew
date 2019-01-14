package com.maoyongxin.myapplication.ui.news.activity;

import android.support.v7.app.AppCompatActivity;


public abstract class NewsBaseActivity extends AppCompatActivity {
    public abstract void showInfo(String info);
    public abstract void setLoading(boolean isLoading);
}
