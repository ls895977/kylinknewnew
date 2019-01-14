package com.maoyongxin.myapplication.ui.widget;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.ui.GroupMessageActivity;


public class GroupPopWindow extends PopupWindow {

    @SuppressLint("InflateParams")
    public GroupPopWindow(final Activity context,final String groupId) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View content = inflater.inflate(R.layout.group_pop, null);

        // 设置SelectPicPopupWindow的View
        this.setContentView(content);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);

        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPreview);


        RelativeLayout add_quest = (RelativeLayout) content.findViewById(R.id.add_quest);
        RelativeLayout group_setting = (RelativeLayout) content.findViewById(R.id.group_setting);
        RelativeLayout group_help = (RelativeLayout) content.findViewById(R.id.group_help);
        add_quest.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //添加好友
                Intent intent=new Intent(context, GroupMessageActivity.class);
                intent.putExtra("groupId",groupId);
                context.startActivity(intent);

            }

        });
        group_setting.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


            }

        });
        group_help.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent, 0, 0);
        } else {
            this.dismiss();
        }
    }
}
