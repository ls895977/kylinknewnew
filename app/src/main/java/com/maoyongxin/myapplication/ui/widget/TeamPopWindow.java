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
import com.maoyongxin.myapplication.ui.CommunityMessageActivity;
import com.maoyongxin.myapplication.ui.editapp.minefragment.CompanyshowDefine;
import com.maoyongxin.myapplication.ui.editapp.minefragment.fwDetail;
import com.maoyongxin.myapplication.ui.editapp.minefragment.xiangqingye;


public class TeamPopWindow extends PopupWindow {

    @SuppressLint("InflateParams")
    public TeamPopWindow(final Activity context,
                         final String noticeId,
                         final Boolean isSuperManager,
                         final Boolean isManager,
                         final Boolean hasadvertise,
                         final String myCommunityName,
                         final String shangjiaInfo) {
        LayoutInflater inflater = (LayoutInflater) context
                                  .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.teampopwindow, null);

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


        RelativeLayout requestInfo = (RelativeLayout) content.findViewById(R.id.questInfo);
        RelativeLayout editTeam = (RelativeLayout) content.findViewById(R.id.edit_team);
        RelativeLayout miro_web = (RelativeLayout) content.findViewById(R.id.miro_web);
        requestInfo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //添加好友
                Intent intent = new Intent(context, CommunityMessageActivity.class);
                intent.putExtra("communityId", noticeId);
                if (isSuperManager) {
                    intent.putExtra("type", "superManager");
                    context.startActivity(intent);
                } else {
                    if (isManager) {
                        intent.putExtra("type", "manager");
                        context.startActivity(intent);
                    } else {
                       // Toast.makeText(CommunityDetailActivity.class,"对不起，仅对管理员开放",Toast.LENGTH_SHORT).show();
                    }
                }
                TeamPopWindow.this.dismiss();

            }

        });


        editTeam.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        miro_web.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hasadvertise) {
                    Intent intent = new Intent(context, fwDetail.class);
                    intent.putExtra("companyName", myCommunityName);
                    context.startActivity(intent);
                } else if (hasadvertise) {
                    Intent intent = new Intent(context, xiangqingye.class);
                    intent.putExtra("shangjiaInfo", shangjiaInfo);
                    intent.putExtra("myCommunityId", noticeId);
                    context.startActivity(intent);
                }
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
