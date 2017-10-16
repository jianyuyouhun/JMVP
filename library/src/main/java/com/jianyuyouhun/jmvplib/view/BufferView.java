package com.jianyuyouhun.jmvplib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.jianyuyouhun.jmvplib.R;

/**
 * 自定义视图
 * 可实现双缓冲绘制
 * 备注：Android设备一般屏幕刷新率固定在60fps，这是由硬件决定的。所以每次onDraw绘制间隔时间最小值为16.66ms。
 * 如果你的每次刷新耗时已经超过了这个时间，那么在尽量优化所有绘制过程之后依然超过16.66ms/每次的情况下，可以开启双缓冲。
 * 双缓冲的原理是将所有绘制操作转为内存cpu处理然后生成一个bitmap，最后由gpu绘制这一个bitmap，
 * 而绘制bitmap本身是较为耗时的（相比于绘制文本、线条等），所以这点也是需要考虑的。
 * Created by wangyu on 2017/6/8.
 */

public abstract class BufferView extends View {

    protected Context mContext;
    private Paint clearCanvasPaint;//清屏画笔
    private Canvas mBufferCanvas;//自定义缓冲区画布
    private Bitmap mBufferBitmap;//自定义缓冲位图
    private int bgColor;//背景颜色，同时也是清屏画笔的颜色

    protected int mViewWidth;//视图宽度
    protected int mViewHeight;//视图高度

    private boolean isBufferDrawMode = false;

    public BufferView(Context context) {
        this(context, null);
    }

    public BufferView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BufferView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BufferView, defStyleAttr, 0);
        bgColor = array.getColor(R.styleable.BufferView_mBackgroundColor, Color.WHITE);
        array.recycle();
        mContext = context;
        isBufferDrawMode = initBufferMode();
        setBackgroundColor(bgColor);
        initClearPaint();
    }

    /**
     * 初始化清屏画笔
     */
    private void initClearPaint() {
        clearCanvasPaint = new Paint();
        clearCanvasPaint.setAntiAlias(true);
        clearCanvasPaint.setColor(bgColor);
        clearCanvasPaint.setStyle(Paint.Style.FILL);
    }

    protected abstract boolean initBufferMode();

    /**
     * 修改缓冲模式
     * @param isBufferDrawMode  是否是双缓冲
     */
    protected void setBufferMode(boolean isBufferDrawMode) {
        this.isBufferDrawMode = isBufferDrawMode;
        invalidate();
    }

    /**
     * 请使用{@link #onCustomDraw(Canvas)}
     */
    @Deprecated
    @Override
    protected void onDraw(Canvas canvas) {
        if (isBufferDrawMode) {
            if (mBufferBitmap == null) {
                mBufferBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
                mBufferCanvas = new Canvas(mBufferBitmap);
            }
            mBufferCanvas.drawPaint(clearCanvasPaint);//清屏
            onCustomDraw(mBufferCanvas);
            canvas.drawBitmap(mBufferBitmap, 0, 0, null);
        } else {
            onCustomDraw(canvas);
        }
    }

    /**
     * 绘制视图,大量绘制需求的情况下可以开启双缓冲
     * @param canvas  画布，开启双缓冲模式下是bufferCanvas, 否则是canvas
     */
    protected abstract void onCustomDraw(Canvas canvas);

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
    }
}
