package com.jianyuyouhun.jmvplib.view.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jianyuyouhun.inject.ViewInjector;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by wangyu on 2017/12/8.
 */

public abstract class MultiItemAdapter extends BaseAdapter {

    private List<MultiItem> dataList = new ArrayList<>();

    private List<Integer> typeList = new ArrayList<>();
    /**
     * 以itemType为key的viewHolderClass列表
     */
    private SparseArray<Class<? extends MultiItemAdapter.ViewHolder>> viewHolderClasses = new SparseArray<>();

    private Context context;

    public MultiItemAdapter(Context context) {
        this.context = context;
        initItemType(typeList);
        for (Integer integer : typeList) {
            viewHolderClasses.put(integer, getViewHolderByItemType(integer));
        }
    }
    /**
     * 添加要显示的数据到末尾 注意：调用本方法设置数据，listView不需要再调用：notifyDataSetChanged
     *
     * @param datas 数据列表
     */
    public void addData(List<MultiItem> datas) {
        this.dataList.addAll(datas);
        notifyDataSetChanged();
    }

    /**
     * 删除某条数据
     *
     * @param position
     */
    public void deleteData(int position) {
        this.dataList.remove(position);
        notifyDataSetChanged();
    }

    /**
     * 获取所有的数据
     *
     * @return 适配器数据源
     */
    public List<MultiItem> getData() {
        return new ArrayList<>(dataList);
    }

    /**
     * 设置要显示的数据，注意：调用本方法设置数据，listView不需要再调用：notifyDataSetChanged
     *
     * @param datas 数据
     */
    public void setData(List<MultiItem> datas) {
        this.dataList.clear();
        addData(datas);
    }

    public void addToLast(MultiItem data) {
        dataList.add(data);
        notifyDataSetChanged();
    }

    public void addToPosition(int position, MultiItem data) {
        dataList.add(position, data);
        notifyDataSetChanged();
    }

    public void addToFirst(MultiItem data) {
        dataList.add(0, data);
        notifyDataSetChanged();
    }

    /**
     * 获取适配器的最后一项，如果适配器大小等于0，将返回null
     */
    public MultiItem getLastItem() {
        int count = getCount();
        if (count == 0) {
            return null;
        } else {
            return getItem(count - 1);
        }
    }

    /**
     * 获取适配器的第一项，如果适配器大小等于0，将返回null
     */
    public MultiItem getFirstItem() {
        int count = getCount();
        if (count == 0) {
            return null;
        } else {
            return getItem(0);
        }
    }

    /**
     * 注册item类型
     * @param typeList
     */
    protected abstract void initItemType(List<Integer> typeList);

    protected abstract Class<? extends MultiItemAdapter.ViewHolder> getViewHolderByItemType(Integer itemType);

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public Context getContext() {
        return context;
    }

    @Override
    public MultiItem getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).getItemType();
    }

    @Override
    public int getViewTypeCount() {
        return typeList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int itemType = getItemViewType(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            Class<? extends ViewHolder> viewHolderCls = viewHolderClasses.get(itemType);
            try {
                viewHolder = viewHolderCls.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
                throw new RuntimeException("初始化ViewHolder失败： 请确保" + viewHolderCls.getSimpleName() + "为static class 并拥有无参构造函数");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException("请确保" + viewHolderCls.getSimpleName() + "拥有public的无参构造函数");
            }
            convertView = LayoutInflater.from(context).inflate(viewHolder.getLayoutId(), parent, false);
            viewHolder.setItemView(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MultiItem multiItem = getItem(position);
        bindView(itemType, viewHolder, multiItem, position);
        return convertView;
    }

    protected abstract void bindView(int itemType, ViewHolder viewHolder, MultiItem multiItem, int position);

    public static abstract class ViewHolder {
        private View itemView;

        public final View getItemView() {
            return itemView;
        }

        final void setItemView(View itemView) {
            this.itemView = itemView;
            ViewInjector.inject(this, itemView);
        }

        @LayoutRes
        protected abstract int getLayoutId();
    }
}
