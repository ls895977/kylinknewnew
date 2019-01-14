package com.maoyongxin.myapplication.ui;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.holidaycheck.permissify.PermissifyManager;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.http.second.HttpNetWorkUtils;
import com.maoyongxin.myapplication.http.second.XGsonUtil;
import com.maoyongxin.myapplication.indecator.ViewPagerDoubleIndicator;
import com.maoyongxin.myapplication.myapp.AppHandlerActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class fabuhuati extends AppHandlerActivity {


    @BindView(R.id.ll_bar)
    LinearLayout llBar;
    @BindView(R.id.tv_returnBack)
    TextView tvReturnBack;
    @BindView(R.id.btn_edit)
    Button btnEdit;
    @BindView(R.id.btn_history)
    Button btnHistory;
    @BindView(R.id.lin_topindicator)
    ViewPagerDoubleIndicator linTopindicator;
    @BindView(R.id.btn_update)
    Button btnUpdate;
    @BindView(R.id.line_top)
    LinearLayout lineTop;
    @BindView(R.id.vp_myviewPager)
    ViewPager vpMyviewPager;
    @BindView(R.id.activity_square_base)
    LinearLayout activitySquareBase;
    private List<Button> btnList;
    private List<Fragment> fragList;
    private int prePosition = 0;
    private OnFabuClickListener onFabuClickListener;

    public ProgressDialog mProgressDialog;

    public void setOnFabuClickListener(OnFabuClickListener onFabuClickListener) {
        this.onFabuClickListener = onFabuClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    public interface OnFabuClickListener {
        void doFabu();
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_fabuhuati;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void handlerPassMsg(int target, int target1, Object obj) {
        switch (target) {
            case HttpNetWorkUtils.UPLOAD_DONGTAI:
                mProgressDialog.dismiss();
                if (null != obj && !obj.toString().equals("")) {
                    try {
                        JsonObject jsonObject = XGsonUtil.getJsonObject(obj.toString());
                        int code = XGsonUtil.getJsonInt(jsonObject, "code");
                        if (code == 200) {
                            showToastShort("发布成功");
                            initView();
                            btnUpdate.setVisibility(View.GONE);
                            vpMyviewPager.setCurrentItem(1);
                        } else {
                            showToastShort("上传失败");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    showToastShort("服务器未返回数据");
                }
                break;
        }
    }

    @Override
    protected void initView() {
        super.initView();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mProgressDialog = new ProgressDialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setMessage(getString(R.string.processing));
        btnList = new ArrayList<Button>();
        btnList.add(btnEdit);
        btnList.add(btnHistory);
        fragList = new ArrayList<Fragment>();
       // fragList.add(new fabuhuatiFragment());
     //   fragList.add(new huatiHisFragement());
        showFragment();
    }

    public Handler getMyHandler() {
        return myHandler;
    }

    public PermissifyManager getMyPermissifyManager() {
        return getPermissifyManager();
    }

    @OnClick({R.id.tv_returnBack, R.id.btn_edit, R.id.btn_history, R.id.btn_update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_returnBack:
                finish();
                break;
            case R.id.btn_edit:
                btnUpdate.setVisibility(View.VISIBLE);
                vpMyviewPager.setCurrentItem(0);
                break;
            case R.id.btn_history:
                btnUpdate.setVisibility(View.GONE);
                vpMyviewPager.setCurrentItem(1);
                break;
            case R.id.btn_update:
                onFabuClickListener.doFabu();
                break;
        }
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> list;

        public ViewPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return list.get(0);
            } else {
                return list.get(1);
            }
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

    /**
     * 展示fragment
     */
    private void showFragment() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        ViewPagerAdapter vpAdapter = new ViewPagerAdapter(fm, fragList);
        vpAdapter.notifyDataSetChanged();
        vpMyviewPager.setAdapter(vpAdapter);

        /**
         * 指示器联动
         */
        vpMyviewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                linTopindicator.scroll(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    btnUpdate.setVisibility(View.GONE);
                } else {
                    btnUpdate.setVisibility(View.VISIBLE);
                }
                btnList.get(prePosition).setTextColor(Color.parseColor("#80000000"));
                btnList.get(position).setTextColor(Color.parseColor("#80000000"));
                prePosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
