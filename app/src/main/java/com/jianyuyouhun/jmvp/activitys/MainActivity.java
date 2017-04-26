package com.jianyuyouhun.jmvp.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.jianyuyouhun.jmvp.R;
import com.jianyuyouhun.jmvplib.app.BaseActivity;
import com.jianyuyouhun.jmvplib.utils.injecter.FindViewById;

/**
 * 首页
 * Created by jianyuyouhun on 2017/4/26.
 */

public class MainActivity extends BaseActivity {

    @FindViewById(R.id.adapter_test)
    private Button adapterButton;

    @FindViewById(R.id.http_test)
    private Button httpButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setIsMainOn(true);
        setListener();
    }

    private void setListener() {
        adapterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(AdapterTestActivity.class);
            }
        });
        httpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(HttpTestActivity.class);
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setIsMainOn(false);
    }
}
