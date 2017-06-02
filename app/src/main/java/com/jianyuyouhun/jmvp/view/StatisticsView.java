package com.jianyuyouhun.jmvp.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import com.jianyuyouhun.jmvp.R;
import com.jianyuyouhun.jmvplib.app.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyu on 2017/6/2.
 */

public class StatisticsView extends View {
    private static final float DEFAULT_START_ANGLE = 0;
    private static final int DEFAULT_INNER_RADIUS = 30;

    private int mWidth;
    private int mHeight;
    private Paint mStatisticsPaint; //统计图画笔
    private float mStartAngle; //统计图起始角度，默认为0度
    private int mInnerRadius; //内部圆半径
    private int mInnerColor; //内部圆颜色
    private RectF mRect = new RectF(); //统计图范围

    private ArrayList<ItemData> mDataList = new ArrayList<>();

    public StatisticsView(Context context) {
        this(context, null);
    }

    public StatisticsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatisticsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.StatisticsView);
        mInnerRadius = ta.getDimensionPixelSize(R.styleable.StatisticsView_innerRadius, BaseActivity.dipToPx(context, DEFAULT_INNER_RADIUS));
        mInnerColor = ta.getColor(R.styleable.StatisticsView_innerColor, Color.WHITE);
        mStartAngle = ta.getFloat(R.styleable.StatisticsView_startAngle, DEFAULT_START_ANGLE);
        ta.recycle();
        initPaint();
    }

    private void initPaint() {
        mStatisticsPaint = new Paint(Paint.DITHER_FLAG | Paint.ANTI_ALIAS_FLAG);
        mStatisticsPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        this.mWidth = w;
        this.mHeight = h;
        int mOuterRadius = Math.min(mWidth, mHeight) / 2;
        mRect = new RectF(-mOuterRadius, -mOuterRadius, mOuterRadius, mOuterRadius);
    }

    /**
     * 设置统计数据
     *
     * @param infos infos
     */
    public void setData(@NonNull List<StatisticsInfo> infos) {
        mDataList.clear();
        float totalValue = 0;
        for (StatisticsInfo info : infos) {
            totalValue += info.getValue();
        }
        for (StatisticsInfo info : infos) {
            ItemData itemData = new ItemData();
            itemData.setItemColor(info.getColor());
            itemData.setItemName(info.getName());
            float percent = info.getValue() / totalValue;
            itemData.setItemPercent(percent * 100);
            itemData.setItemAngle(360f * percent);
            mDataList.add(itemData);
        }
        setData(mDataList);
    }

    /**
     * 设置统计图数据
     *
     * @param dataList dataList
     */
    public void setData(@NonNull ArrayList<ItemData> dataList) {
        this.mDataList = dataList;
        invalidate();
    }

    /**
     * 设置起始角度
     *
     * @param mStartAngle 角度值
     */
    public void setStartAngle(float mStartAngle) {
        this.mStartAngle = mStartAngle;
        invalidate();
    }

    /**
     * 获取统计数据
     *
     * @return 数据列表
     */
    public
    @NonNull
    ArrayList<ItemData> getDataList() {
        return mDataList;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureDimension(widthMeasureSpec), measureDimension(heightMeasureSpec));
    }

    private int measureDimension(int measureSpec) {
        int resultSize = BaseActivity.dipToPx(getContext(), 250);
        int size = MeasureSpec.getSize(measureSpec);
        int mode = MeasureSpec.getMode(measureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            resultSize = size;
        }
        if (mode == MeasureSpec.AT_MOST) {
            resultSize = Math.min(size, resultSize);
        }
        return resultSize;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDataList.isEmpty()) {
            return;
        }
        canvas.translate(mWidth / 2, mHeight / 2);
        float currentAngle = mStartAngle;
        for (ItemData data : mDataList) {
            mStatisticsPaint.setColor(data.getItemColor());
            canvas.drawArc(mRect, currentAngle, data.getItemAngle(), true, mStatisticsPaint);
            currentAngle += data.getItemAngle();
        }

        mStatisticsPaint.setColor(mInnerColor);
        canvas.drawCircle(0, 0, mInnerRadius, mStatisticsPaint);
    }

    /** 统计图每项实体 */
    public static class ItemData {
        private String itemName; //对应统计项名称
        private String itemValue; //对应统计项值
        private float itemPercent; //对应统计项百分比
        private float itemAngle; //对应统计项角度
        private int itemColor; //对应统计项百分比

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getItemValue() {
            return itemValue;
        }

        public void setItemValue(String itemValue) {
            this.itemValue = itemValue;
        }

        public float getItemPercent() {
            return itemPercent;
        }

        public void setItemPercent(float itemPercent) {
            this.itemPercent = itemPercent;
        }

        public float getItemAngle() {
            return itemAngle;
        }

        public void setItemAngle(float itemAngle) {
            this.itemAngle = itemAngle;
        }

        public int getItemColor() {
            return itemColor;
        }

        public void setItemColor(@ColorInt int itemColor) {
            this.itemColor = itemColor;
        }
    }

    public class StatisticsInfo {

        private String name;

        private int value;

        @ColorInt
        private int color;

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "StatisticsInfo{" +
                    "name='" + name + '\'' +
                    ", value=" + value +
                    ", color=" + color +
                    '}';
        }
    }
}
