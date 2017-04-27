package com.jianyuyouhun.jmvplib.utils.http;

/**
 * 上传异常
 * Created by wangyu on 2017/4/27.
 */

public class UploadException extends RuntimeException {
    public UploadException() {
        this("找不到对应文件");
    }
    public UploadException(String message) {
        super(message);
    }
}
