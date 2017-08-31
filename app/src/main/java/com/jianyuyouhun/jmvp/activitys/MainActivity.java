package com.jianyuyouhun.jmvp.activitys;

import android.view.View;

import com.jianyuyouhun.jmvp.R;
import com.jianyuyouhun.jmvp.view.AutoScrollDialog;
import com.jianyuyouhun.jmvplib.app.BaseActivity;
import com.jianyuyouhun.jmvplib.utils.injecter.view.OnClick;

/**
 * 首页
 * Created by jianyuyouhun on 2017/4/26.
 */

public class MainActivity extends BaseActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
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
        AutoScrollDialog autoScrollDialog = new AutoScrollDialog(getActivity());
        autoScrollDialog.show();
    }

    @OnClick({R.id.animator_view_test})
    protected void onAnimatorViewClick(View view) {
        startActivity(AnimatorViewActivity.class);
    }
}
