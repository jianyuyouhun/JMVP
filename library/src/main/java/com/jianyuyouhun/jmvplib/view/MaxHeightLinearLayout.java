package com.jianyuyouhun.jmvplib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.jianyuyouhun.jmvplib.R;


/**
 * 最大高度限制线性布局
 * Created by wangyu on 2017/5/25.
 */

public class MaxHeightLinearLayout extends LinearLayout {
    private float maxHeight = -1;
    private static final int MODE_SHIFT = 30;
    private static final int MODE_MASK = 0x3 << MODE_SHIFT;

    public MaxHeightLinearLayout(Context context) {
        this(context, null);
    }

    public MaxHeightLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaxHeightLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs, defStyleAttr);
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.MaxHeightLinearLayout, defStyleAttr, 0);
        maxHeight = typedArray.getDimension(R.styleable.MaxHeightLinearLayout_maxHeight, -1);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (maxHeight == -1) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (heightMode == MeasureSpec.EXACTLY) {
            heightSize = heightSize <= maxHeight ? heightSize
                    : (int) maxHeight;
        }

        if (heightMode == MeasureSpec.UNSPECIFIED) {
            heightSize = heightSize <= maxHeight ? heightSize
                    : (int) maxHeight;
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = heightSize <= maxHeight ? heightSize
                    : (int) maxHeight;
        }
        @IntRange(from = 0, to = (1 << MODE_SHIFT) - 1)
        int heightSizeFormat = heightSize;
        int maxHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSizeFormat,
                heightMode);
        super.onMeasure(widthMeasureSpec, maxHeightMeasureSpec);
    }

}
