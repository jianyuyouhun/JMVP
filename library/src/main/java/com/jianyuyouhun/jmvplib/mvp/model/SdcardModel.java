package com.jianyuyouhun.jmvplib.mvp.model;

import android.os.Environment;
import android.text.TextUtils;

import com.jianyuyouhun.jmvplib.app.JApp;
import com.jianyuyouhun.jmvplib.mvp.BaseJModel;

/**
 * 存储管理
 * Created by wangyu on 2017/5/10.
 */

public class SdcardModel extends BaseJModel<JApp> {

    private static final String IMG_DIR = "/img/";

    private String JAPP_ROOT_DIR;


    @Override
    public void onModelCreate(JApp app) {
        super.onModelCreate(app);
        JAPP_ROOT_DIR = Environment.getExternalStorageDirectory().getPath();
    }

    public String getImgPath(String url) {
        return JAPP_ROOT_DIR + IMG_DIR + getFileName(url);
    }

    public String getFileName(String url) {
        if (!TextUtils.isEmpty(url)) {
            return (url.hashCode() + "").replace("-", "_");
        } else {
            return "";
        }
    }
}
