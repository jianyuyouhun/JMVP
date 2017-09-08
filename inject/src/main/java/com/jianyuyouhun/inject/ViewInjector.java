package com.jianyuyouhun.inject;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;

import com.jianyuyouhun.inject.annotation.FindViewById;
import com.jianyuyouhun.inject.annotation.OnClick;
import com.jianyuyouhun.inject.annotation.OnLongClick;
import com.jianyuyouhun.inject.listener.InjectOnClickListener;
import com.jianyuyouhun.inject.listener.InjectOnLongClickListener;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * 注解绑定类
 * Created by wangyu on 2017/4/5.
 */

public class ViewInjector {
    /**
     * 注入View和OnClickListener，通常在Fragment和Adapter中使用
     *
     * @param target 需要被注入的对象
     * @param view   view查找对象
     */
    public static void inject(Object target, View view) {
        injectView(target, ViewFinder.create(view));
        injectOnClick(target, ViewFinder.create(view));
        injectOnLongClick(target, ViewFinder.create(view));
    }

    /**
     * 注入View和OnClickListener
     */
    public static void inject(ViewGroup viewGroup) {
        injectView(viewGroup, ViewFinder.create(viewGroup));
        injectOnClick(viewGroup, ViewFinder.create(viewGroup));
        injectOnLongClick(viewGroup, ViewFinder.create(viewGroup));
    }

    /**
     * 注入View和OnClickListener
     */
    public static void inject(Dialog dialog) {
        injectView(dialog, ViewFinder.create(dialog));
        injectOnClick(dialog, ViewFinder.create(dialog));
        injectOnLongClick(dialog, ViewFinder.create(dialog));
    }

    /**
     * 注入View和OnClickListener
     */
    public static void inject(Activity activity) {
        injectView(activity, ViewFinder.create(activity));
        injectOnClick(activity, ViewFinder.create(activity));
        injectOnLongClick(activity, ViewFinder.create(activity));
    }

    /**
     * 注入View和OnClickListener, 通常在Fragment和Adapter中使用
     */
    public static void inject(Object target, ViewFinder viewFinder) {
        injectView(target, viewFinder);
        injectOnClick(target, viewFinder);
        injectOnLongClick(target, viewFinder);
    }

///////////////////////////////////////////////////////////////////////////////////////////////////

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
                if (findViewById != null) {
                    int id = findViewById.value();
                    View view = viewFinder.bindView(id);
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
                        } catch (IllegalAccessException | IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            cls = cls.getSuperclass();
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////

    public static void injectOnClick(Object target, View view) {
        injectOnClick(target, ViewFinder.create(view));
    }

    public static void injectOnClick(Dialog dialog) {
        injectOnClick(dialog, ViewFinder.create(dialog));
    }

    public static void injectOnClick(Activity activity) {
        injectOnClick(activity, ViewFinder.create(activity));
    }

    public static void injectOnClick(Object target, ViewFinder viewFinder) {
        if (target == null || viewFinder == null) {
            throw new NullPointerException();
        }
        Class<?> cls = target.getClass();

        while (cls != null && cls != Object.class && cls != Activity.class && cls != View.class) {
            Method[] methods = cls.getDeclaredMethods();
            for (Method method : methods) {
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }

                OnClick onClick = method.getAnnotation(OnClick.class);
                if (onClick != null) {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    Class<?> viewClass = null;
                    if (parameterTypes.length != 1) {
                        String msg = method.getName() + " OnClick注解方法有且只能有一个参数！";
                        throw new ViewInjectException(msg);
                    } else {
                        viewClass = parameterTypes[0];
                    }

                    int[] ids = onClick.value();
                    for (int id : ids) {
                        View targetView = viewFinder.bindView(id);
                        if (targetView == null) {
                            // 根据注解的ViewID，无法查找到对应的View， 检查控件在对应的XML中的ID是否存在
                            String msg = method.getName() + " 根据ID不能查找到对应的View，请检查XML资源文件中的id属性是否等于注解的ID";
                            throw new ViewInjectException(msg);
                        } else if (!viewClass.isAssignableFrom(targetView.getClass())) {
                            // 控件类型和方法参数不匹配， 请检查方法参数的类型
                            String msg = method.getName() + " 方法参数类型：" + viewClass.getName() + ", View在XML中申明的类型："
                                    + targetView.getClass().getName();
                            throw new ViewInjectException(msg);
                        } else {
                            targetView.setOnClickListener(new InjectOnClickListener(method, target));
                        }
                    }
                }
            }
            cls = cls.getSuperclass();
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////

    public static void injectOnLongClick(Object target, View view) {
        injectOnLongClick(target, ViewFinder.create(view));
    }

    public static void injectOnLongClick(Dialog dialog) {
        injectOnLongClick(dialog, ViewFinder.create(dialog));
    }

    public static void injectOnLongClick(Activity activity) {
        injectOnLongClick(activity, ViewFinder.create(activity));
    }

    public static void injectOnLongClick(Object target, ViewFinder viewFinder) {
        if (target == null || viewFinder == null) {
            throw new NullPointerException();
        }
        Class<?> cls = target.getClass();
        while (cls != null && cls != Object.class && cls != Activity.class && cls != View.class) {
            Method[] methods = cls.getDeclaredMethods();
            for (Method method : methods) {
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }

                OnLongClick onLongClick = method.getAnnotation(OnLongClick.class);
                if (onLongClick != null) {
                    Type returnType = method.getGenericReturnType();
                    if (returnType != Boolean.TYPE) {
                        String msg = method.getName() + " 方法返回类型：" + method.getName() + "的返回类型必须是boolean";
                        throw new ViewInjectException(msg);
                    }

                    Class<?>[] parameterTypes = method.getParameterTypes();
                    Class<?> viewClass = null;
                    if (parameterTypes.length != 1) {
                        String msg = method.getName() + " OnLongClick注解方法有且只能有一个参数！";
                        throw new ViewInjectException(msg);
                    } else {
                        viewClass = parameterTypes[0];
                    }

                    int[] ids = onLongClick.value();
                    for (int id : ids) {
                        View targetView = viewFinder.bindView(id);
                        if (targetView == null) {
                            // 根据注解的ViewID，无法查找到对应的View， 检查控件在对应的XML中的ID是否存在
                            String msg = method.getName() + " 根据ID不能查找到对应的View，请检查XML资源文件中的id属性是否等于注解的ID";
                            throw new ViewInjectException(msg);
                        } else if (!viewClass.isAssignableFrom(targetView.getClass())) {
                            // 控件类型和方法参数不匹配， 请检查方法参数的类型
                            String msg = method.getName() + " 方法参数类型：" + viewClass.getName() + ", View在XML中申明的类型："
                                    + targetView.getClass().getName();
                            throw new ViewInjectException(msg);
                        } else {
                            targetView.setOnLongClickListener(new InjectOnLongClickListener(method, target));
                        }
                    }
                }
            }
            cls = cls.getSuperclass();
        }
    }
}
