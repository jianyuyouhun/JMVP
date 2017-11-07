package com.jianyuyouhun.jmvp.ui.activitys;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jianyuyouhun.inject.annotation.FindViewById;
import com.jianyuyouhun.jmvp.R;
import com.jianyuyouhun.jmvp.view.chart.LineChartView;
import com.jianyuyouhun.jmvp.view.chart.LineInfo;
import com.jianyuyouhun.jmvp.view.chart.PointInfo;
import com.jianyuyouhun.jmvplib.app.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by wangyu on 2017/11/7.
 */

public class LineChartActivity extends BaseActivity {

    @FindViewById(R.id.line_chart)
    private LineChartView lineChart;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_line_chart;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildTestData();
        buildTestData2();
    }

    private void buildTestData() {
        List<PointInfo> pointInfos = new ArrayList<>();
        for (int i = 1; i < 180; i++) {
            PointInfo info = new PointInfo();
            info.setName("第" + i + "天");
            info.setExplain("这是" + i +"的说明");
            info.setValue((int) (Math.random() * 50));//随机0-100之间取值
            info.setEffect(true);
            pointInfos.add(info);
        }
        lineChart.addLineInfoList(new LineInfo(Color.BLACK, Color.BLACK, pointInfos));
    }

    private void buildTestData2() {
        List<PointInfo> pointInfos = new ArrayList<>();
        for (int i = 1; i < 180; i++) {
            PointInfo info = new PointInfo();
            info.setName("第" + i + "天");
            info.setExplain("这是" + i +"的说明");
            info.setEffect(i % 2 == 0);
            info.setValue((int) (Math.random() * 120));//随机0-100之间取值
            pointInfos.add(info);
        }
        lineChart.addLineInfoList(new LineInfo(Color.RED, Color.RED, pointInfos));
    }
}
