package com.jianyuyouhun.jmvp.view.chart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.jianyuyouhun.jmvp.R;
import com.jianyuyouhun.jmvplib.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用折线图
 * Created by wangyu on 2017/11/7.
 */

public class LineChartView extends View {

    private Paint mPaint;//画笔
    private Paint dotPaint;//原点画笔
    private int width;//宽
    private int height;//高
    private Point originPoint;//坐标原点

    private float coordinatorTextSize;//坐标文字大小
    @ColorInt
    private int coordinatorTextColor;//坐标文字颜色
    @ColorInt
    private int coordinatorXLineColor;//x坐标线条颜色
    @ColorInt
    private int coordinatorYLineColor;//x坐标线条颜色

    private int coordinatorLineWidth;//坐标线条宽度

    private int coordinatorTextHeight;
    private int coordinatorTextWidth;
    private float coordinatorHeight;
    private float coordinatorWidth;

    private float scaleX, scaleY;//比例尺，scaleX表示坐标宽度除以point数目，scaleY表示point的一个value单位代表的像素
    private int maxPointValue = 100;
    private int maxXNumber = 0;//横向坐标点最大数量
    private List<LineInfo> lineInfoList = new ArrayList<>();
    private List<PointInfo> xTextList = new ArrayList<>();

    private PointInfo selectedPoint;
    private Point touchPoint;
    private LineInfo touchLine;
    private int touchRange;

    public LineChartView(Context context) {
        this(context, null);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs, defStyleAttr);
        initPaint();
        setKeepScreenOn(true);
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.LineChartView, defStyleAttr, 0);
        coordinatorTextSize = array.getDimensionPixelSize(R.styleable.LineChartView_coordinatorTextSize, 30);
        coordinatorTextColor = array.getColor(R.styleable.LineChartView_coordinatorTextColor, Color.BLACK);
        coordinatorXLineColor = array.getColor(R.styleable.LineChartView_coordinatorXColor, Color.RED);
        coordinatorYLineColor = array.getColor(R.styleable.LineChartView_coordinatorYColor, Color.RED);
        coordinatorLineWidth = array.getDimensionPixelSize(R.styleable.LineChartView_coordinatorLineWidth, 2);
        array.recycle();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(coordinatorLineWidth);
        mPaint.setTextSize(coordinatorTextSize);
        Paint.FontMetrics metrics = mPaint.getFontMetrics();
        coordinatorTextHeight = (int) (metrics.bottom - metrics.top);
        Rect rect = new Rect();
        String textString = "2017-11-7";
        mPaint.getTextBounds(textString, 0, textString.length(), rect);
        coordinatorTextWidth = rect.width();
        dotPaint = new Paint();
        dotPaint.setAntiAlias(true);
        dotPaint.setStyle(Paint.Style.STROKE);
        dotPaint.setStrokeWidth(2);
        touchRange = CommonUtils.dipToPx(getContext(), 20);
    }

    public void addLineInfoList(LineInfo lineInfo) {
        lineInfoList.add(lineInfo);
        maxPointValue = 100;
        maxXNumber = 7;
        LineInfo baseLine = null;
        for (LineInfo info : lineInfoList) {
            if (maxPointValue < info.getMaxValue()) {
                maxPointValue = info.getMaxValue();
            }
            if (maxXNumber < info.getPointInfoList().size()) {
                maxXNumber = info.getPointInfoList().size();
                baseLine = info;
            }
        }
        getCoordinatorXText(baseLine);
        invalidate();
    }

    public void removeLineInfoList(LineInfo lineInfo) {
        lineInfoList.remove(lineInfo);
        maxPointValue = 100;
        maxXNumber = 7;
        LineInfo baseLine = null;
        for (LineInfo info : lineInfoList) {
            if (maxPointValue < info.getMaxValue()) {
                maxPointValue = info.getMaxValue();
            }
            if (maxXNumber < info.getPointInfoList().size()) {
                maxXNumber = info.getPointInfoList().size();
                baseLine = info;
            }
        }
        getCoordinatorXText(baseLine);
        invalidate();
    }

    private void getCoordinatorXText(LineInfo baseLine) {
        xTextList.clear();
        if (baseLine == null || baseLine.getPointInfoList().size() < 7) {
            xTextList.add(new PointInfo("1", 0, true));
            xTextList.add(new PointInfo("2", 0, true));
            xTextList.add(new PointInfo("3", 0, true));
            xTextList.add(new PointInfo("4", 0, true));
        } else {
            List<PointInfo> pointInfoList = baseLine.getPointInfoList();
            int offset = pointInfoList.size() / 3;
            xTextList.add(pointInfoList.get(0));
            xTextList.add(pointInfoList.get(offset));
            xTextList.add(pointInfoList.get(pointInfoList.size() - offset - 1));
            xTextList.add(pointInfoList.get(pointInfoList.size() - 1));
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCoordinator(canvas);
        drawLineList(canvas);
        drawSelectTips(canvas);
    }

    /**
     * 绘制坐标
     *
     * @param canvas
     */
    private void drawCoordinator(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        coordinatorHeight = height - coordinatorTextHeight - 20;
        coordinatorWidth = width - coordinatorTextWidth;
        int x = coordinatorTextWidth / 2;//起点左侧留出文字位置
        originPoint = new Point(x, (int) coordinatorHeight);
        mPaint.setColor(coordinatorXLineColor);
        canvas.drawLine(originPoint.x, coordinatorHeight, originPoint.x + coordinatorWidth, coordinatorHeight, mPaint);
        mPaint.setColor(coordinatorYLineColor);
        canvas.drawLine(originPoint.x, originPoint.y, originPoint.x, 0, mPaint);
        scaleX = coordinatorWidth / maxXNumber;
        scaleY = coordinatorHeight / maxPointValue;
        float xRange = coordinatorWidth / 3;//标识四个点，实际需要除以三
        mPaint.setColor(coordinatorTextColor);

        int startY = (int) (coordinatorHeight + coordinatorTextHeight);
        for (int i = 0; i < xTextList.size(); i++) {
            String text = xTextList.get(i).getName();
            int textWidth = getTextWidth(text);
            int startX = (int) (originPoint.x + xRange * i - textWidth / 2);
            canvas.drawText(xTextList.get(i).getName(), startX, startY, mPaint);
        }
    }

    /**
     * 绘制折线
     *
     * @param canvas
     */
    private void drawLineList(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        for (int i = 0; i < lineInfoList.size(); i++) {
            LineInfo lineInfo = lineInfoList.get(i);
            drawLine(canvas, lineInfo);
        }
    }

    private void drawLine(Canvas canvas, LineInfo lineInfo) {
        mPaint.setColor(lineInfo.getLineColor());
        Path path = new Path();
        boolean hasMove = false;
        for (int i = 0; i < lineInfo.getPointInfoList().size(); i++) {
            PointInfo pointInfo = lineInfo.getPointInfoList().get(i);
            Point point = new Point();
            point.x = (int) (originPoint.x + i * scaleX);
            point.y = (int) (coordinatorHeight - pointInfo.getValue() * scaleY);
            if (pointInfo.isEffect()) {
                if (!hasMove) {
                    path.moveTo(point.x, point.y);
                    hasMove = true;
                }
                pointInfo.setPoint(point);
                path.lineTo(point.x, point.y);
            } else {
                pointInfo.setPoint(null);
            }
        }
        canvas.drawPath(path, mPaint);
    }

    /**
     * 绘制当前选择的点
     *
     * @param canvas
     */
    private void drawSelectTips(Canvas canvas) {
        if (selectedPoint == null) {
            return;
        }
        Point point = selectedPoint.getPoint();
        canvas.drawLine(point.x, originPoint.y, point.x, 0, dotPaint);
        canvas.drawCircle(point.x, point.y, 4, dotPaint);
        TextView textView = new TextView(getContext());
        textView.setBackgroundColor(Color.parseColor("#33000000"));
        textView.setTextColor(Color.BLACK);
        textView.setText(selectedPoint.getExplain());
        int padding = CommonUtils.dipToPx(getContext(), 12);
        textView.setPadding(padding, padding / 2, padding, padding / 2);
        Bitmap selectedBitmap = convertViewToBitmap(textView);
        int selectBitmapWidth = selectedBitmap.getWidth();
        int selectBitmapHeight = selectedBitmap.getHeight();
        float tipsX;
        if (point.x + selectBitmapWidth + 16 > coordinatorWidth) {
            tipsX = point.x - selectBitmapWidth - 16;
        } else {
            tipsX = point.x + 16;
        }
        canvas.drawBitmap(selectedBitmap, tipsX, 16, null);
    }

    private int getTextWidth(String text) {
        Rect rect = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), rect);
        return rect.width();
    }

    private Bitmap convertViewToBitmap(View view) {
        view.setDrawingCacheEnabled(true);
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        Matrix matrix = new Matrix();
        matrix.postScale(1, 1);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth() - 1, bitmap.getHeight(), matrix, true);
        view.setDrawingCacheEnabled(false);
        return newBitmap;
    }

    /**
     * 触摸时判断当前选中哪个点
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchPoint = new Point();
                touchPoint.x = (int) event.getX();
                touchPoint.y = (int) event.getY();
                findLine(touchPoint);
                calculateSelectedPoint(touchPoint);
                return true;
            case MotionEvent.ACTION_UP:// 手指离开时可以选择隐藏tipsView(shouldShownTips = false; invalidate();)
                selectedPoint = null;
                touchPoint = null;
                touchLine = null;
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE://移动时不更新Y,y只用于按下时寻找目标线
                touchPoint.x = (int) event.getX();
                calculateSelectedPoint(touchPoint);
                return true;
            case MotionEvent.ACTION_CANCEL:// 手指离开时可以选择隐藏tipsView(shouldShownTips = false; invalidate();)
                selectedPoint = null;
                touchPoint = null;
                touchLine = null;
                invalidate();
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    private void findLine(Point touchPoint) {
        for (LineInfo lineInfo : lineInfoList) {
            for (PointInfo pointInfo : lineInfo.getPointInfoList()) {
                if (pointInfo.isEffect() && pointInfo.getPoint() != null) {
                    Point point = pointInfo.getPoint();
                    int x = touchPoint.x - point.x;
                    int y = touchPoint.y - point.y;
                    if (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) < touchRange) {
                        //如果两点间距在40dp以内，就作为当前选中线
                        touchLine = lineInfo;
                        dotPaint.setColor(lineInfo.getLinePointColor());
                        break;
                    }
                }
            }
        }
    }

    private void calculateSelectedPoint(Point touchPoint) {
        if (touchLine == null) {
            return;
        }
        float range = touchRange > scaleX ? touchRange : scaleX;

        for (PointInfo pointInfo : touchLine.getPointInfoList()) {
            if (pointInfo.isEffect() && pointInfo.getPoint() != null) {
                Point point = pointInfo.getPoint();
                if (Math.abs(point.x - touchPoint.x) < range) {//滑动时y不管，只看x相近
                    selectedPoint = pointInfo;
                    break;
                }
            }
        }
        invalidate();
    }
}
