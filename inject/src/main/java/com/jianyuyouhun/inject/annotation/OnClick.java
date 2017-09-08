package com.jianyuyouhun.inject.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 控件的点击事件注解
 * <p/>
 * <p/>
 * <pre>
 * &#064;OnClick({ R.id.viewId })
 * public void onClick(View view) {
 * 	// 点击事件
 * }
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnClick {
    int[] value();
}
