package com.maoyongxin.myapplication.ui.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static android.view.Gravity.CENTER;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.ListPopupWindow.MATCH_PARENT;

/**
 * Created by maoyongxin on 2017/11/23.
 */

public class BannerView extends LinearLayout implements ViewPager.OnPageChangeListener {
    private ViewPager mViewPager;
    private BannerAdapter mAdapter;
    private BannerViewListener mListener;
    private int mSelectedPageIndex = 0;
    private LinearLayout mIndicatorView;
    private BannerHandler mHandler;
    private boolean mIsScroll = false;
    private static int MSG_LOOP = 0x0001, LOOP_INTERVAL = 3000;


    private static class BannerHandler extends Handler {
        private WeakReference<BannerView> weakReference = null;

        public BannerHandler(BannerView bannerView) {
            super(Looper.getMainLooper());
            this.weakReference = new WeakReference<BannerView>(bannerView);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (this.weakReference == null) {
                return;
            }
            BannerView bannerView = this.weakReference.get();
            if (bannerView == null || bannerView.mViewPager == null || bannerView.mViewPager.getAdapter() == null || bannerView.mViewPager.getAdapter().getCount() <= 1) {
                return;
            }
            if (!bannerView.mIsScroll) {
                bannerView.mViewPager.setCurrentItem(bannerView.mSelectedPageIndex + 1, true);
            }
        }
    }


    public interface BannerViewListener {
        void onShowImage(ImageView view, String imgUrl);

        void onItemClick(int position, String targetUrl, Object userData);

        View onCreateIndicatorView();
    }

    public BannerView(Context context) {
        this(context, null);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        selectIndicatorView(mAdapter.getDataPosition(mSelectedPageIndex), false);
        mSelectedPageIndex = position;
        selectIndicatorView(mAdapter.getDataPosition(mSelectedPageIndex), true);


    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == 1) {
            mIsScroll = true;
        } else if (state == 2) {
            mIsScroll = false;

            mHandler.removeCallbacksAndMessages(null);
            mHandler.sendEmptyMessageDelayed(MSG_LOOP, LOOP_INTERVAL);
        }
    }

    public BannerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    private void initViews(Context context) {
        // 动态创建对象
        RelativeLayout rl_banner = new RelativeLayout(context);
        rl_banner.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        this.addView(rl_banner, MATCH_PARENT, MATCH_PARENT);

        mViewPager = new ViewPager(context);
        rl_banner.addView(mViewPager, MATCH_PARENT, MATCH_PARENT);
        mIndicatorView = new LinearLayout(context);
        mIndicatorView.setOrientation(HORIZONTAL);
        mIndicatorView.setGravity(CENTER);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp.bottomMargin = (int) dip2px(context, 6);
        mIndicatorView.setLayoutParams(lp);
        rl_banner.addView(mIndicatorView);
        mViewPager.setAdapter(mAdapter = new BannerAdapter(this));
        mViewPager.addOnPageChangeListener(this);

        mHandler = new BannerHandler(this);
        // 启动循环
        mHandler.sendEmptyMessageDelayed(MSG_LOOP, LOOP_INTERVAL);
    }

    public void setBannerViewListener(BannerViewListener l) {
        mListener = l;
    }

    // 设置数据
    public void setBannerDatas(ArrayList<BannerInfo> data) {
        mAdapter.setBannerDatas(data);
        // 移除旧的
        mIndicatorView.removeAllViews();
        // 添加新的
        if (data.size() > 1) {
            for (int i = 0; i < data.size(); i++) {
                if (mListener != null) {
                    mIndicatorView.addView(mListener.onCreateIndicatorView());
                }
            }
        }
        if (data.size() > 1) {
            // 选中新的
            selectIndicatorView(mAdapter.getDataPosition(mSelectedPageIndex), true);
        }
    }

    private void selectIndicatorView(int index, boolean bSelected) {
        View v = mIndicatorView.getChildAt(index);
        if (v != null) {
            v.setSelected(bSelected);
        }
    }

    private void onItemClick() {
        if (mListener != null) {
            BannerInfo info = mAdapter.getItem(mSelectedPageIndex);
            if (info != null) {
                mListener.onItemClick(mAdapter.getDataPosition(mSelectedPageIndex), info.mTargetUrl, info.mUserData);
            }

        }
    }

    private void onShowImage(ImageView v, String url) {
        if (mListener != null) {
            mListener.onShowImage(v, url);
        }
    }


    private float dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (dpValue * scale + 0.5f);
    }


    public static class BannerInfo {
        public String mImgUrl;
        public String mTargetUrl;
        public Object mUserData;


        public BannerInfo(String img, String url) {
            this(img, url, null);
        }

        public BannerInfo(String img, String url, Object userData) {
            mImgUrl = img;
            mTargetUrl = url;
            mUserData = userData;
        }
    }

    private class BannerAdapter extends PagerAdapter implements View.OnClickListener {
        private ArrayList<BannerInfo> mData = new ArrayList<>();
        WeakReference<BannerView> mBannerViewWeakReference;


        public BannerAdapter(BannerView v) {
            mBannerViewWeakReference = new WeakReference<BannerView>(v);
        }

        public void setBannerDatas(ArrayList<BannerInfo> data) {
            mData = data;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mData.size() > 1 ? 100000000 : mData.size();
        }

        @Override
        public boolean isViewFromObject(View v, Object o) {
            return v == o;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView iv = new ImageView(container.getContext());
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setOnClickListener(this);
            container.addView(iv, -1, -1);
            BannerInfo info = getItem(position);

            if (mBannerViewWeakReference.get() != null) {
                mBannerViewWeakReference.get().onShowImage(iv, info.mImgUrl);
            }
            return iv;
        }

        public BannerInfo getItem(int position) {
            if (mData.size() < 1) {
                return null;
            }
            return mData.get(position % mData.size());
        }

        @Override
        public void onClick(View view) {
            Log.i("aaaa", "onClick");
            if (mBannerViewWeakReference.get() != null) {
                mBannerViewWeakReference.get().onItemClick();
            }
        }

        @Override
        public int getItemPosition(Object object) {
            //return super.getItemPosition(object);
            return POSITION_NONE;
        }

        public int getDataPosition(int position) {
            return position % mData.size();
        }
    }
}
