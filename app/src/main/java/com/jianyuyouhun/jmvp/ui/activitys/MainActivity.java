package com.jianyuyouhun.jmvp.ui.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.jianyuyouhun.inject.annotation.FindViewById;
import com.jianyuyouhun.inject.annotation.OnClick;
import com.jianyuyouhun.inject.annotation.OnLongClick;
import com.jianyuyouhun.jmvp.R;
import com.jianyuyouhun.jmvp.view.AutoScrollDialog;
import com.jianyuyouhun.jmvplib.app.BaseActivity;
import com.jianyuyouhun.jmvplib.view.TouchEnableChildLayout;

/**
 * 首页
 * Created by jianyuyouhun on 2017/4/26.
 */

public class MainActivity extends BaseActivity {

    @FindViewById(R.id.touch_enable_layout)
    private TouchEnableChildLayout mAllLayout;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAllLayout.setEnabled(true);
    }

    @OnClick({R.id.adapter_test})
    protected void onAdapterClick(View view) {
        startActivity(AdapterTestActivity.class);
    }

    @OnClick({R.id.http_get})
    protected void onHttpClick(View view) {
        startActivity(HttpTestActivity.class);
    }

    @OnClick({R.id.permission_test})
    protected void onPermissionClick(View view) {
        startActivity(RequestPermissionActivity.class);
    }

    @OnClick({R.id.image_load_test})
    protected void onImageLoadClick(View view) {
        startActivity(ImageLoadTestActivity.class);
    }

    @OnClick({R.id.login_test})
    protected void onLoginTestClick(View view) {
        startActivity(LoginTestActivity.class);
    }

    @OnClick({R.id.dialog_test})
    protected void onDialogTestClick(View view) {
        new AutoScrollDialog(getActivity()).show();
    }

    @OnClick({R.id.animator_view_test})
    protected void onAnimatorViewClick(View view) {
        startActivity(AnimatorViewActivity.class);
    }

    @OnClick(R.id.view_pagers_test)
    protected void onViewPagersClick(View view) {
        startActivity(ViewPagerActivity.class);
    }

    @OnLongClick({
            R.id.dialog_test,
            R.id.adapter_test,
            R.id.image_load_test,
            R.id.permission_test,
            R.id.animator_view_test,
            R.id.login_test,
            R.id.http_get,
            R.id.view_pagers_test})
    protected boolean onDialogLongClick(View view) {
        showToast("别乱按");
        return true;
    }

}
