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
import com.jianyuyouhun.jmvplib.utils.Logger;

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
        test();
    }

    private void test() {
        List<People> peopleList = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            peopleList.add(new People(i));
        }
        knickOut(peopleList);
    }

    /**
     * 剔除第7个
     * @param peopleList
     */
    private void knickOut(List<People> peopleList) {
        if (peopleList.size() == 1) {
            String id = peopleList.get(peopleList.size() - 1).getId() + "";
            Logger.d("id", id);
        } else if (peopleList.size() < 7) {
            knickLastSeven(peopleList);
        } else {
            List<People> newList = new ArrayList<>();
            int offset = peopleList.size() % 7;
            for (int i = 0; i < peopleList.size(); i++) {
                if (i % 7 != 6) {
                    newList.add(peopleList.get(i));
                }
            }
            List<People> offsetList = new ArrayList<>();
            if (newList.size() < 7) {
                knickOut(newList);
            } else {
                for (int i = 0; i < offset; i++) {
                    People lastPeople = newList.remove(newList.size() - 1);
                    offsetList.add(0, lastPeople);
                }
                offsetList.addAll(newList);
                knickOut(offsetList);
            }
        }
    }

    /**
     * 小于7个的时候
     * @param peopleList
     */
    private void knickLastSeven(List<People> peopleList) {
        List<People> lessSevenList = new ArrayList<>();
        int pos = 7 % peopleList.size() - 1;
        lessSevenList.addAll(getListAfter(pos, peopleList));
        lessSevenList.addAll(getListBefore(pos, peopleList));
        knickOut(lessSevenList);
    }

    /**
     * 取目标位置后的list
     * @param pos
     * @param src
     * @return
     */
    private List<People> getListAfter(int pos, List<People> src) {
        List<People> result = new ArrayList<>();
        for (int i = pos + 1; i < src.size(); i++) {
            result.add(src.get(i));
        }
        return result;
    }

    /**
     * 取目标位置前的list
     * @param pos
     * @param src
     * @return
     */
    private List<People> getListBefore(int pos, List<People> src) {
        List<People> result = new ArrayList<>();
        for (int i = 0; i < pos; i++) {
            result.add(src.get(i));
        }
        return result;
    }

    private class People {
        int id;
        public People(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
    
    private void buildTestData() {
        List<PointInfo> pointInfos = new ArrayList<>();
        for (int i = 1; i < 30; i++) {
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
        for (int i = 1; i < 30; i++) {
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
