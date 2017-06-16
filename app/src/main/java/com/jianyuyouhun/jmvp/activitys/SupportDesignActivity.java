package com.jianyuyouhun.jmvp.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jianyuyouhun.jmvp.R;
import com.jianyuyouhun.jmvp.adapter.TestRecyclerAdapter;
import com.jianyuyouhun.jmvp.view.OnLoadMoreListener;
import com.jianyuyouhun.jmvp.view.PullRefreshRecyclerView;
import com.jianyuyouhun.jmvplib.app.BaseActivity;
import com.jianyuyouhun.jmvplib.utils.injecter.FindViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * support 包测试
 * Created by wangyu on 2017/6/16.
 */

public class SupportDesignActivity extends BaseActivity {

    @FindViewById(R.id.listView)
    private PullRefreshRecyclerView listView;

    private TestRecyclerAdapter recyclerAdapter;

    private List<String> list = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initList();
        recyclerAdapter = new TestRecyclerAdapter(this);
        listView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(int currentPage) {
                listView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerAdapter.addDataList(list);
                    }
                }, 2000);
            }
        });
        recyclerAdapter.addDataList(list);
        testAdapter();
    }

    private void testAdapter() {
        listView.setAdapter(recyclerAdapter);
        recyclerAdapter.openHeader();
    }

    private void initList() {
        for (int i = 0; i < 10; i++) {
            list.add("数据" + i);
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_support_design;
    }

}
