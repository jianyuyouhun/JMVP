package com.jianyuyouhun.jmvp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.jianyuyouhun.jmvp.R;
import com.jianyuyouhun.jmvplib.utils.injecter.FindViewById;
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

    @NonNull
    @Override
    protected DemoListAdapter.MyViewHolder onNewViewHolder() {
        return new MyViewHolder();
    }

    public class MyViewHolder extends SimpleBaseAdapter.ViewHolder {
        @FindViewById(R.id.list_item_text)
        private TextView mTextView;
    }
}
