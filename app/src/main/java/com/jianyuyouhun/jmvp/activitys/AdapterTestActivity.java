package com.jianyuyouhun.jmvp.activitys;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.jianyuyouhun.jmvp.R;
import com.jianyuyouhun.jmvp.adapter.DemoListAdapter;
import com.jianyuyouhun.jmvp.app.App;
import com.jianyuyouhun.jmvp.mvp.adaptertest.AdapterTestModel;
import com.jianyuyouhun.jmvp.mvp.adaptertest.AdapterTestPresenter;
import com.jianyuyouhun.jmvp.mvp.adaptertest.AdapterTestView;
import com.jianyuyouhun.jmvplib.app.BaseMVPActivity;
import com.jianyuyouhun.jmvplib.utils.injecter.FindViewById;

import java.util.ArrayList;
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
        mPresenter.beginPresent();
    }

    private void initView() {
        adapter = new DemoListAdapter(getContext());
        mListView.setAdapter(adapter);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_adapter_test;
    }

    @Override
    protected AdapterTestPresenter getPresenter() {
        return new AdapterTestPresenter();
    }

    @Override
    protected AdapterTestModel initModel() {
        return App.getInstance().getJModel(AdapterTestModel.class);
    }

    @Override
    protected boolean bindModelAndView() {
        mPresenter.onBindModelView(mModel, this);
        return true;
    }

    private void initList(String s) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < s.length(); i++) {
            String str = String.valueOf(s.charAt(i));
            if (!str.equals(" ")) {
                list.add(String.valueOf(s.charAt(i)));
            }
        }
        adapter.setData(list);
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
    public void onDataSuccess(String s) {
        mTextView.setText(s);
        initList(s);
    }
}
