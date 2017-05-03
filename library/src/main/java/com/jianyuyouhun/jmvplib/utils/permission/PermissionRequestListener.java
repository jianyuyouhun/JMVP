package com.jianyuyouhun.jmvplib.utils.permission;

/**
 * 动态权限请求结果
 * Created by wangyu on 2017/5/3.
 */

public interface PermissionRequestListener {
    void onRequestSuccess(String permission, String permissionName);
    void onRequestFailed(String permission, String permissionName);
}
