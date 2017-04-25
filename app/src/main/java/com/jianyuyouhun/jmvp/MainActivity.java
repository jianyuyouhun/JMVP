package com.jianyuyouhun.jmvp;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.jianyuyouhun.jmvp.adapter.DemoListAdapter;
import com.jianyuyouhun.jmvplib.app.BaseMVPActivity;
import com.jianyuyouhun.jmvplib.app.JApp;
import com.jianyuyouhun.jmvplib.utils.injecter.FindViewById;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseMVPActivity<DickPresenter, DickModel> implements DickView {

    @FindViewById(R.id.textView)
    private TextView mTextView;

    @FindViewById(R.id.listView)
    private ListView mListView;

    private DemoListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setIsMainOn(true);
        initView();
        mPresenter.beginPresent();
    }

    private void initView() {
        adapter = new DemoListAdapter(getContext());
        mListView.setAdapter(adapter);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected DickPresenter getPresenter() {
        return new DickPresenter();
    }

    @Override
    protected DickModel initModel() {
        return JApp.getInstance().getJModel(DickModel.class);
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
        setIsMainOn(false);
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
