package com.maoyongxin.myapplication.server.widget.wheelSelectDialog;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.jky.baselibrary.util.common.ScreenUtil;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.myapp.AppApplication;

import java.util.List;


@SuppressWarnings("FieldCanBeLocal")
public class Picker extends View {

    private float mTextSize;
    private int mTextColor;
    private float mStrokeWidth;
    private int mStrokeColor;
    private float mAlphaPicked;
    private float mAlphaNormal;
    private int mShowItemNum;
    private float mHeightTimes;

    private float mUnitHeight;
    private TextPaint mPaintText;
    private Paint mPaintLine;

    private boolean mFlagReadyInit;
    private boolean mFlagAfterInit;

    private float mWidth;
    private float mHeight;

    private float mXCenter;
    private float mYCenter;

    private List<PickerItem> mItems;

    private float mOffset;
    private float mOffsetMax;
    private int mIndexPicked;

    private float mLastY;

    private OnItemPickedListener mOnItemPickedListener;

    public Picker(Context context) {
        this(context, null);
    }

    public Picker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Picker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Picker);
        mTextSize = a.getDimension(R.styleable.Picker_textSize, ScreenUtil.dip2Px(context, 14));
        mTextColor = a.getColor(R.styleable.Picker_textColor, getResources().getColor(R.color.text_heavy));
        mStrokeWidth = a.getDimension(R.styleable.Picker_lineWidth, ScreenUtil.dip2Px(context, 0.67f));
        mStrokeColor = a.getColor(R.styleable.Picker_lineColor, getResources().getColor(R.color.stroke_heavy));
        mAlphaPicked = a.getFloat(R.styleable.Picker_alphaPicked, 1);
        mAlphaNormal = a.getFloat(R.styleable.Picker_alphaNormal, 0.3f);
        mShowItemNum = a.getInteger(R.styleable.Picker_showItemNumber, 3);
        mHeightTimes = a.getFloat(R.styleable.Picker_heightTimes, 3.6f);

        mUnitHeight = mTextSize * 3.2f;
        mShowItemNum = mShowItemNum < 1 ? 1 : mShowItemNum;
        a.recycle();
    }

    private void initPaint() {
        mPaintText = new TextPaint();
        mPaintText.setAntiAlias(true);
        mPaintText.setColor(mTextColor);
        mPaintText.setTextSize(mTextSize);
        mPaintText.setTextAlign(Paint.Align.CENTER);
        mPaintLine = new Paint();
        mPaintLine.setAntiAlias(true);
        mPaintLine.setColor(mStrokeColor);
        mPaintLine.setStrokeWidth(mStrokeWidth);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getWidth();
        mHeight = (int) (mUnitHeight * mHeightTimes);
        getLayoutParams().height = (int) mHeight;
        mXCenter = mWidth / 2;
        mYCenter = mHeight / 2;
        mFlagReadyInit = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mItems != null) {
            for (int i = 0; i < mItems.size(); i++) {
                if (Math.abs(mIndexPicked - i) > (mShowItemNum - 1) / 2)
                    continue;

                float alpha = i == mIndexPicked ? mAlphaPicked : mAlphaNormal;

                mPaintText.setAlpha((int) (alpha * 255));
                String text = mItems.get(i).getText();
                if (mItems.get(i).mHasUnit)
                    text += "  " + mItems.get(i).getUnit();

                float yOri = mItems.get(i).getY();
                float yNow = yOri - mOffset;

                if (yNow < -mUnitHeight)
                    continue;
                if (yNow > mHeight + mUnitHeight)
                    break;
                if (mItems.get(i).isYInited()) {
                    //canvas.drawText(text, mXCenter, getItemY(i), mPaintText);

                    TextPaint tp = new TextPaint();
                    tp.setColor(Color.BLUE);
                    tp.setStyle(Paint.Style.FILL);
                    tp.setTextSize(50);
                    canvas.translate(mXCenter, getItemY(i));
                    String message = "paint,draw paint指用颜色画,如油画颜料、水彩或者水墨画,而draw 通常指用铅笔、钢笔或者粉笔画,后者一般并不涂上颜料。两动词的相应名词分别为p";
                    StaticLayout myStaticLayout = new StaticLayout(text, mPaintText, canvas.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    myStaticLayout.draw(canvas);
                }

            }
            canvas.drawLine(0, mYCenter - mUnitHeight / 2, mWidth, mYCenter - mUnitHeight / 2, mPaintLine);
            canvas.drawLine(0, mYCenter + mUnitHeight / 2, mWidth, mYCenter + mUnitHeight / 2, mPaintLine);
            canvas.restore();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mLastY = event.getY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                float y = event.getY();
                float offset = y - mLastY;
                boolean refresh = updateOffset(offset);
                updateIndexPicked();
                if (refresh)
                    refresh();
                mLastY = y;
                break;
            }
            case MotionEvent.ACTION_UP: {
                float y = event.getY();
                float offset = y - mLastY;
                boolean refresh = updateOffset(offset);
                updateIndexPicked();
                if (refresh)
                    refresh();
                stickyToPickedItem();
                break;
            }
        }
        return true;
    }

    /**
     * 计算滚动偏移量
     *
     * @return 是否需要重绘界面
     */
    private boolean updateOffset(float offset) {
        float offset0 = mOffset;
        mOffset -= offset;
        if (mOffset > mOffsetMax)
            mOffset = mOffsetMax;
        if (mOffset < 0)
            mOffset = 0;
        return offset0 != offset;
    }

    private boolean updateIndexPicked() {
        int index = 0;
        float nearest = Float.MAX_VALUE;
        for (int i = 0; i < mItems.size(); i++) {
            float distance = Math.abs(getItemY(i) - mYCenter);
            if (distance < nearest) {
                nearest = distance;
                index = i;
            }
        }
        boolean update = mIndexPicked != index;
        if (update) {
            mIndexPicked = index;
            invokeOnItemPickedListener();
        }
        return update;
    }

    private float getItemY(int index) {
        return mItems.get(index).getY() - mOffset + mTextSize / 5 * 2;
    }

    /**
     * 跳回选中项
     */
    private void stickyToPickedItem() {
        mOffset = mIndexPicked * mUnitHeight;
        refresh();
    }

    private void refresh() {
        postInvalidate();
    }

    public void setItems(final List<PickerItem> items) {
        if (items == null)
            return;
        new Thread() {
            @Override
            public void run() {
                super.run();
                while (!mFlagReadyInit) {
                    SystemClock.sleep(20);
                }
                AppApplication.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        mItems = items;
                        for (int i = 0; i < mItems.size(); i++) {
                            mItems.get(i).setY(mYCenter + mUnitHeight * i);
                        }
                        mOffsetMax = mUnitHeight * (mItems.size() - 1);
                        mOffsetMax = mOffsetMax > 0 ? mOffsetMax : 0;
                        if (mOffset >= mOffsetMax) {
                            mOffset = mOffsetMax;
                            mIndexPicked = (int) (mOffset / mUnitHeight + 0.1);
                        }
                        refresh();
                        invokeOnItemPickedListener();
                        mFlagAfterInit = true;
                    }
                });
            }
        }.start();
    }

    public void setPickedIndex(final int index) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                while (!mFlagAfterInit) {
                    SystemClock.sleep(20);
                }
                AppApplication.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        int finalIndex = index;
                        if (finalIndex < 0)
                            finalIndex = 0;
                        else if (finalIndex >= mItems.size())
                            finalIndex = mItems.size() - 1;
                        mIndexPicked = finalIndex;
                        mOffset = finalIndex * mUnitHeight;
                        refresh();
                        invokeOnItemPickedListener();
                    }
                });
            }
        }.start();
    }

    public int getPickedIndex() {
        return mIndexPicked;
    }

    private void invokeOnItemPickedListener() {
        if (mOnItemPickedListener != null && mItems != null)
            mOnItemPickedListener.onPick(this, mIndexPicked);
    }

    public void setOnItemPickedListener(OnItemPickedListener listener) {
        mOnItemPickedListener = listener;
        invokeOnItemPickedListener();
    }


    public interface OnItemPickedListener {
        void onPick(Picker picker, int index);
    }

    public static class PickerItem {

        private String mText;
        private Bitmap mIcon;
        private String mUnit;
        private Object mValue;
        private boolean mHasIcon;
        private boolean mHasUnit;
        private float mY;
        private boolean mYInited;

        public PickerItem(String text) {
            mText = text;
            mHasIcon = false;
        }

        public PickerItem(String text, Bitmap icon) {
            mText = text;
            if (mIcon == null)
                return;
            mIcon = icon;
            mHasIcon = true;
        }

        public String getText() {
            return mText;
        }

        public void setText(String text) {
            mText = text;
        }

        public Bitmap getIcon() {
            return mIcon;
        }

        public void setIcon(Bitmap icon) {
            if (icon == null)
                return;
            mIcon = icon;
            mHasIcon = true;
        }

        public String getUnit() {
            return mUnit;
        }

        public void setUnit(String unit) {
            if (!TextUtils.isEmpty(unit)) {
                mUnit = unit;
                mHasUnit = true;
            }
        }

        float getY() {
            return mY;
        }

        void setY(float y) {
            mY = y;
            mYInited = true;
        }

        public boolean isYInited() {
            return mYInited;
        }

        public boolean hasIcon() {
            return mHasIcon;
        }

        public boolean hasUnit() {
            return mHasUnit;
        }

        public Object getValue() {
            return mValue;
        }

        public void setValue(Object value) {
            mValue = value;
        }
    }
}
