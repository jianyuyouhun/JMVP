package com.jianyuyouhun.jmvplib.mvp.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.jianyuyouhun.jmvplib.app.JApp;
import com.jianyuyouhun.jmvplib.mvp.BaseJModelImpl;

/**
 * 权限记录
 * Created by wangyu on 2017/5/4.
 */

public class PermissionModel extends BaseJModelImpl {

    private static final String JAPP_CACHE_NAME = "japp_permission_cache";

    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    @Override
    public void onModelCreate(JApp app) {
        super.onModelCreate(app);
        sp = app.getSharedPreferences(JAPP_CACHE_NAME, Context.MODE_PRIVATE);
        spEditor = sp.edit();
        spEditor.apply();
    }

    /**
     * SP中写入boolean类型value
     *
     * @param permission    权限
     * @param isIgnore      是否不再提示
     */
    public void putPermissionRecord(String permission, boolean isIgnore) {
        spEditor.putBoolean(permission, isIgnore).apply();
    }

    /**
     * 是否不再提示
     * @param permission    权限
     * @return    是否不再提示
     */
    public boolean getPermissionRecord(String permission) {
        return sp.getBoolean(permission, false);
    }
}