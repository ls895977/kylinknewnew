package com.maoyongxin.myapplication.ui.widget.swipfreshView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class MRecyclerView extends RecyclerView {

    private OnScrollListener mOnScrollListener;
    private OnScrollEdgeListener mOnScrollEdgeListener;

    public MRecyclerView(Context context) {
        super(context);
    }

    public MRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    {
        super.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (mOnScrollListener != null) {
                    mOnScrollListener.onScrollStateChanged(recyclerView, newState);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (getLayoutManager().canScrollVertically()) {
                    if (mOnScrollEdgeListener != null && !ViewCompat.canScrollVertically(MRecyclerView.this, 1) && dy > 0) {
                        mOnScrollEdgeListener.onScrollBottom();
                    }
                    if (mOnScrollEdgeListener != null && !ViewCompat.canScrollVertically(MRecyclerView.this, -1) && dy < 0) {
                        mOnScrollEdgeListener.onScrollTop();
                    }
                } else if (getLayoutManager().canScrollHorizontally()) {
                    if (mOnScrollEdgeListener != null && !ViewCompat.canScrollHorizontally(MRecyclerView.this, 1) && dx > 0) {
                        mOnScrollEdgeListener.onScrollBottom();
                    }
                    if (mOnScrollEdgeListener != null && !ViewCompat.canScrollHorizontally(MRecyclerView.this, -1) && dx < 0) {
                        mOnScrollEdgeListener.onScrollTop();
                    }
                }
                if (mOnScrollListener != null) {
                    mOnScrollListener.onScrolled(recyclerView, dx, dy);
                }
            }
        });
    }

    @Override
    public void setOnScrollListener(OnScrollListener listener) {
        mOnScrollListener = listener;
    }

    public void setOnScrollEdgeListener(OnScrollEdgeListener listener) {
        mOnScrollEdgeListener = listener;
    }

    public interface OnScrollEdgeListener {
        void onScrollTop();

        void onScrollBottom();
    }
}
