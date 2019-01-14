package com.maoyongxin.myapplication.ui.news;


import android.content.Context;
import android.content.Intent;

public class ShareHelper {

    public static void shareText(Context context, String title, String desc, String url) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, desc + "\n" + url);
        sendIntent.setType("text/plain");
        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(Intent.createChooser(sendIntent, title));
    }
}
