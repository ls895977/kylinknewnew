package com.maoyongxin.myapplication.ui.editapp.findfragment.zhihu.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.maoyongxin.myapplication.R;

import butterknife.OnClick;

public class Dialog_ZhiHuButoom extends BaseDialog {
    OnClick onClick;

//    public Dialog_ZhiHuButoom(Context context, OnClick click) {
//        super(context);
//        this.onClick = click;
//    }
    public Dialog_ZhiHuButoom(Context context) {
        super(context);

    }
    TextView add1, add2, time1, time2;

    @Override
    protected int initLayoutId() {
        return R.layout.dlg_photocll;
    }

    @Override
    protected void initWindow() {
        windowDeploy(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);
    }

    @Override
    protected void initView() {
        setOnClickListener(R.id.cancer);
        setOnClickListener(R.id.center);
        setOnClickListener(R.id.add1);
        add1 = getViewAndClick(R.id.add1);
        add1.setSelected(true);
        add2 = getViewAndClick(R.id.add2);
        time1 = getViewAndClick(R.id.time_1);
        time2 = getViewAndClick(R.id.time_2);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.cancer:
                dismiss();
                break;
            case R.id.center://确认
//                onClick.onItem(1);
                dismiss();
                break;
            case R.id.add1:
                add1.setSelected(true);
                add2.setSelected(false);
                break;
            case R.id.add2:
                add2.setSelected(true);
                add1.setSelected(false);
                break;
            case R.id.time_1:
                time1.setSelected(true);
                time2.setSelected(false);
                break;
            case R.id.time_2:
                time2.setSelected(true);
                time1.setSelected(false);
                break;
        }
    }

//    public interface OnClick {
//        void onItem(int p);
//    }
}
