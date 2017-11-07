package com.jianyuyouhun.jmvp.view.chart;

import android.graphics.Point;
import android.support.annotation.ColorInt;

import java.util.ArrayList;
import java.util.List;

/**
 * 线条实体
 * Created by wangyu on 2017/11/7.
 */

public class LineInfo {

    private List<PointInfo> pointInfoList;
    @ColorInt
    private int lineColor;
    @ColorInt
    private int linePointColor;

    private int maxValue;
    private int minValue;

    public LineInfo(@ColorInt int lineColor, @ColorInt int linePointColor, List<PointInfo> pointInfos) {
        this.pointInfoList = pointInfos;
        calculateMaxAndMin();
        this.lineColor = lineColor;
        this.linePointColor = linePointColor;
    }

    private void calculateMaxAndMin() {
        maxValue = 0;
        minValue = 0;
        for (PointInfo pointInfo : pointInfoList) {
            if (pointInfo.getValue() > maxValue) {
                maxValue = pointInfo.getValue();
            }
            if (pointInfo.getValue() < minValue) {
                minValue = pointInfo.getValue();
            }
        }
    }

    /**
     * 获取最大值
     */
    public int getMaxValue() {
        return maxValue;
    }

    /**
     * 获取最小值
     */
    public int getMinValue() {
        return minValue;
    }

    public List<PointInfo> getPointInfoList() {
        return pointInfoList;
    }

    public void setPointInfoList(List<PointInfo> pointInfoList) {
        this.pointInfoList = pointInfoList;
        calculateMaxAndMin();
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public int getLinePointColor() {
        return linePointColor;
    }

    public void setLinePointColor(int linePointColor) {
        this.linePointColor = linePointColor;
    }

}
