package com.jianyuyouhun.jmvplib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.jianyuyouhun.jmvplib.R;


/**
 * 任意长宽比例控件 可设置长和宽的任意比例
 * &#60;ProportionLayout
 *  android:layout_width="match_parent"
 *  android:layout_height="match_parent"
 *  android:layout_centerInParent="true"
 *  zdxing:isAdaptive="true"  //是否自适应 用于控制内容不超出屏幕
 *  zdxing:orientation="HORIZONTAL"  //以宽为基准
 *  zdxing:scaleSize="1.5" &#62;   宽高比例  1:1.5
 *    &#60;View
 *      android:layout_width="match_parent"
 *      android:layout_height="match_parent" /&#62;
 *  &#60;/ProportionLayout&#62;
 *
 * </pre>
 */
public class ProportionLayout extends ViewGroup {
    /**
     * 水平布局 以水平方向为准
     */
    public static final int HORIZONTAL = 0;

    /**
     * 垂直布局 以垂直方向为准
     */
    public static final int VERTICAL = 1;

    /**
     * 比例方向
     */
    private int orientation = HORIZONTAL;

    /**
     * 比例大小
     */
    private float scaleSize = 1f;

    /**
     * 宽高是否自适应
     */
    private boolean isAdaptive = true;

    public ProportionLayout(Context context) {
        this(context, null);
    }

    public ProportionLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProportionLayout);

        setScaleSize(typedArray.getFloat(R.styleable.ProportionLayout_scaleSize, 1f));
        setOrientation(typedArray.getInt(R.styleable.ProportionLayout_ppLayoutOrientation, HORIZONTAL));
        setAdaptive(typedArray.getBoolean(R.styleable.ProportionLayout_isAdaptive, false));

        typedArray.recycle();
    }

    @Override
    public void addView(View child) {
        if (getChildCount() > 0)
            throw new IllegalStateException("ProportionLayout can host only one direct child");
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        if (getChildCount() > 0)
            throw new IllegalStateException("ProportionLayout can host only one direct child");
        super.addView(child, index);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (getChildCount() > 0)
            throw new IllegalStateException("ProportionLayout can host only one direct child");
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() > 0)
            throw new IllegalStateException("ProportionLayout can host only one direct child");
        super.addView(child, index, params);
    }

    public void setOrientation(int orientation) {
        if (this.orientation != orientation) {
            this.orientation = orientation;
            requestLayout();
        }
    }

    /**
     * 宽高是否自适应控件 默认true
     *
     * @param isAdaptive
     */
    public void setAdaptive(boolean isAdaptive) {
        if (this.isAdaptive != isAdaptive) {
            this.isAdaptive = isAdaptive;
            requestLayout();
        }
    }

    public void setScaleSize(float scaleSize) {
        if (this.scaleSize != scaleSize) {
            this.scaleSize = scaleSize;
            requestLayout();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = r - l;
        int height = b - t;

        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            int viewWidth = view.getMeasuredWidth();
            int viewHeight = view.getMeasuredHeight();

            int left = (width - viewWidth) / 2;
            int top = (height - viewHeight) / 2;
            int right = left + viewWidth;
            int bottom = top + viewHeight;

            view.layout(left, top, right, bottom);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float width = MeasureSpec.getSize(widthMeasureSpec);
        float height = MeasureSpec.getSize(heightMeasureSpec);

        float scale = 1f;
        int newWidth;
        int newHeight;
        if (orientation == VERTICAL) {
            // 垂直方向为基准
            float tempWidth = height * scaleSize;
            if (tempWidth > width && isAdaptive)
                scale = width / tempWidth;

            newWidth = (int) (height * scaleSize * scale);
            newHeight = (int) (height * scale);
        } else {
            // 水平方向为基准
            float tempHeight = width * scaleSize;
            if (tempHeight > height && isAdaptive)
                scale = height / tempHeight;

            newHeight = (int) (width * scale * scaleSize);
            newWidth = (int) (width * scale);
        }

        if (getChildCount() > 0)
            getChildAt(0).measure(MeasureSpec.makeMeasureSpec(newWidth, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(newHeight, MeasureSpec.EXACTLY));

        setMeasuredDimension(newWidth, newHeight);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }
}