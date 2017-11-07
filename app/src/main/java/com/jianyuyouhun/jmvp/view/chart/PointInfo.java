package com.jianyuyouhun.jmvp.view.chart;

import android.graphics.Point;

/**
 * 点数据
 * Created by wangyu on 2017/11/7.
 */

public class PointInfo {

    private String name;
    private int value;
    private boolean isEffect;
    private Point point;

    private String explain;//说明字段

    public PointInfo() {
    }

    public PointInfo(String name, int value, boolean isEffect) {
        this.name = name;
        this.value = value;
        this.isEffect = isEffect;
        this.explain = "这是说明";
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public boolean isEffect() {
        return isEffect;
    }

    public void setEffect(boolean effect) {
        isEffect = effect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}