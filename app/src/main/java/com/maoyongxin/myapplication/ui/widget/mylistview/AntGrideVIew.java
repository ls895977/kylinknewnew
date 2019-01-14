package com.maoyongxin.myapplication.ui.widget.mylistview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by maoyongxin on 2017/12/26.
 */

public class AntGrideVIew extends GridView {

    public AntGrideVIew(Context context) {
        super(context);
    }

    public AntGrideVIew(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AntGrideVIew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
