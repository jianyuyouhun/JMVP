package com.jianyuyouhun.jmvp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyu on 2017/6/16.
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter {
    public static final int TYPE_HEADER = 0;  //说明是带有Header的
    public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
    public static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的

    protected List<T> dataList;

    //构造函数
    public BaseRecyclerAdapter(){
        this.dataList = new ArrayList<>();
    }

    public void addDataList(List<T> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void setDataList(List<T> dataList) {
        this.dataList.clear();
        addDataList(dataList);
    }

    private boolean hasHeader = false;
    private boolean hasFooter = false;

    public void openHeader() {
        hasHeader = true;
        notifyItemInserted(0);
    }

    public void closeHeader() {
        if (hasHeader) {
            hasHeader = false;
            notifyItemRemoved(0);
        }
    }

    public void openFooter() {
        hasFooter = true;
        notifyItemInserted(getItemCount() - 1);
    }

    public void closeFooter() {
        if (hasFooter) {
            hasFooter = false;
            notifyItemRemoved(getItemCount() - 1);
        }
    }

    /** 重写这个方法，很重要，是加入Header和Footer的关键，我们通过判断item的类型，从而绑定不同的view    * */
    @Override
    public int getItemViewType(int position) {
        if (!hasHeader && !hasFooter){
            return TYPE_NORMAL;
        }
        if (position == 0){
            //第一个item应该加载Header
            return TYPE_HEADER;
        }
        if (position == getItemCount()-1){
            //最后一个,应该加载Footer
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    //创建View，如果是HeaderView或者是FooterView，直接在Holder中返回
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(hasHeader && viewType == TYPE_HEADER) {
            return onCreateHeaderViewHolder(parent);
        }
        if(hasFooter && viewType == TYPE_FOOTER){
            return onCreateFooterViewHolder(parent);
        }
        return onCreateChildViewHolder(parent, viewType);
    }

    public abstract RecyclerView.ViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType);

    public abstract RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent);

    public abstract RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (hasHeader && hasFooter) {
            if (position == 0) {
                onBindHeader(holder);
            } else if (position == getItemCount() - 1){
                onBindFooter(holder);
            } else {
                onBindView(holder, position - 2, dataList.get(position - 2));
            }
        } else if (!hasHeader && hasFooter) {
            if (position == getItemCount() - 1) {
                onBindFooter(holder);
            } else {
                onBindView(holder, position - 1, dataList.get(position - 1));
            }
        } else if (hasHeader) {
            if (position == 0) {
                onBindHeader(holder);
            } else {
                onBindView(holder, position - 1, dataList.get(position - 1));
            }
        } else {
            onBindView(holder, position, dataList.get(position));
        }
    }

    public void onBindView(RecyclerView.ViewHolder holder, int position, T t) {

    }

    public void onBindFooter(RecyclerView.ViewHolder holder) {

    }

    public void onBindHeader(RecyclerView.ViewHolder holder) {

    }


    //返回View中Item的个数，这个时候，总的个数应该是ListView中Item的个数加上HeaderView和FooterView
    @Override
    public int getItemCount() {
        if(!hasHeader && !hasFooter){
            return dataList.size();
        } else if (!hasHeader){
            return dataList.size() + 1;
        } else if (!hasFooter){
            return dataList.size() + 1;
        } else {
            return dataList.size() + 2;
        }
    }
}
