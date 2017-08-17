package com.jianyuyouhun.jmvp.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.jianyuyouhun.jmvp.R;
import com.jianyuyouhun.jmvp.view.AutoScrollDialog;
import com.jianyuyouhun.jmvplib.app.BaseActivity;
import com.jianyuyouhun.jmvplib.utils.injecter.view.FindViewById;

/**
 * 首页
 * Created by jianyuyouhun on 2017/4/26.
 */

public class MainActivity extends BaseActivity {

    @FindViewById(R.id.adapter_test)
    private Button adapterButton;

    @FindViewById(R.id.http_get)
    private Button httpButton;

    @FindViewById(R.id.permission_test)
    private Button permissionButton;

    @FindViewById(R.id.image_load_test)
    private Button imageButton;

    @FindViewById(R.id.login_test)
    private Button loginButton;

    @FindViewById(R.id.dialog_test)
    private Button mDialogTestButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        permissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(RequestPermissionActivity.class);
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ImageLoadTestActivity.class);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(LoginTestActivity.class);
            }
        });
        mDialogTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoScrollDialog autoScrollDialog = new AutoScrollDialog(getActivity());
                autoScrollDialog.show();
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }
}
