package com.maoyongxin.myapplication.ui.news.view.flipview;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public class GridItemDecoration extends RecyclerView.ItemDecoration {
    private int dimen;

    public GridItemDecoration(Context context) {
        dimen = (int) (context.getResources().getDisplayMetrics().density * 1);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (position % 2 == 0) {
            outRect.right += dimen;
        } else {
            outRect.left += dimen;
        }

        if (position > 1) {
            outRect.top += dimen;
        }

        if (position / 2 < state.getItemCount() / 2) {
            outRect.bottom += dimen;
        }
    }
}
