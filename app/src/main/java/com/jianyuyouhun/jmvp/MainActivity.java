package com.jianyuyouhun.jmvp;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.jianyuyouhun.jmvp.adapter.DemoListAdapter;
import com.jianyuyouhun.jmvplib.app.BaseActivity;
import com.jianyuyouhun.jmvplib.app.JApp;
import com.jianyuyouhun.jmvplib.utils.injecter.FindViewById;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<DickPresenter, DickModel> {

    @FindViewById(R.id.textView)
    private TextView mTextView;

    @FindViewById(R.id.listView)
    private ListView mListView;

    private DemoListAdapter adapter;

    private DickView view = new DickView() {
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
        }

        @Override
        public void onDataSuccess(String s) {
            mTextView.setText(s);
            initList(s);
        }
    };

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
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public DickPresenter getPresenter() {
        return new DickPresenter();
    }

    @Override
    protected DickModel initModel() {
        return JApp.getInstance().getJModel(DickModel.class);
    }

    @Override
    public boolean bindModelAndView() {
        mPresenter.onBindModelView(mModel, view);
        return true;
    }

    private void initList(String s) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < s.length(); i++) {
            list.add(String.valueOf(s.charAt(i)));
        }
        adapter.setData(list);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setIsMainOn(false);
    }
}
