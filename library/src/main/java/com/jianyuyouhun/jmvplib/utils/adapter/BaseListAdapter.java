package com.jianyuyouhun.jmvplib.utils.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * listAdapter封装
 * Created by wangyu on 2017/4/20.
 */

public abstract class BaseListAdapter<Info> extends BaseAdapter {

    protected LayoutInflater mInflater;
    protected Context mContext;
    private List<Info> mDatas;

    public BaseListAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    public BaseListAdapter(Context context, List<Info> datas) {
        this(context);
        this.mDatas = datas;
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public Info getItem(int position) {
        if (mDatas != null && position >= 0 && position < mDatas.size()) {
            return mDatas.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 获取数据列表
     * @return mDatas
     */
    public List<Info> getDatas() {
        return mDatas;
    }

    /**
     * 获取第一个Item
     *
     * @return 获取第一个Item，如果数据集为空，则返回null
     */
    @Nullable
    public Info getFirstItem() {
        return mDatas != null && !mDatas.isEmpty() ? mDatas.get(0) : null;
    }

    /**
     * 获取最后一个Item
     *
     * @return 获取最后一个Item，如果数据集为空，则返回null
     */
    @Nullable
    public Info getLastItem() {
        return mDatas != null && !mDatas.isEmpty() ? mDatas.get(mDatas.size() - 1) : null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Info info = getItem(position);
        View view;
        if (convertView == null) {
            view = newView(mInflater, position, info, parent);
        } else {
            view = convertView;
        }
        bindView(view, position, info);
        return view;
    }

    public abstract View newView(LayoutInflater mInflater, int position, Info info, ViewGroup parent);

    public abstract void bindView(View view, int position, Info info);

    public Context getContext() {
        return mContext;
    }

    /**
     * 改变实体列表，并通知数据已改变。如果原来存在数据，将对其进行覆盖。
     *
     * @param newDatas 新的items
     * @return 原来的items或null
     */
    public List<Info> changeDatas(List<Info> newDatas) {
        return changeDatas(newDatas, false);
    }

    public List<Info> changeDatas(List<Info> newDatas, boolean invalidate) {
        if (newDatas == mDatas) {
            if (invalidate)
                notifyDataSetInvalidated();
            else
                notifyDataSetChanged();
            return null;
        }

        List<Info> oldItems = mDatas;
        mDatas = newDatas;
        if (newDatas != null && !invalidate) {
            notifyDataSetChanged();
        } else {
            notifyDataSetInvalidated();
        }

        return oldItems;
    }

    /**
     * 销毁数据
     */
    public void clearDatas() {
        mDatas = null;
    }
}
