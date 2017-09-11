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
import com.jianyuyouhun.jmvplib.mvp.model.permission.OnRequestPermissionResultListener;

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
        public void onRequestSuccess(String permission, String permissionName) {
            showToast("成功获取" + permissionName + "权限");
        }

        @Override
        public void onRequestFailed(String permission, String permissionName) {
            showToast("获取" + permissionName + "权限失败");
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
        permissionRequester.setOnRequestPermissionResultListener(requestListener);
        storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionRequester.requestPermission(getActivity(), "存储", Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionRequester.requestPermission(getActivity(), "相机", Manifest.permission.CAMERA);
            }
        });
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionRequester.requestPermission(getActivity(), "电话", Manifest.permission.CALL_PHONE);
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_permission_test;
    }
}
