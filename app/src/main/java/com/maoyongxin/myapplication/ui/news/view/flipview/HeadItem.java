package com.maoyongxin.myapplication.ui.news.view.flipview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.ui.news.data.NewsItem;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HeadItem extends FrameLayout implements NewsItemView {

    private static final String TAG = "HeadItem";

    @BindView(R.id.image)
    ImageView image;

    public HeadItem(Context context) {
        super(context);
    }

    public HeadItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeadItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void bind(NewsItem gankItem, Listener listener) {
        if (gankItem != null) {
            if (!TextUtils.isEmpty(gankItem.getImage())) {
                Glide.with(getContext())
                        .load(gankItem.getImage())
                        .centerCrop()
                        .into(image);
            } else {
              //  Log.e(TAG, "no image " + new Gson().toJson(gankItem));
            }
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }
}
