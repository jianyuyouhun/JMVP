package com.jianyuyouhun.jmvp.activitys;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.jianyuyouhun.jmvp.R;
import com.jianyuyouhun.jmvp.app.App;
import com.jianyuyouhun.jmvp.mvp.httpTest.HttpTestModel;
import com.jianyuyouhun.jmvp.mvp.httpTest.HttpTestPresenter;
import com.jianyuyouhun.jmvp.mvp.httpTest.HttpTestView;
import com.jianyuyouhun.jmvplib.app.BaseMVPActivity;
import com.jianyuyouhun.jmvplib.utils.injecter.FindViewById;

/**
 * http测试页面
 * Created by jianyuyouhun on 2017/4/26.
 */

public class HttpTestActivity extends BaseMVPActivity<HttpTestPresenter, HttpTestModel> implements HttpTestView {

    @FindViewById(R.id.http_test)
    private Button testButton;

    @FindViewById(R.id.webView)
    private WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.beginPresent();
            }
        });
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_http_test;
    }

    @NonNull
    @Override
    protected HttpTestModel initModel() {
        return App.getInstance().getJModel(HttpTestModel.class);
    }

    @Override
    protected void bindModelAndView(HttpTestPresenter mPresenter) {
        mPresenter.onBindModelView(mModel, this);
    }

    @Override
    public void showError(String error) {
        showToast(error);
    }

    @Override
    public void showDialog() {
        showProgressDialog();
    }

    @Override
    public void hideDialog() {
        dismissProgressDialog();
    }

    @Override
    public void showHtml(String htmlString) {
        mWebView.loadData(htmlString, "text/html; charset=UTF-8", null);
    }
}
