package com.maoyongxin.myapplication.ui.news.view.flipview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.ui.news.data.NewsItem;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NormalItem extends FrameLayout implements NewsItemView {
    private static final String TAG = "NormalItem";

    private int width;

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.type)
    TextView type;
    @Nullable
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.show_bottom)
    ImageButton showBottom;
    @BindView(R.id.like)
    ImageButton likeButton;

    public NormalItem(Context context) {
        super(context);
    }

    public NormalItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NormalItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void bind(final NewsItem gankItem, final Listener listener) {
        if(gankItem != null) {
            title.setText(gankItem.desc);
            type.setText(gankItem.type);

            if (image != null) {
                if (TextUtils.isEmpty(gankItem.getImage())) {
                    image.setVisibility(GONE);
                } else {
                    image.setVisibility(VISIBLE);

                    if (width == 0) {
                        width = (int) (getResources().getDisplayMetrics().density * 100);
                    }

                    Glide.with(getContext())
                            .load(gankItem.getImage() + "?imageView2/1/w/" + width)
                            .placeholder(R.drawable.drawable_placeholder)
                            .centerCrop()
                            .into(image);
                }
            }

            if (gankItem.like) {
                likeButton.setImageResource(R.drawable.bt_like);
            } else {
                likeButton.setImageResource(R.drawable.bt_unlike);
            }
        } else {
            title.setText(null);
            type.setText(null);
            if (image!= null) {
                image.setVisibility(GONE);
            }
        }

        showBottom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.showBottomSheet(gankItem);
            }
        });

        likeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gankItem.like = !gankItem.like;
                if (gankItem.like) {
                    likeButton.setImageResource(R.drawable.bt_like);
                } else {
                    likeButton.setImageResource(R.drawable.bt_unlike);
                }
                listener.like(gankItem);
            }
        });

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.open(gankItem);
            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }
}
