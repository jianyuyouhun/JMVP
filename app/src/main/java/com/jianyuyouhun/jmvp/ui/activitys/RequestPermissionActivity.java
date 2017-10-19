package com.jianyuyouhun.jmvp.ui.activitys;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jianyuyouhun.inject.annotation.FindViewById;
import com.jianyuyouhun.jmvp.R;
import com.jianyuyouhun.jmvplib.app.BaseActivity;
import com.jianyuyouhun.jmvplib.utils.AppHelper;
import com.jianyuyouhun.permission.library.EZPermission;
import com.jianyuyouhun.permission.library.OnRequestPermissionResultListener;
import com.jianyuyouhun.permission.library.PRequester;

/**
 * 动态权限demo
 * Created by wangyu on 2017/5/3.
 */

public class RequestPermissionActivity extends BaseActivity {

    @FindViewById(R.id.storage)
    private Button storage;

    @FindViewById(R.id.camera)
    private Button camera;

    @FindViewById(R.id.phone)
    private Button phone;

    @FindViewById(R.id.tips)
    private TextView mTips;

    private OnRequestPermissionResultListener requestListener = new OnRequestPermissionResultListener() {
        @Override
        public void onRequestSuccess(String permission) {
            showToast("成功获取" + permission + "权限");
        }

        @Override
        public void onRequestFailed(String permission) {
            showToast("获取" + permission + "权限失败");
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTips.setText("Current SDK:" + AppHelper.getOsVersion());
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            showToast("当前版本小于Android 6.0 无需做动态权限申请");
        }
        setListener();
    }

    private void setListener() {
        storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EZPermission.Companion.getInstance()
                        .requestPermission(
                                getActivity(),
                                new PRequester(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                requestListener);
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EZPermission.Companion.getInstance()
                        .requestPermission(
                                getActivity(),
                                new PRequester(Manifest.permission.CAMERA),
                                requestListener);
            }
        });
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EZPermission.Companion.getInstance()
                        .requestPermission(
                                getActivity(),
                                new PRequester(Manifest.permission.CALL_PHONE),
                                requestListener);
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_permission_test;
    }
}
