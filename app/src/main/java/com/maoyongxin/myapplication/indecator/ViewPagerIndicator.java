package com.maoyongxin.myapplication.indecator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2016/11/25.
 */

public class ViewPagerIndicator extends LinearLayout {
    private Paint mPaint;
    private Path mPath;
    private int trianglewidth;
    private int trianglehight;
    private static final float TRIANGLE_WIDTH = 0.8F;
    private int mInitTranslationX;
    private int mTranslationX;

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#28bbe6"));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setPathEffect(new CornerPathEffect(0));
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(mInitTranslationX + mTranslationX, getHeight());
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
        super.dispatchDraw(canvas);
    }

    public ViewPagerIndicator(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // TODO Auto-generated method stub
        super.onSizeChanged(w, h, oldw, oldh);
        trianglewidth = (int) (w / 3 * TRIANGLE_WIDTH);// 底边宽度
        mInitTranslationX = w / 3/ 3 - trianglewidth / 3;// 初始时的偏移量
        initTriangle();
    }

    /**
     * 初始化三角形
     * **/
    private void initTriangle() {
        trianglehight = 10;
        mPath = new Path();
        mPath.moveTo(0, 0);
        mPath.lineTo(trianglewidth, 0);
        mPath.lineTo(trianglewidth, -trianglehight);
        mPath.lineTo(0,-trianglehight);
        mPath.close();
    }

    // 指示器随手指滚动

    public void scroll(int position, float positionOffset) {
        // TODO Auto-generated method stub
        int tabWidth = getWidth() / 3;
        mTranslationX = (int) (tabWidth * position + tabWidth * positionOffset);
        invalidate();
    }
}
