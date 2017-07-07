package com.jianyuyouhun.jmvplib.utils.injecter.model;

import com.jianyuyouhun.jmvplib.app.JApp;
import com.jianyuyouhun.jmvplib.mvp.BaseJModel;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * 注解model
 * Created by wangyu on 2017/6/30.
 */

public class ModelInjector {

    public static void injectModel(Object object) {
        Class<?> aClass = object.getClass();

        while (aClass != BaseJModel.class && aClass != Object.class) {
            Field[] declaredFields = aClass.getDeclaredFields();
            if (declaredFields != null && declaredFields.length > 0) {
                for (Field field : declaredFields) {
                    int modifiers = field.getModifiers();
                    if (Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers)) {
                        // 忽略掉static 和 final 修饰的变量
                        continue;
                    }

                    if (!field.isAnnotationPresent(Model.class)) {
                        continue;
                    }

                    Class<?> type = field.getType();
                    if (!BaseJModel.class.isAssignableFrom(type)) {
                        throw new RuntimeException("@Model 注解只能应用到BaseJModelImpl的子类");
                    }
                    @SuppressWarnings("unchecked")
                    BaseJModel baseManager = JApp.getInstance().getJModel((Class<? extends BaseJModel>) type);

                    if (baseManager == null) {
                        throw new RuntimeException(type.getSimpleName() + "Model还未初始化！");
                    }

                    if (!field.isAccessible())
                        field.setAccessible(true);

                    try {
                        field.set(object, baseManager);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }

            aClass = aClass.getSuperclass();
        }
    }
}
