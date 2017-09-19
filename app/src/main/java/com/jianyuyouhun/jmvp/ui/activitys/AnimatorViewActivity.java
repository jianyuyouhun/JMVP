package com.jianyuyouhun.jmvp.ui.activitys;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.jianyuyouhun.inject.annotation.FindViewById;
import com.jianyuyouhun.inject.annotation.OnClick;
import com.jianyuyouhun.jmvp.R;
import com.jianyuyouhun.jmvp.app.server.db.DataBaseModel;
import com.jianyuyouhun.jmvp.app.server.notification.NotificationModel;
import com.jianyuyouhun.jmvplib.app.BaseActivity;
import com.jianyuyouhun.jmvplib.utils.injecter.model.Model;
import com.suke.widget.SwitchButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 控件展示
 * Created by wangyu on 2017/8/31.
 */

public class AnimatorViewActivity extends BaseActivity {

    @FindViewById(R.id.switch_btn)
    private SwitchButton mSwitchButton;

    @FindViewById(R.id.switch_state)
    private TextView mSwitchState;

    @FindViewById(R.id.search_result)
    private TextView mSearchResult;

    @Model
    private NotificationModel mNotificationModel;

    @Model
    private DataBaseModel mDataBaseModel;

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

    @OnClick(R.id.hangup_btn)
    private void onHangupClick(View view) {
        mNotificationModel.notifyNewMsg("通知");
    }

    @OnClick(R.id.search_btn)
    private void onSearch(View view) {
        mDataBaseModel.submitDBTask(new DataBaseModel.DBTask<String>() {
            @Override
            public String runOnDBThread(SQLiteDatabase sqLiteDatabase) {
                Cursor cursor = sqLiteDatabase.rawQuery("select * from user where user_age < 22", null);
                JSONArray jsonArray = new JSONArray();
                while (cursor.moveToNext()) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("uid", cursor.getString(0));
                        jsonObject.put("name", cursor.getString(1));
                        jsonObject.put("age", cursor.getString(2));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject);
                }
                return jsonArray.toString();
            }

            @Override
            public void runOnUIThread(String s) {
                mSearchResult.setText(s);
            }
        });
    }


}
