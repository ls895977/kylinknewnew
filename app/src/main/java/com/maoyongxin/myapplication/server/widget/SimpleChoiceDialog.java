package com.maoyongxin.myapplication.server.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jky.baselibrary.util.common.ScreenUtil;
import com.maoyongxin.myapplication.R;

import java.util.ArrayList;

public class SimpleChoiceDialog extends Dialog implements View.OnClickListener {

    private ArrayList<String> mItems = new ArrayList<>();
    private TextView mTvCancel;
    private LinearLayout mLinearLayout;
    private ArrayList<View> mVItems = new ArrayList<>();
    private OnChoiceListener mOnChoiceListener;

    private SimpleChoiceDialog(Context context) {
        super(context, R.style.PopupDialog);
    }

    public SimpleChoiceDialog(Context context, ArrayList<String> items) {
        this(context);
        this.mItems.addAll(items);
        setContentView(R.layout.dialog_simple_choice);
        initView();
        initEvent();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(lp);
    }

    private void initView() {
        mTvCancel = (TextView) findViewById(R.id.SimpleChoice_tv_cancel);
        mLinearLayout = (LinearLayout) findViewById(R.id.SimpleChoiceDialog_ll);
        initItems();
    }

    private void initItems() {
        mLinearLayout.removeAllViews();
        for (String str : mItems) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_simple_choice, null);
            mVItems.add(view);
            TextView tv = (TextView) view.findViewById(R.id.SimpleChoice_Tv);
            tv.setText(str);
            if (mLinearLayout.getChildCount() > 0)
                mLinearLayout.addView(newDividerLine());
            view.setOnClickListener(this);
            mLinearLayout.addView(view);
        }
    }

    private View newDividerLine() {
        View view = new View(getContext());
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtil.dip2Px(getContext(), 0.67f));
        view.setLayoutParams(lp);
        view.setBackgroundResource(R.color.background);
        return view;
    }

    private void initEvent() {
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setOnChoiceListener(OnChoiceListener onChoiceListener) {
        mOnChoiceListener = onChoiceListener;
    }

    @Override
    public void onClick(View v) {
        dismiss();
        if (mOnChoiceListener != null)
            mOnChoiceListener.onChoice(mVItems.indexOf(v));
    }

    public interface OnChoiceListener {
        void onChoice(int index);
    }
}
