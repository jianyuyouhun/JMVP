package com.jianyuyouhun.jmvp.activitys;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jianyuyouhun.inject.annotation.FindViewById;
import com.jianyuyouhun.jmvp.R;
import com.jianyuyouhun.jmvp.app.App;
import com.jianyuyouhun.jmvp.mvp.httpTest.HttpTestModel;
import com.jianyuyouhun.jmvp.mvp.httpTest.HttpTestPresenter;
import com.jianyuyouhun.jmvp.mvp.httpTest.HttpTestView;
import com.jianyuyouhun.jmvplib.app.BaseMVPActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * http测试页面
 * Created by jianyuyouhun on 2017/4/26.
 */

public class HttpTestActivity extends BaseMVPActivity<HttpTestPresenter, HttpTestModel> implements HttpTestView {

    @FindViewById(R.id.http_url_input)
    private EditText mUrlInput;

    @FindViewById(R.id.http_get)
    private Button testGet;

    @FindViewById(R.id.http_post)
    private Button testPost;

    @FindViewById(R.id.result)
    private TextView result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.doHttpTest(false);
            }
        });
        testPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.doHttpTest(true);
            }
        });
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
    protected void bindModelAndView(HttpTestModel mModel, HttpTestPresenter mPresenter) {
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
        try {
            JSONObject object = new JSONObject(htmlString);
            Iterator<String> sIterator = object.keys();
            String outPut = "";
            while(sIterator.hasNext()){
                // 获得key
                String key = sIterator.next();
                // 根据key获得value, value也可以是JSONObject,JSONArray,使用对应的参数接收即可
                String value = object.getString(key);
                outPut += key + ":" + value + "\n";
                System.out.println("key: "+key+",value"+value);
            }
            result.setText(outPut);
        } catch (JSONException e) {
            e.printStackTrace();
            result.setText(htmlString);
        }
    }

    @Override
    public String getUrl() {
        return mUrlInput.getText().toString().trim();
    }
}
