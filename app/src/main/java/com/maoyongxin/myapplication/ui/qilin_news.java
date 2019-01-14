package com.maoyongxin.myapplication.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.maoyongxin.myapplication.R;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class qilin_news extends AppCompatActivity implements GestureDetector.OnGestureListener {

    // ViewFlipper实例
    ViewFlipper flipper;
    // 定义手势检测实例
    GestureDetector detector;
    // 定义一个动画数组，用于为ViewFlipper指定切换动画效果
    Animation[] animations = new Animation[4];
    // 定义手势动作亮点之间的最小距离
    final int FLIP_DISTANCE = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qilin_news);
        // 创建手势检测器
        detector = new GestureDetector(this, this);
        // 获得ViewFlipper实例
        flipper = (ViewFlipper) this.findViewById(R.id.viewFlipper);
        // 为ViewFlipper添加8个ImageView组件
        flipper.addView(addImageView(R.drawable.rmxw));
        flipper.addView(addImageView(R.drawable.rmxw));
        flipper.addView(addImageView(R.drawable.rmxw));
        flipper.addView(addImageView(R.drawable.rmxw));

        flipper.addView(addImageView(R.drawable.rmxw));
        flipper.addView(addImageView(R.drawable.rmxw));
        flipper.addView(addImageView(R.drawable.rmxw));
        flipper.addView(addImageView(R.drawable.rmxw));



        // 初始化Animation数组
        animations[0] = AnimationUtils.loadAnimation(this, R.anim.left_in);
        animations[1] = AnimationUtils.loadAnimation(this, R.anim.left_out);
        animations[2] = AnimationUtils.loadAnimation(this, R.anim.right_in);
        animations[3] = AnimationUtils.loadAnimation(this, R.anim.right_out);
    }

    // 定义添加ImageView的工具方法
    private View addImageView(int resId) {
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(resId);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        return imageView;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 将该Activity上的触碰事件交给GestureDetector处理
        return detector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // 如果第一个触点事件的X坐标大于第二个触点事件的X坐标超过FLIP_DISTANCE
        // 也就是手势从右向左滑
        if (e1.getX() - e2.getX() > FLIP_DISTANCE) {
            // 为flipper设置切换的动画效果
            flipper.setInAnimation(animations[0]);
            flipper.setOutAnimation(animations[1]);
            flipper.showPrevious();
            return true;
        }
        // 如果第二个触点事件的X坐标大于第一个触点事件的X坐标超过FLIP_DISTANCE
        // 也就是手势从右向左滑
        else if (e2.getX() - e1.getX() > FLIP_DISTANCE) {
            // 为flipper设置切换的动画效果
            flipper.setInAnimation(animations[2]);
            flipper.setOutAnimation(animations[3]);
            flipper.showNext();
            return true;
        }
        return false;
    }
}
