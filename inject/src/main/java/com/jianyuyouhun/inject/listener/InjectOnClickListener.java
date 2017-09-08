package com.jianyuyouhun.inject.listener;

import android.view.View;
import android.view.View.OnClickListener;

import java.lang.reflect.Method;

public class InjectOnClickListener implements OnClickListener {
    private Method method;
    private Object target;

    public InjectOnClickListener(Method method, Object target) {
        super();
        this.method = method;
        this.target = target;
    }

    @Override
    public void onClick(View v) {
        try {
            method.invoke(target, v);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
