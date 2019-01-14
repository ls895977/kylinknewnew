package com.maoyongxin.myapplication.server.widget.wheelSelectDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.maoyongxin.myapplication.R;

import java.util.ArrayList;
import java.util.List;


public class SimplePickerDialog extends Dialog implements View.OnClickListener {

    private TextView mTvCancel;
    private TextView mTvConfirm;
    private Picker mPicker;

    private List<Picker.PickerItem> mItems = new ArrayList<>();

    private int mIndex;
    private String mText;

    private OnResultListener mOnResultListener;

    public SimplePickerDialog(Context context) {
        super(context, R.style.PopupDialog);
        setContentView(R.layout.dialog_simple_picker);
//        setCancelable(false);
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
        mTvCancel = (TextView) findViewById(R.id.select_dialog_tv_title_left);
        mTvConfirm = (TextView) findViewById(R.id.select_dialog_tv_title_right);
        mPicker = (Picker) findViewById(R.id.SimplePickerDialog_picker);
    }

    private void initEvent() {
        mTvCancel.setOnClickListener(this);
        mTvConfirm.setOnClickListener(this);
        mPicker.setOnItemPickedListener(new Picker.OnItemPickedListener() {
            @Override
            public void onPick(Picker picker, int index) {
                if (mItems != null && mItems.size() > 0) {
                    mIndex = index;
                    mText = mItems.get(index).getText();
                }
            }
        });
    }

    public void setItems(List<Picker.PickerItem> items) {
        mItems.clear();
        mItems.addAll(items);
        mPicker.setItems(mItems);
    }

    public List<Picker.PickerItem> getItems() {
        return mItems;
    }

    public void setOnResultListener(OnResultListener onResultListener) {
        mOnResultListener = onResultListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_dialog_tv_title_left: {
                dismiss();
                break;
            }
            case R.id.select_dialog_tv_title_right: {
                if (mOnResultListener != null)
                    mOnResultListener.onResult(mIndex, mText);
                dismiss();
                break;
            }
        }
    }

    public interface OnResultListener {
        void onResult(int index, String text);
    }
}
