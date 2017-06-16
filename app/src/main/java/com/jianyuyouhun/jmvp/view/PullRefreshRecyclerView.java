package com.jianyuyouhun.jmvp.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 *
 * Created by wangyu on 2017/6/16.
 */

public class PullRefreshRecyclerView extends RecyclerView {

    private LinearLayoutManager linearLayoutManager;

    public boolean isOpenLoadmore() {
        return isOpenLoadmore;
    }

    public void setOpenLoadmore(boolean openLoadmore) {
        isOpenLoadmore = openLoadmore;
    }

    private boolean isOpenLoadmore = false;

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        isOpenLoadmore = true;
        this.onLoadMoreListener = onLoadMoreListener;
    }

    private OnLoadMoreListener onLoadMoreListener;

    public PullRefreshRecyclerView(Context context) {
        this(context,null);
    }

    public PullRefreshRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullRefreshRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        linearLayoutManager = new LinearLayoutManager(context);
        setLayoutManager(linearLayoutManager);
        initListener();
    }

    private void initListener() {
        addOnScrollListener(new EndlessOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                if (isOpenLoadmore) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore(currentPage);
                    }
                }
            }
        });
    }
}
