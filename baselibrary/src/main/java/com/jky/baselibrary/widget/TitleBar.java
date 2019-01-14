package com.jky.baselibrary.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jky.baselibrary.R;
import com.jky.baselibrary.util.common.Logger;
import com.jky.baselibrary.util.common.ScreenUtil;


public class TitleBar extends RelativeLayout implements View.OnClickListener {

    public static final String TAG = TitleBar.class.getSimpleName();
    public static final int FUNCTION_LEFT_ICON = 0x01;
    public static final int FUNCTION_LEFT_TEXT = 0x01 << 1;
    public static final int FUNCTION_CENTER_ICON = 0x01 << 2;
    public static final int FUNCTION_CENTER_TEXT = 0x01 << 3;
    public static final int FUNCTION_RIGHT_ICON = 0x01 << 4;
    public static final int FUNCTION_RIGHT_TEXT = 0x01 << 5;
    public static final int FUNCTION_ALL = FUNCTION_LEFT_ICON
            | FUNCTION_LEFT_TEXT
            | FUNCTION_CENTER_ICON
            | FUNCTION_CENTER_TEXT
            | FUNCTION_RIGHT_ICON
            | FUNCTION_RIGHT_TEXT;

    private RelativeLayout mLayout;
    private View mVStatusBarArea;
    private RelativeLayout mLayoutContents;
    private TextView mTvLeft;
    private TextView mTvCenter;
    private TextView mTvRight;
    private ImageView mIvLeft;
    private ImageView mIvCenter;
    private ImageView mIvRight;
    private ProgressBar mProgressBar;
    private View mVUnderline;

    private int mBarHeight;
    private int mBackGroundColor;

    private String mTextLeft;
    private int mTextSizeLeft;
    private int mTextColorLeft;
    private int mTextBackgroundLeft;
    private int mTextLeftMarginLeft;
    private int mIconLeft;
    private int mIconBackgroundLeft;
    private int mIconLeftMarginLeft;

    private String mTextCenter;
    private int mTextSizeCenter;
    private int mTextColorCenter;
    private int mIconCenter;

    private String mTextRight;
    private int mTextSizeRight;
    private int mTextColorRight;
    private int mTextBackgroundRight;
    private int mTextRightMarginRight;
    private int mIconRight;
    private int mIconBackgroundRight;
    private int mIconRightMarginRight;

    private boolean mBackFunction;
    private int mIconBack;

    private boolean mUnderline;
    private int mUnderlineHeight;
    private int mUnderlineColor;

    private int mFunction;

    private OnClickListener mListener;


    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.widget_title_bar, this);
        initView();
        initAttr(context, attrs, defStyleAttr);
        initState();
        initEvent();
        initFunction();
    }

    private void initView() {
        mLayout = (RelativeLayout) findViewById(R.id.rl_widget_TitleBar);
        mVStatusBarArea = findViewById(R.id.v_status_bar_area_widget_TitleBar);
        mLayoutContents = (RelativeLayout) findViewById(R.id.rl_contents_widget_TitleBar);

        mTvLeft = (TextView) findViewById(R.id.tv_left_widget_TitleBar);
        mTvCenter = (TextView) findViewById(R.id.tv_center_widget_TitleBar);
        mTvRight = (TextView) findViewById(R.id.tv_right_widget_TitleBar);

        mIvLeft = (ImageView) findViewById(R.id.iv_left_widget_TitleBar);
        mIvCenter = (ImageView) findViewById(R.id.iv_center_widget_TitleBar);
        mIvRight = (ImageView) findViewById(R.id.iv_right_widget_TitleBar);

        mProgressBar = (ProgressBar) findViewById(R.id.pb_widget_TitleBar);

        mVUnderline = findViewById(R.id.v_underline_widget_TitleBar);
    }

    private void initAttr(Context context, AttributeSet attrs, int defStyle) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            setLayerType(View.LAYER_TYPE_SOFTWARE, null);//关闭硬件加速
//        }

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
        mBarHeight = a.getDimensionPixelSize(R.styleable.TitleBar_height, ScreenUtil.dip2Px(context, 48));
        mBackGroundColor = a.getColor(R.styleable.TitleBar_backgroundColor, getResources().getColor(R.color.WHITE));

        mTextLeft = a.getString(R.styleable.TitleBar_textLeft);
        mTextColorLeft = a.getColor(R.styleable.TitleBar_textColorLeft, getResources().getColor(R.color.GRAY));
        mTextSizeLeft = a.getInteger(R.styleable.TitleBar_textSizeLeft, 14);
        mTextBackgroundLeft = a.getResourceId(R.styleable.TitleBar_textBackgroundLeft, -1);
        mTextLeftMarginLeft = a.getDimensionPixelSize(R.styleable.TitleBar_textLeftMarginLeft, 0);
        mIconLeft = a.getResourceId(R.styleable.TitleBar_iconLeft, -1);
        mIconBackgroundLeft = a.getResourceId(R.styleable.TitleBar_iconBackgroundLeft, -1);
        mIconLeftMarginLeft = a.getDimensionPixelSize(R.styleable.TitleBar_iconLeftMarginLeft, 0);

        mTextCenter = a.getString(R.styleable.TitleBar_textCenter);
        mTextColorCenter = a.getColor(R.styleable.TitleBar_textColorCenter, getResources().getColor(R.color.BLACK));
        mTextSizeCenter = a.getInteger(R.styleable.TitleBar_textSizeCenter, 16);
        mIconCenter = a.getResourceId(R.styleable.TitleBar_iconCenter, -1);

        mTextRight = a.getString(R.styleable.TitleBar_textRight);
        mTextColorRight = a.getColor(R.styleable.TitleBar_textColorRight, getResources().getColor(R.color.GRAY));
        mTextSizeRight = a.getInteger(R.styleable.TitleBar_textSizeRight, 14);
        mTextBackgroundRight = a.getResourceId(R.styleable.TitleBar_textBackgroundRight, -1);
        mTextRightMarginRight = a.getDimensionPixelSize(R.styleable.TitleBar_textRightMarginRight, 0);
        mIconRight = a.getResourceId(R.styleable.TitleBar_iconRight, -1);
        mIconBackgroundRight = a.getResourceId(R.styleable.TitleBar_iconBackgroundRight, -1);
        mIconRightMarginRight = a.getDimensionPixelSize(R.styleable.TitleBar_iconRightMarginRight, 0);

        mBackFunction = a.getBoolean(R.styleable.TitleBar_backFunction, false);
        mIconBack = a.getResourceId(R.styleable.TitleBar_iconBack, -1);

        mUnderline = a.getBoolean(R.styleable.TitleBar_underline, false);
        mUnderlineHeight = a.getDimensionPixelSize(R.styleable.TitleBar_underlineHeight, ScreenUtil.dip2Px(context, 0.67f));
        mUnderlineColor = a.getColor(R.styleable.TitleBar_underlineColor, getResources().getColor(R.color.BLACK));

        mFunction = a.getInt(R.styleable.TitleBar_function, 0);

        a.recycle();
    }

    private void initState() {
        mLayout.setBackgroundColor(mBackGroundColor);
        mLayoutContents.getLayoutParams().height = mBarHeight;

        if (mTextLeft != null) {
            mTvLeft.setVisibility(VISIBLE);
            mTvLeft.setText(mTextLeft);
        }
        mTvLeft.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mTextSizeLeft);
        mTvLeft.setTextColor(mTextColorLeft);
        if (mTextBackgroundLeft != -1)
            mTvLeft.setBackgroundResource(mTextBackgroundLeft);
        ((MarginLayoutParams) mTvLeft.getLayoutParams()).leftMargin = mTextLeftMarginLeft;

        if (mIconLeft != -1) {
            mTvLeft.setVisibility(GONE);
            mIvLeft.setVisibility(VISIBLE);
            mIvLeft.setImageResource(mIconLeft);
        }
        if (mIconBackgroundLeft != -1)
            mIvLeft.setBackgroundResource(mIconBackgroundLeft);
        ((MarginLayoutParams) mIvLeft.getLayoutParams()).leftMargin = mIconLeftMarginLeft;

        if (mTextCenter != null) {
            mTvCenter.setVisibility(VISIBLE);
            mTvCenter.setText(mTextCenter);
        }
        mTvCenter.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mTextSizeCenter);
        mTvCenter.setTextColor(mTextColorCenter);

        if (mIconCenter != -1) {
            mTvCenter.setVisibility(GONE);
            mIvCenter.setVisibility(VISIBLE);
            mIvCenter.setImageResource(mIconCenter);
        }

        if (mTextRight != null) {
            mTvRight.setVisibility(VISIBLE);
            mTvRight.setText(mTextRight);
        }
        mTvRight.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mTextSizeRight);
        mTvRight.setTextColor(mTextColorRight);
        if (mTextBackgroundRight != -1)
            mTvRight.setBackgroundResource(mTextBackgroundRight);
        ((MarginLayoutParams) mTvRight.getLayoutParams()).rightMargin = mTextRightMarginRight;

        if (mIconRight != -1) {
            mTvRight.setVisibility(GONE);
            mIvRight.setVisibility(VISIBLE);
            mIvRight.setImageResource(mIconRight);
        }
        if (mIconBackgroundRight != -1)
            mIvRight.setBackgroundResource(mIconBackgroundRight);
        ((MarginLayoutParams) mIvRight.getLayoutParams()).rightMargin = mIconRightMarginRight;

        if (mBackFunction) {
            mTvLeft.setVisibility(GONE);
            mIvLeft.setVisibility(VISIBLE);
            mIvLeft.setImageResource(-1 != mIconBack ? mIconBack : R.drawable.ic_menu_back);
        }

        if (mUnderline) {
            mVUnderline.setVisibility(VISIBLE);
        }
        mVUnderline.getLayoutParams().height = mUnderlineHeight;
        mVUnderline.setBackgroundColor(mUnderlineColor);
    }

    private void initEvent() {
        mTvLeft.setOnClickListener(this);
        mTvCenter.setOnClickListener(this);
        mTvRight.setOnClickListener(this);
        mIvLeft.setOnClickListener(this);
        mIvCenter.setOnClickListener(this);
        mIvRight.setOnClickListener(this);
    }

    private void initFunction() {
        mTvLeft.setVisibility(hasFunction(FUNCTION_LEFT_TEXT) ? VISIBLE : GONE);
        mTvLeft.setClickable(hasFunction(FUNCTION_LEFT_TEXT));

        mIvLeft.setVisibility(hasFunction(FUNCTION_LEFT_ICON) ? VISIBLE : GONE);
        mIvLeft.setClickable(hasFunction(FUNCTION_LEFT_ICON));

        mTvCenter.setVisibility(hasFunction(FUNCTION_CENTER_TEXT) ? VISIBLE : GONE);
        mTvCenter.setClickable(hasFunction(FUNCTION_CENTER_TEXT));

        mIvCenter.setVisibility(hasFunction(FUNCTION_CENTER_ICON) ? VISIBLE : GONE);
        mIvCenter.setClickable(hasFunction(FUNCTION_CENTER_ICON));

        mTvRight.setVisibility(hasFunction(FUNCTION_RIGHT_TEXT) ? VISIBLE : GONE);
        mTvRight.setClickable(hasFunction(FUNCTION_RIGHT_TEXT));

        mIvRight.setVisibility(hasFunction(FUNCTION_RIGHT_ICON) ? VISIBLE : GONE);
        mIvRight.setClickable(hasFunction(FUNCTION_RIGHT_ICON));
    }

    public void setFunction(int function, boolean enable) {
        if (enable) {
            mFunction = mFunction | function;
        } else {
            mFunction = mFunction & (function ^ FUNCTION_ALL);
        }
        initFunction();
    }

    public void setText(int function, String text) {
        switch (function) {
            case FUNCTION_LEFT_TEXT: {
                mTextLeft = text;
                mTvLeft.setText(mTextLeft);
                setFunction(function, true);
                break;
            }
            case FUNCTION_CENTER_TEXT: {
                mTextCenter = text;
                mTvCenter.setText(mTextCenter);
                setFunction(function, true);
                break;
            }
            case FUNCTION_RIGHT_TEXT: {
                mTextRight = text;
                mTvRight.setText(mTextRight);
                setFunction(function, true);
                break;
            }
        }
    }

    public void setIcon(int function, int resId) {
        switch (function) {
            case FUNCTION_LEFT_ICON: {
                mIconLeft = resId;
                mIvLeft.setImageResource(mIconLeft);
                setFunction(function, true);
                break;
            }
            case FUNCTION_CENTER_ICON: {
                mIconCenter = resId;
                mIvCenter.setImageResource(mIconCenter);
                setFunction(function, true);
                break;
            }
            case FUNCTION_RIGHT_ICON: {
                mIconRight = resId;
                mIvRight.setImageResource(mIconRight);
                setFunction(function, true);
                break;
            }
        }
    }

    public void setBackground(int function, int backgroundResId) {
        switch (function) {
            case FUNCTION_LEFT_TEXT: {
                mTvLeft.setBackgroundResource(backgroundResId);
                break;
            }
            case FUNCTION_CENTER_TEXT: {
                mTvCenter.setBackgroundResource(backgroundResId);
                break;
            }
            case FUNCTION_RIGHT_TEXT: {
                mTvRight.setBackgroundResource(backgroundResId);
                break;
            }
            case FUNCTION_LEFT_ICON: {
                mIvLeft.setBackgroundResource(backgroundResId);
                break;
            }
            case FUNCTION_CENTER_ICON: {
                mIvCenter.setBackgroundResource(backgroundResId);
                break;
            }
            case FUNCTION_RIGHT_ICON: {
                mIvRight.setBackgroundResource(backgroundResId);
                break;
            }
        }
    }

    public void showProgressBar(boolean show) {
        mProgressBar.setVisibility(show ? VISIBLE : GONE);
    }

    public void setTitleBarBackGroundColor(int color) {
        mLayout.setBackgroundColor(color);
    }

    public void enableStatusBarArea(boolean enable) {
        if (enable) {
            mVStatusBarArea.setVisibility(VISIBLE);
            mVStatusBarArea.getLayoutParams().height = ScreenUtil.getStatusBarHeight(getContext());
        } else {
            mVStatusBarArea.setVisibility(GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        boolean hasListener = mListener != null;
        int function = 0;

        if (i == R.id.tv_left_widget_TitleBar) {
            Logger.d(TAG, "TEXT_LEFT");
            function = FUNCTION_LEFT_TEXT;
        } else if (i == R.id.iv_left_widget_TitleBar) {
            Logger.d(TAG, "ICON_LEFT");
            function = FUNCTION_LEFT_ICON;
        } else if (i == R.id.tv_center_widget_TitleBar) {
            Logger.d(TAG, "TEXT_CENTER");
            function = FUNCTION_CENTER_TEXT;
        } else if (i == R.id.iv_center_widget_TitleBar) {
            Logger.d(TAG, "ICON_CENTER");
            function = FUNCTION_CENTER_ICON;
        } else if (i == R.id.tv_right_widget_TitleBar) {
            Logger.d(TAG, "TEXT_RIGHT");
            function = FUNCTION_RIGHT_TEXT;
        } else if (i == R.id.iv_right_widget_TitleBar) {
            Logger.d(TAG, "ICON_RIGHT");
            function = FUNCTION_RIGHT_ICON;
        }
        boolean consumed = false;
        if (hasListener) {
            consumed = mListener.onClick(function);
        }

        if (!consumed && mBackFunction && function == FUNCTION_LEFT_ICON) {
            Logger.d(TAG, "BACK_FUNCTION");
            if (getContext() instanceof Activity) {
                Logger.d(TAG, "BACK_FUNCTION_EXECUTE");
                Activity activity = (Activity) getContext();
                activity.finish();
            }
        }
    }

    private boolean hasFunction(int function) {
        return (mFunction & function) == function;
    }

    public void setOnTitleBarClickListener(OnClickListener listener) {
        mListener = listener;
    }

    public interface OnClickListener {
        boolean onClick(int function);
    }
}
