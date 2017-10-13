package com.jianyuyouhun.jmvplib.utils.http.autorequest;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * JSON路径 JSON_PATH
 * Created by wangyu on 2017/8/16.
 */
@Retention(RUNTIME)
@Target(PARAMETER)
public @interface JsonPath {
    String value();
}
