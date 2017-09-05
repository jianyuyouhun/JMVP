package com.jianyuyouhun.jmvplib.mvp.model.permission;

/**
 * 动态权限请求结果
 * Created by wangyu on 2017/5/3.
 */

public interface OnRequestPermissionResultListener {
    void onRequestSuccess(String permission, String permissionName);
    void onRequestFailed(String permission, String permissionName);
}
