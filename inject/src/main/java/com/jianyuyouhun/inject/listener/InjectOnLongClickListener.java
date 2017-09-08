package com.jianyuyouhun.inject.listener;

import android.view.View;

import java.lang.reflect.Method;

/**
 * 注解注入的长按事件
 * Created by wangyu on 2017/9/1.
 */

public class InjectOnLongClickListener implements View.OnLongClickListener {
    private Method method;
    private Object target;

    public InjectOnLongClickListener(Method method, Object target) {
        super();
        this.method = method;
        this.target = target;
    }

    @Override
    public boolean onLongClick(View v) {
        try {
            return (boolean) method.invoke(target, v);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;//出现异常时返回false，不拦截事件
    }
}
