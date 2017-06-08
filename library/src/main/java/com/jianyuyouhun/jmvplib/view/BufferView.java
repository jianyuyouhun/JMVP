package com.jianyuyouhun.jmvplib.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自定义视图
 * 可实现双缓冲绘制
 * Created by wangyu on 2017/6/8.
 */

public abstract class BufferView extends View {

    protected Context mContext;
    private Paint clearCanvasPaint;//清屏画笔
    private Canvas mBufferCanvas;//自定义缓冲区画布
    private Bitmap mBufferBitmap;//自定义缓冲位图

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
        mContext = context;
        isBufferDrawMode = initBufferMode();
        initClearPaint();
    }

    /**
     * 初始化清屏画笔
     */
    private void initClearPaint() {
        clearCanvasPaint = new Paint();
        clearCanvasPaint.setAntiAlias(true);
        clearCanvasPaint.setColor(Color.WHITE);
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
     * @param canvas
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
     * 绘制视图
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
