package com.jianyuyouhun.jmvp.ui.adapter;

import android.content.Context;
import android.widget.TextView;

import com.jianyuyouhun.inject.annotation.FindViewById;
import com.jianyuyouhun.jmvp.R;
import com.jianyuyouhun.jmvplib.view.adapter.SimpleBaseAdapter;


/**
 * 测试adapter
 * Created by wangyu on 2017/4/24.
 */

public class DemoListAdapter extends SimpleBaseAdapter<String, DemoListAdapter.MyViewHolder> {

    public DemoListAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.list_item;
    }

    @Override
    protected void bindView(DemoListAdapter.MyViewHolder viewHolder, String s, int position) {
        viewHolder.mTextView.setText(s);
    }

    public static class MyViewHolder extends SimpleBaseAdapter.ViewHolder {
        @FindViewById(R.id.list_item_text)
        private TextView mTextView;
    }
}
