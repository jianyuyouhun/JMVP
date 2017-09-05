package com.jianyuyouhun.jmvplib.mvp.model.permission;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;

import com.jianyuyouhun.jmvplib.R;
import com.jianyuyouhun.jmvplib.app.BaseActivity;
import com.jianyuyouhun.jmvplib.utils.injecter.model.Model;
import com.jianyuyouhun.jmvplib.utils.injecter.model.ModelInjector;

import java.util.Map;

/**
 * 动态权限处理
 * Created by wangyu on 2017/5/3.
 */

public class PermissionRequester {

    @StringRes
    private int title;
    @StringRes
    private int message;
    @StringRes
    private int negativeButtonText;
    @StringRes
    private int positiveButtonText;

    @Model
    private PermissionModel permissionModel;

    public static final int REQUEST_PERMISSION_CODE_DEFAULT = 0x0001;

    private String permission;
    private int requestCode;
    private String permissionName;

    private OnRequestPermissionResultListener onRequestPermissionResultListener;

    public PermissionRequester() {
        setContentText();
        ModelInjector.injectModel(this);
    }

    private void setContentText() {
        setContentText(R.string.permission_title, R.string.permission_message);
    }

    public void setContentText(@StringRes int title, @StringRes int message) {
        setContentText(title, message, R.string.permission_ok, R.string.permission_cancel);
    }

    /**
     *
     * @param title                 标题
     * @param message               消息
     * @param positiveButtonText    积极按钮文本
     * @param negativeButtonText    消极按钮文本
     */
    public void setContentText(@StringRes int title, @StringRes int message, @StringRes int positiveButtonText, @StringRes int negativeButtonText) {
        this.title = title;
        this.message = message;
        this.positiveButtonText = positiveButtonText;
        this.negativeButtonText = negativeButtonText;
    }

    public void requestPermission(final BaseActivity activity, final String permissionName, final String permission) {
        requestPermission(activity, permissionName, permission, REQUEST_PERMISSION_CODE_DEFAULT);
    }

    /**
     * 请求权限
     * @param activity          activity
     * @param permissionName    权限名称
     * @param permission        权限
     * @param code              请求码
     */
    public void requestPermission(final BaseActivity activity, final String permissionName, final String permission,
                                  int code) {
        this.permission = permission;
        this.permissionName = permissionName;
        this.requestCode = code;
        if (ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
            if (onRequestPermissionResultListener == null) return;
            onRequestPermissionResultListener.onRequestSuccess(permission, permissionName);
        } else {
            //没有权限的时候直接尝试获取权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                permissionModel.putPermissionRecord(permission, false);
            }
            ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
        }
    }

    /**
     * 处理申请结果
     * @param requestCode       请求码
     * @param permissions       权限
     * @param grantResults      状态
     */
    public void onRequestPermissionsResult(final BaseActivity activity, final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if (requestCode == this.requestCode) {
            if (grantResults.length == 0) {
                if (onRequestPermissionResultListener == null) return;
                onRequestPermissionResultListener.onRequestFailed(permission, permissionName);
            } else {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (onRequestPermissionResultListener == null) return;
                    onRequestPermissionResultListener.onRequestSuccess(permission, permissionName);
                } else {
                    if (permissionModel.getPermissionRecord(permission)) {//如果被禁止不在询问
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle(title);
                        builder.setMessage(activity.getResources().getString(message, permissionName));
                        builder.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                activity.startSystemSettingActivity(requestCode);
                            }
                        });
                        builder.setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if (onRequestPermissionResultListener == null) return;
                                onRequestPermissionResultListener.onRequestFailed(permission, permissionName);
                            }
                        });
                        builder.show();
                    } else if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                        permissionModel.putPermissionRecord(permission, true);
                        if (onRequestPermissionResultListener == null) return;
                        onRequestPermissionResultListener.onRequestFailed(permission, permissionName);
                    } else {
                        if (onRequestPermissionResultListener == null) return;
                        onRequestPermissionResultListener.onRequestFailed(permission, permissionName);
                    }
                }
            }
        }
    }

    /**
     *
     * @param activity      activity
     * @param requestCode   请求码
     * @param resultCode    结果
     * @param data          intent
     */
    public void onActivityResult(BaseActivity activity, int requestCode, int resultCode, Intent data) {
        if (requestCode == this.requestCode) {
            if (ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
                if (onRequestPermissionResultListener == null) return;
                onRequestPermissionResultListener.onRequestSuccess(permission, permissionName);
            } else {
                if (onRequestPermissionResultListener == null) return;
                onRequestPermissionResultListener.onRequestFailed(permission, permissionName);
            }
            synchronizePermissionsState(activity);
        }
    }

    /**
     * 同步其他权限禁用记录
     *
     */
    public void synchronizePermissionsState(BaseActivity activity) {
        Map<String, Boolean> allPermissionMap = permissionModel.getAllPermissionMap();
        if (allPermissionMap == null) {//取不到缓存
            return;
        }
        for (String permission : allPermissionMap.keySet()) {
            if (ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
                permissionModel.putPermissionRecord(permission, false);//授予的权限都将禁用记录置空
            } else if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                permissionModel.putPermissionRecord(permission, true);//用户喜欢在设置页面点来点去的时候就是这里了
            } else {
                permissionModel.putPermissionRecord(permission, false);//怕出错，加上这行保险
            }
        }
    }

    /**
     * 设置权限申请监听器
     * @param onRequestPermissionResultListener   回调
     */
    public void setOnRequestPermissionResultListener(@NonNull OnRequestPermissionResultListener onRequestPermissionResultListener) {
        this.onRequestPermissionResultListener = onRequestPermissionResultListener;
    }
}
