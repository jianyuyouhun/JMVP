package com.jianyuyouhun.jmvp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jianyuyouhun.jmvp.R;
import com.jianyuyouhun.jmvplib.utils.injecter.FindViewById;
import com.jianyuyouhun.jmvplib.utils.injecter.ViewInjectUtil;

import java.util.List;

/**
 * demoRecyclerAdapter
 * Created by wangyu on 2017/5/2.
 */

public class DemoRecyclerAdapter extends RecyclerView.Adapter<DemoRecyclerAdapter.MyViewHolder> {

    private List<String> list;
    private Context context;
    private LayoutInflater inflater;

    public DemoRecyclerAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public DemoRecyclerAdapter(Context context, List<String> stringList) {
        this(context);
        this.list = stringList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(inflater.inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mTextView.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setDatas(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @FindViewById(R.id.list_item_text)
        private TextView mTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            ViewInjectUtil.inject(this, itemView);
        }
    }
}
