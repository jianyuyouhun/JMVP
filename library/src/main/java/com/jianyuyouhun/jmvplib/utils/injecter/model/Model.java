package com.jianyuyouhun.jmvplib.utils.injecter.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * model注解
 * presenter, model中使用
 * Created by wangyu on 2017/6/30.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Model {
}
