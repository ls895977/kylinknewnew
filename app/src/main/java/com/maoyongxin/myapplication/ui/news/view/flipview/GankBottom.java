package com.maoyongxin.myapplication.ui.news.view.flipview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.ui.news.data.NewsItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class GankBottom extends LinearLayout implements GankItemView {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.url)
    TextView url;
    @BindView(R.id.who)
    TextView who;
    @BindView(R.id.time)
    TextView time;

    public GankBottom(Context context) {
        super(context);
    }

    public GankBottom(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GankBottom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @Override
    public void bind(final NewsItem gankItem, final GankItemView.Listener listener) {
        if (gankItem != null) {
            title.setText(gankItem.desc);
            type.setText(gankItem.type);
            if (gankItem.who == null) {
                who.setVisibility(GONE);
            } else {
                who.setVisibility(VISIBLE);
                who.setText(gankItem.who);
            }
            url.setText(gankItem.url);
            time.setText(gankItem.day);
        }
    }

    @OnClick(R.id.url)
    void openUrl() {

    }
}
