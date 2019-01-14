package com.jky.baselibrary.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jky.baselibrary.LibConfig;
import com.jky.baselibrary.R;
import com.jky.baselibrary.util.common.ScreenUtil;

public class AdsBanner extends RelativeLayout {

    public static final String TAG = AdsBanner.class.getSimpleName();
    public static final int ID = TAG.hashCode();

    private ViewPager mViewPager;
    private TextView mText;
    private RadioGroup mRgIndicators;

    private boolean mShowIndicator;
    private boolean mShowText;
    private int mIndicatorSelector;
    private BannerAdapter mAdapter;
    private boolean mAutoPager;
    private int mPageTimeSet;
    private int mPageTimeUse;
    private int mPageTimeCountDown;
    private Thread mPageThread;
    private int mCurrentPage;

    private int mFlag;

    public AdsBanner(Context context) {
        this(context, null);
    }

    public AdsBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdsBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.widget_ads_banner, this);
        initView();
        setAttrs(context, attrs, defStyleAttr);
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.vp_widget_AdsBanner);
        mRgIndicators = (RadioGroup) findViewById(R.id.rg_widget_AdsBanner);
        mText = (TextView) findViewById(R.id.tv_widget_AdsBanner);
    }

    private void setAttrs(Context context, AttributeSet attrs, int defStyle) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            setLayerType(View.LAYER_TYPE_SOFTWARE, null);//关闭硬件加速
//        }

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AdsBanner);
        int bannerHeight = a.getDimensionPixelSize(R.styleable.AdsBanner_bannerHeight, ScreenUtil.dip2Px(context, 144));
        boolean matchParentHeight = a.getBoolean(R.styleable.AdsBanner_matchParentHeight, false);
        mShowIndicator = a.getBoolean(R.styleable.AdsBanner_showIndicator, false);
        int indicatorGravity = a.getInt(R.styleable.AdsBanner_setIndicatorGravity, 1);
        mIndicatorSelector = a.getResourceId(R.styleable.AdsBanner_setIndicatorSelector, R.drawable.selector_dot);
        mShowText = a.getBoolean(R.styleable.AdsBanner_showText, false);
        int textGravity = a.getInt(R.styleable.AdsBanner_setTextGravity, -1);
        int textColor = a.getColor(R.styleable.AdsBanner_setTextColor, getResources().getColor(android.R.color.white));
        int textBackgroundColor = a.getColor(R.styleable.AdsBanner_setTextBackgroundColor, getResources().getColor(android.R.color.transparent));
        float textSize = a.getInteger(R.styleable.AdsBanner_setTextSize, 14);
        mAutoPager = a.getBoolean(R.styleable.AdsBanner_autoPager, false);
        mPageTimeSet = a.getInteger(R.styleable.AdsBanner_autoPagerMillis, 3600);
        mPageTimeUse = mPageTimeSet;
        a.recycle();

        mViewPager.getLayoutParams().height = matchParentHeight ? -1 : bannerHeight;
        mRgIndicators.setVisibility(mShowIndicator ? VISIBLE : GONE);
        setGravity(mRgIndicators, indicatorGravity);

        mText.setVisibility(mShowText ? VISIBLE : GONE);
        setGravity(mText, textGravity);
        mText.setTextColor(textColor);
        mText.setBackgroundColor(textBackgroundColor);
        mText.setTextSize(textSize);
    }

    public void setbannerHeight(int height) {
        mViewPager.getLayoutParams().height = height;
    }

    private void setGravity(TextView view, int gravity) {
        switch (gravity) {
            case -1: {
                view.setGravity(Gravity.LEFT);
                break;
            }
            case 0: {
                view.setGravity(Gravity.CENTER_HORIZONTAL);
                break;
            }
            case 1: {
                view.setGravity(Gravity.RIGHT);
                break;
            }
        }
    }

    private void setGravity(LinearLayout view, int gravity) {
        switch (gravity) {
            case -1: {
                view.setGravity(Gravity.LEFT);
                break;
            }
            case 0: {
                view.setGravity(Gravity.CENTER_HORIZONTAL);
                break;
            }
            case 1: {
                view.setGravity(Gravity.RIGHT);
                break;
            }
        }
    }

    public void setViewPagerAdapter(BannerAdapter adapter) {
        mAdapter = adapter;
        mViewPager.setAdapter(adapter);
        mCurrentPage = mAdapter.getStartPosition();
        refreshIndicator();
        setOnPageChangedListener(null);
        mViewPager.setCurrentItem(mCurrentPage, false);
        if (mAutoPager)
            startAutoPager();
    }

    private void refreshIndicator() {
        if (!mShowIndicator)
            return;
        mRgIndicators.removeAllViews();
        for (int i = 0; i < (mAdapter.isInfiniteBanner() ? mAdapter.getCount() - 2 : mAdapter.getCount()); i++) {
            RadioButton rb = new RadioButton(getContext());
            rb.setChecked(i == 0);
            rb.setClickable(false);
            rb.setButtonDrawable(mIndicatorSelector);
            rb.setId(ID + i);
            rb.setPadding(ScreenUtil.dip2Px(getContext(), 2), 0, ScreenUtil.dip2Px(getContext(), 2), 0);
            mRgIndicators.addView(rb);
        }
    }

    public void setOnPageChangedListener(final ViewPager.OnPageChangeListener listener) {
        mViewPager.clearOnPageChangeListeners();
        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mPageTimeCountDown = mPageTimeUse;
                if (mAdapter.isInfiniteBanner())
                    if (position == mAdapter.getCount() - 1 && positionOffset <= 0.0f) {
                        mViewPager.setCurrentItem(1, false);
                    } else if (position == 0 && positionOffset <= 0.0f) {
                        mViewPager.setCurrentItem(mAdapter.getCount() - 2, false);
                    }
                if (listener != null)
                    listener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(final int position) {
                mCurrentPage = position;
                if (mShowIndicator)
                    mRgIndicators.check(ID + mAdapter.getRealPosition(position));
                if (mShowText)
                    mText.setText(mAdapter.getPageText(mAdapter.getRealPosition(position)));
                if (listener != null)
                    listener.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (listener != null)
                    listener.onPageScrollStateChanged(state);
            }
        };
        mViewPager.addOnPageChangeListener(onPageChangeListener);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
//        mPageTimeUse = mPageTimeSet;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
//        mPageTimeUse = -1;
    }

    private void startAutoPager() {
        startPager(null);
    }

    /**
     * 激活轮播
     *
     * @param millis 轮播间隔
     */
    public void startPager(@Nullable Integer millis) {
        if (millis != null && millis > 0)
            mPageTimeUse = millis;
        if (mPageThread != null) {
            mFlag++;
        }
        mPageThread = new Thread(new Runnable() {
            int flag = mFlag;

            @Override
            public void run() {
                mPageTimeCountDown = mPageTimeUse;
                while (mPageTimeUse > 0 && flag == mFlag) {
                    SystemClock.sleep(200);
                    mPageTimeCountDown -= 200;
                    if (mPageTimeCountDown <= 0) {
                        mPageTimeCountDown = mPageTimeUse;
                        if (flag == mFlag)
                            LibConfig.getApplication().getHandler().post(new Runnable() {
                                @Override
                                public void run() {
                                    if (mCurrentPage + 1 == mAdapter.getCount())
                                        mViewPager.setCurrentItem(0, true);
                                    else
                                        mViewPager.setCurrentItem(mCurrentPage + 1, true);
                                }
                            });
                    }
                }
            }
        });
        mPageThread.start();
    }

    public void setOffscreenPageLimit(int limit) {
        mViewPager.setOffscreenPageLimit(limit);
    }

    public static abstract class BannerAdapter extends PagerAdapter {

        @Override
        public abstract int getCount();

        /**
         * 是否无限轮播
         */
        public abstract boolean isInfiniteBanner();

        public abstract Object genItem(ViewGroup container, int position);

        public abstract void deleteItem(ViewGroup container, int position, Object object);

        public abstract int findItemPosition(Object object);

        public abstract String getPageText(int position);

        public int getRealPosition(int position) {
            int p = position;
            if (isInfiniteBanner()) {
                if (position == 0)
                    p = getCount() - 3;
                else if (position == getCount() - 1)
                    p = 0;
                else
                    p -= 1;
            }
            return p;
        }

        private int getStartPosition() {
            return isInfiniteBanner() ? 1 : 0;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return genItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            deleteItem(container, position, object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getItemPosition(Object object) {
            return findItemPosition(object);
        }
    }
}
