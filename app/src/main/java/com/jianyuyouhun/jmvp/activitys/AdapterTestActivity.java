package com.jianyuyouhun.jmvp.activitys;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ListView;
import android.widget.TextView;

import com.jianyuyouhun.jmvp.R;
import com.jianyuyouhun.jmvp.adapter.DemoListAdapter;
import com.jianyuyouhun.jmvp.app.App;
import com.jianyuyouhun.jmvp.mvp.adaptertest.AdapterTestModel;
import com.jianyuyouhun.jmvp.mvp.adaptertest.AdapterTestPresenter;
import com.jianyuyouhun.jmvp.mvp.adaptertest.AdapterTestView;
import com.jianyuyouhun.jmvplib.app.BaseMVPActivity;
import com.jianyuyouhun.jmvplib.utils.injecter.view.FindViewById;

import java.util.List;

public class AdapterTestActivity extends BaseMVPActivity<AdapterTestPresenter, AdapterTestModel> implements AdapterTestView {

    @FindViewById(R.id.textView)
    private TextView mTextView;

    @FindViewById(R.id.listView)
    private ListView mListView;

    private DemoListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        mPresenter.test();
    }

    private void initView() {
        adapter = new DemoListAdapter(getContext());
        mListView.setAdapter(adapter);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_adapter_test;
    }

    @NonNull
    @Override
    protected AdapterTestModel initModel() {
        return App.getInstance().getJModel(AdapterTestModel.class);
    }

    @Override
    protected void bindModelAndView(AdapterTestModel mModel, AdapterTestPresenter mPresenter) {
        mPresenter.onBindModelView(mModel, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showLoading() {
        showProgressDialog();
        showToast("showLoading");
    }

    @Override
    public void hideLoading() {
        dismissProgressDialog();
        showToast("hideLoading");
    }

    @Override
    public void showError(String error) {
        showToast(error);
        logE(error);
    }

    @Override
    public void onDataSuccess(List<String> list, String msg) {
        mTextView.setText(msg);
        adapter.setData(list);
    }
}
