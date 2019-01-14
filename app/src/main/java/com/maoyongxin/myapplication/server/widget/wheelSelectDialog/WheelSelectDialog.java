package com.maoyongxin.myapplication.server.widget.wheelSelectDialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.maoyongxin.myapplication.R;


/**
 * Created by admin on 2017/3/6.
 */

public class WheelSelectDialog extends Dialog implements WheelView.OnWheelChangedListener, View.OnClickListener {
    WheelView wheelView;
    TextView tv_title;
    OnWheelSelectedListener onWheelSelectedListener;
    OnDialogCancelListener onDialogCancelListener;
    String[] itemDatas;//传入的第一个字段
    String itemInfo = "";//单位提示，为空时只显示datas[]的值
    String[] datas;

    public WheelSelectDialog(Context context, String[] itemDatas, String itemInfo) {
        super(context, R.style.Transparent);
        this.itemDatas = itemDatas;
        this.itemInfo = itemInfo;
        initViews();
    }

    public void showCancel(boolean isShow) {
        if (isShow) {
            findViewById(R.id.cancel).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.cancel).setVisibility(View.GONE);
        }
    }

    public void setTtile(String title) {
        this.tv_title.setText(title);
    }

    public void setOnWheelSelectedListener(OnWheelSelectedListener onWheelSelectedListener) {
        this.onWheelSelectedListener = onWheelSelectedListener;
    }

    public void setOnDialogCancelListener(OnDialogCancelListener onDialogCancelListener) {
        this.onDialogCancelListener = onDialogCancelListener;
    }

    private void initViews() {

        setContentView(R.layout.dlg_number_select);

        findViewById(R.id.sure).setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        wheelView = (WheelView) findViewById(R.id.number_wheel);
        initDatas();
        wheelView.setBackgroundResource(Color.TRANSPARENT);
        wheelView.setAdapter(new ArrayWheelAdapter<String>(datas));
        wheelView.setVisibleItems(3);
        wheelView.setCyclic(false);
        wheelView.setCurrentItem(0);

        wheelView.addChangingListener(this);
        getWindow().setWindowAnimations(android.R.style.Animation_InputMethod);
        setParams();
        setCancelable(false);
        setCanceledOnTouchOutside(false);

    }

    private void initDatas() {
        datas = new String[itemDatas.length];
        if (null != itemInfo && !itemInfo.equals("")) {
            for (int i = 0; i < itemDatas.length; i++) {
                datas[i] = itemDatas[i] + itemInfo;
            }
        } else {
            for (int i = 0; i < itemDatas.length; i++) {
                datas[i] = itemDatas[i];
            }
        }
//        for (int i = 0; i < itemDatas.length; i++) {
//            if (datas[i].length() > 20) {
//                StringBuffer sb = new StringBuffer(datas[i]);
//                sb.delete(20, datas[i].length());
//                sb.append("...");
//                datas[i] = sb.toString();
//            }
//        }
    }

    private void setParams() {
        WindowManager.LayoutParams lay = getWindow().getAttributes();
        lay.width = getContext().getResources().getDisplayMetrics().widthPixels;
    }

    private void setCurrentSelect() {
        this.wheelView.setCurrentItem(0);
    }


    @Override
    public void show() {
        setCurrentSelect();
        super.show();
    }


    private String getInfo(int value) {
        return itemDatas[value];
    }

    private int getIndex(int value) {
        return value;
    }


    private int[] selectIndex = new int[1];

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel.getId() == R.id.number_wheel) {
            selectIndex[0] = newValue;
        }

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sure) {
            dismiss();
            if (onWheelSelectedListener != null) {

                onWheelSelectedListener.onSelect(getInfo(selectIndex[0]), getIndex(selectIndex[0]));
            }
        }
        if (v.getId() == R.id.cancel) {
            dismiss();
            if (null != onDialogCancelListener) {
                onDialogCancelListener.onCacnel();
            }
        }
    }

    public interface OnWheelSelectedListener {
        void onSelect(String selected, int index);
    }

    public interface OnDialogCancelListener {
        void onCacnel();
    }
}