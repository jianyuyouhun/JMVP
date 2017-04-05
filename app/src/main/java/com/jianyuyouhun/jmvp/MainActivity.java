package com.jianyuyouhun.jmvp;

import android.os.Bundle;
import android.widget.TextView;

import com.jianyuyouhun.jmvplib.app.BaseActivity;
import com.jianyuyouhun.jmvplib.app.JApp;
import com.jianyuyouhun.jmvplib.utils.injecter.FindViewById;

public class MainActivity extends BaseActivity<DickPresenter, DickModel> {

    @FindViewById(R.id.textView)
    private TextView mTextView;

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
            dismissProgressDialog();
            showToast(error);
        }

        @Override
        public void onDataSuccess(String s) {
            mTextView.setText(s);
            showToast(s);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setIsMainOn(true);
        mPresenter.beginPresent();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public DickPresenter getPresenter() {
        return JApp.getInstance().getJPresenter(DickPresenter.class);
    }

    @Override
    protected DickModel initModel() {
        return new DickModel();
    }

    @Override
    public boolean bindModelAndView() {
        mPresenter.onBindModelView(mModel, view);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setIsMainOn(false);
    }
}
