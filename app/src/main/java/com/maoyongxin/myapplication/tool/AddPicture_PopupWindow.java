package com.maoyongxin.myapplication.tool;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.maoyongxin.myapplication.R;

public class AddPicture_PopupWindow extends PopupWindow {

    private LinearLayout up_1_btn, up_2_btn, dmiss_btn;
    private View mMenuView;

    public AddPicture_PopupWindow(Activity context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.addpicture_popupwindow, null);
        up_1_btn = (LinearLayout) mMenuView.findViewById(R.id.up_1_btn);
        up_2_btn = (LinearLayout) mMenuView.findViewById(R.id.up_2_btn);
        dmiss_btn = (LinearLayout) mMenuView.findViewById(R.id.dmiss_btn);
        // 取消按钮
        dmiss_btn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // 销毁弹出框
                dismiss();
            }
        });
        // 设置按钮监听
//        camera.setOnClickListener(itemsOnClick);
//        photoAlbum.setOnClickListener(itemsOnClick);
        // 设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.FILL_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
         this.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        up_1_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (popClickListener != null) {
                    popClickListener.btn1();
                }
                dismiss();
            }
        });
        up_2_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if(popClickListener!=null){
                    popClickListener.btn2();
                }
                dismiss();
            }
        });

    }

    public PopClickListener popClickListener;

    public void setClick(PopClickListener popClickListener) {
        this.popClickListener = popClickListener;
    }

    public interface PopClickListener {
        public void btn1();

        public void btn2();
    }

}
