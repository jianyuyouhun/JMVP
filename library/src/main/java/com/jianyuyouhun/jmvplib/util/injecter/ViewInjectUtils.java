package com.jianyuyouhun.jmvplib.util.injecter;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;

import java.lang.reflect.Field;

/**
 * 注解绑定类
 * Created by wangyu on 2017/4/5.
 */

public class ViewInjectUtils {
    /**
     * 注入View和OnClickListener，通常在Fragment和Adapter中使用
     *
     * @param target 需要被注入的对象
     * @param view   view查找对象
     */
    public static void inject(Object target, View view) {
        injectView(target, ViewFinder.create(view));
    }

    /**
     * 注入View和OnClickListener
     */
    public static void inject(Dialog dialog) {
        injectView(dialog, ViewFinder.create(dialog));
    }

    /**
     * 注入View和OnClickListener
     */
    public static void inject(Activity activity) {
        injectView(activity, ViewFinder.create(activity));
    }

    /**
     * 注入View和OnClickListener, 通常在Fragment和Adapter中使用
     */
    public static void inject(Object target, ViewFinder viewFinder) {
        injectView(target, viewFinder);
    }

    /** 注入View {@link #injectView(Object, ViewFinder)} */
    public static void injectView(Object target, View view) {
        injectView(target, ViewFinder.create(view));
    }

    /** 注入View {@link #injectView(Object, ViewFinder)} */
    public static void injectView(Dialog dialog) {
        injectView(dialog, ViewFinder.create(dialog));
    }

    /** 注入View {@link #injectView(Object, ViewFinder)} */
    public static void injectView(Activity activity) {
        injectView(activity, ViewFinder.create(activity));
    }

    /**
     * 注入View
     *
     * @param target
     * @param viewFinder
     */
    public static void injectView(Object target, ViewFinder viewFinder) {
        if (target == null || viewFinder == null) {
            throw new NullPointerException();
        }
        Class<?> cls = target.getClass();
        while (cls != null && cls != Object.class && cls != Activity.class && cls != View.class) {
            Field[] declaredFields = cls.getDeclaredFields();
            for (Field field : declaredFields) {
                if (!field.isAccessible())
                    field.setAccessible(true);
                FindViewById findViewById = field.getAnnotation(FindViewById.class);
                if (findViewById == null) {
                    continue;
                } else {
                    int id = findViewById.value();
                    View view = viewFinder.findViewById(id);
                    if (view == null) {
                        // 根据注解的ViewID，无法查找到View， 检查控件在对应的XML中的ID是否存在
                        String msg = field.getName() + " 根据ID不能查找到对应的View，请检查XML资源文件中的id属性是否等于注解的ID";
                        throw new ViewInjectException(msg);
                    } else if (!field.getType().isInstance(view)) {
                        // 控件类型不匹配， 请检查XML中的控件类型和java代码类型是否匹配
                        String msg = field.getName() + " 类型匹配错误，java类型：" + field.getType().getSimpleName()
                                + ", View在XML中申明的类型：" + view.getClass().getSimpleName() + "，请检查XML中的控件类型和java代码类型是否匹配";
                        throw new ViewInjectException(msg);
                    } else {
                        try {
                            field.set(target, view);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            cls = cls.getSuperclass();
        }
    }

}
