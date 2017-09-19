package com.jianyuyouhun.jmvp.ui.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.jianyuyouhun.inject.annotation.FindViewById;
import com.jianyuyouhun.inject.annotation.OnClick;
import com.jianyuyouhun.jmvp.R;
import com.jianyuyouhun.jmvp.app.server.notification.NotificationModel;
import com.jianyuyouhun.jmvplib.app.BaseActivity;
import com.jianyuyouhun.jmvplib.utils.injecter.model.Model;
import com.suke.widget.SwitchButton;

/**
 * 控件展示
 * Created by wangyu on 2017/8/31.
 */

public class AnimatorViewActivity extends BaseActivity {

    @FindViewById(R.id.switch_btn)
    private SwitchButton mSwitchButton;

    @FindViewById(R.id.switch_state)
    private TextView mSwitchState;

    @Model
    private NotificationModel mNotificationModel;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_animator_view_collect;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSwitchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                refreshState(isChecked);
            }
        });
        refreshState(mSwitchButton.isChecked());
    }

    private void refreshState(boolean isChecked) {
        mSwitchState.setText(isChecked ? "选中了" : "未选中");
    }

    boolean isString = false;

    @OnClick(R.id.hangup_btn)
    private void onHangupClick(View view) {
        mNotificationModel.notifyNewMsg(isString ? "通知" : 1);
        isString = !isString;
    }
}
