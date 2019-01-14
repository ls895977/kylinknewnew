package com.maoyongxin.myapplication.ui.square;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SquareVideoActivity extends AppHandlerActivity {


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
    private List<Button> btnList;
    private List<Fragment> fragList;
    private int prePosition = 0;
    private OnFabuClickListener onFabuClickListener;

    public ProgressDialog mProgressDialog;

    public void setOnFabuClickListener(OnFabuClickListener onFabuClickListener) {
        this.onFabuClickListener = onFabuClickListener;
    }

    public interface OnFabuClickListener {
        void doFabu();
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_square_base;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void handlerPassMsg(int target, int target1, Object obj) {
        switch (target) {
            case HttpNetWorkUtils.UPLOAD_DONGTALVIDEO:


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
            case 0x333:
                Log.e("44444444444444444",String.valueOf(obj));
//            {"allCount":1910800,"size":1910800}
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(obj));
                    String all = jsonObject.getString("allCount");
                    String size = jsonObject.getString("size");
                    mProgressDialog.setMax(Integer.parseInt(all));
                    mProgressDialog.setProgress(Integer.parseInt(size));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void initView() {
        super.initView();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//白底黑字+沉浸式导航栏

        // 弹出进度的dialog
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setProgressNumberFormat(""); //设置左下角不显示
        mProgressDialog.setCancelable(false);


        btnList = new ArrayList<Button>();
        btnList.add(btnEdit);
        btnList.add(btnHistory);
        fragList = new ArrayList<Fragment>();
        fragList.add(SquareVideoFragment.newInstance(getIntent().getStringExtra("file")));
        fragList.add(new SquareHistoryFragment());
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
