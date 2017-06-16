package com.jianyuyouhun.jmvp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jianyuyouhun.jmvp.R;
import com.jianyuyouhun.jmvplib.utils.injecter.FindViewById;
import com.jianyuyouhun.jmvplib.utils.injecter.ViewInjectUtil;

/**
 * Created by wangyu on 2017/6/16.
 */

public class TestRecyclerAdapter extends BaseRecyclerAdapter<String> {
    private Context context;
    private LayoutInflater inflater;

    public TestRecyclerAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        TextView view = new TextView(context);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.setText("我是头");
        view.setTextColor(Color.BLACK);
        return new HeaderVH(view);
    }

    @Override
    public RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent) {
        TextView view = new TextView(context);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.setText("我是脚");
        view.setTextColor(Color.BLACK);
        return new FooterVH(view);
    }

    @Override
    public void onBindView(RecyclerView.ViewHolder holder, int position, String s) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.mItem.setText(s);
    }

    private class HeaderVH extends RecyclerView.ViewHolder {

        public HeaderVH(View itemView) {
            super(itemView);
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        @FindViewById(R.id.list_item_text)
        private TextView mItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ViewInjectUtil.inject(this, itemView);
        }
    }

    private class FooterVH extends RecyclerView.ViewHolder {

        public FooterVH(View itemView) {
            super(itemView);
        }
    }
}
