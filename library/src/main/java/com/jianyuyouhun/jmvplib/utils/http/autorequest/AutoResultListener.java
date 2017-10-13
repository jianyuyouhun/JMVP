package com.jianyuyouhun.jmvplib.utils.http.autorequest;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.jianyuyouhun.jmvplib.mvp.OnResultListener;
import com.jianyuyouhun.jmvplib.utils.Logger;
import com.jianyuyouhun.jmvplib.utils.json.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;

/**
 * 自动结果解析
 * Created by wangyu on 2017/8/16.
 */
public abstract class AutoResultListener implements OnResultListener<JSONObject> {

    private static final String TAG = "AutoResultListener";
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void onResult(int code, JSONObject jsonObject) {
        try {
            if (jsonObject == null) {
                jsonObject = new JSONObject();
                jsonObject.put("code", code);
            }

            Class<? extends AutoResultListener> aClass = getClass();
            Method[] methods = aClass.getDeclaredMethods();
            List<Method> targetMethods = new ArrayList<>();
            for (Method method : methods) {//去除static声明的方法
                int modifier = method.getModifiers();
                if (!Modifier.isStatic(modifier)) {
                    targetMethods.add(method);
                }
            }
            if (targetMethods.size() != 1) {
                throw new RuntimeException("错误，使用AutoResultListener自动解析服务器返回结果，类方法有且只能有一个！");
            }
            final Method method = targetMethods.get(0);
            method.setAccessible(true);

            // 泛型参数类型
            Type[] genericParameterTypes = method.getGenericParameterTypes();
            // 参数类型
            Class<?>[] parameterTypes = method.getParameterTypes();
            // 参数的注解
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();

            // 方法调用 参数
            final Object[] data = new Object[genericParameterTypes.length];

            for (int i = 0; i < parameterTypes.length; i++) {
                Object argData;
                Class parameterClass = parameterTypes[i];
                String name = getParameterName(parameterAnnotations[i]);
                if (name == null) {
                    throw new RuntimeException("第" + i + "个参数必须使用[JsonPath]注解");
                }
                // 当前 name 路径的上一个json
                String[] paths = name.split("\\.");
                JSONObject parentJSONObject = getParentJSONObject(jsonObject, paths);

                if (parentJSONObject == null) {
                    Logger.i(TAG, "第" + i + "个参数[" + name + "]在JSON中不存在～");
                    continue;
                }

                // 最后一个json的key
                String paramName = paths[paths.length - 1];
                if (!parentJSONObject.has(paramName)) {
                    Logger.i(TAG, "第" + i + "个参数[" + name + "]在JSON中不存在！");
                    continue;
                }
                if (parameterClass.isArray()) {
                    // 是数组
                    // 这是数组的实例类型
                    Class componentType = parameterClass.getComponentType();

                    JSONArray jsonArray = parentJSONObject.optJSONArray(paramName);
                    if (jsonArray == null) {
                        Logger.i(TAG, "第" + i + "个参数[" + name + "]不是一个Array～");
                        continue;
                    }

                    List listData = JsonUtil.toList(jsonArray, componentType);

                    int length = jsonArray.length();
                    Object[] result = (Object[]) Array.newInstance(componentType, length);
                    argData = result;
                    for (int index = 0; index < length; index++) {
                        result[index] = listData.get(index);
                    }
                } else if (Collection.class.isAssignableFrom(parameterClass)) {
                    // 集合 数组
                    Type genericParameterType = genericParameterTypes[i];
                    if (genericParameterType instanceof ParameterizedType) {
                        Type[] genericTypes = ((ParameterizedType) genericParameterType).getActualTypeArguments();
                        if (genericTypes.length != 1) {
                            throw new RuntimeException("集合，泛型类型是两个？");
                        }

                        JSONArray jsonArray = parentJSONObject.optJSONArray(paramName);

                        if (jsonArray == null) {
                            Logger.i(TAG, "第" + i + "个参数[" + name + "]JSON中不是一个List～");
                            continue;
                        }

                        List listData = JsonUtil.toList(jsonArray, (Class) genericTypes[0]);
                        Collection<Object> result;
                        if (parameterClass == Set.class) {
                            result = new HashSet<>();
                        } else if (parameterClass == List.class) {
                            result = new ArrayList<>();
                        } else if (parameterClass == Queue.class || parameterClass == Deque.class) {
                            result = new LinkedList<>();
                        } else if (parameterClass.equals(ArrayList.class) || parameterClass.equals(LinkedList.class)
                                || parameterClass.equals(HashSet.class) || parameterClass.equals(TreeSet.class)
                                || parameterClass.equals(Vector.class) || parameterClass.equals(Stack.class)) {
                            //noinspection unchecked
                            result = (Collection<Object>) parameterClass.newInstance();
                        } else {
                            throw new RuntimeException("不支持的集合参数！");
                        }
                        // 放入参数到数组
                        for (Object o : listData) {
                            result.add(o);
                        }
                        argData = result;
                    } else {
                        throw new RuntimeException(name + "参数的集合框架未声明泛型，无法解析！");
                    }
                } else if (Map.class.isAssignableFrom(parameterClass)) {

                    throw new RuntimeException("不支持 Map 类型的数据转换");
                } else if (parameterClass == Integer.class || parameterClass == int.class) {
                    argData = parentJSONObject.optInt(paramName, 0);
                } else if (parameterClass == Character.class || parameterClass == char.class) {
                    argData = (char) parentJSONObject.optInt(paramName, 0);
                } else if (parameterClass == Double.class || parameterClass == double.class) {
                    argData = parentJSONObject.optDouble(paramName, 0);
                } else if (parameterClass == Float.class || parameterClass == float.class) {
                    argData = (float) parentJSONObject.optDouble(paramName, 0);
                } else if (parameterClass == Byte.class || parameterClass == byte.class) {
                    argData = (byte) parentJSONObject.optInt(paramName, 0);
                } else if (parameterClass == Long.class || parameterClass == long.class) {
                    argData = parentJSONObject.optLong(paramName, 0);
                } else if (parameterClass == Short.class || parameterClass == short.class) {
                    argData = (short) parentJSONObject.optInt(paramName, 0);
                } else if (parameterClass == Boolean.class || parameterClass == boolean.class) {
                    argData = parentJSONObject.optBoolean(paramName, false);
                } else if (parameterClass == String.class) {
                    argData = parentJSONObject.optString(paramName, null);
                } else {
                    JSONObject jsonParamObj = parentJSONObject.optJSONObject(paramName);
                    if (jsonParamObj != null) {
                        argData = JsonUtil.toObject(jsonParamObj, parameterClass);
                    } else {
                        argData = null;
                    }
                }
                data[i] = argData;
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        method.invoke(AutoResultListener.this, data);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据paths，获取最后一个json
     *
     * @param rootJsonObject 服务器返回的json
     * @param paths          路径
     * @return 获取到的json数据 or null
     * @throws JSONException json类型不匹配
     */
    @Nullable
    private JSONObject getParentJSONObject(JSONObject rootJsonObject, String[] paths) throws JSONException {
        JSONObject jsonObject = rootJsonObject;
        int index = 0;
        while (index < paths.length - 1) {
            jsonObject = jsonObject.optJSONObject(paths[index]);
            if (jsonObject == null) {
//                throw new JSONException("no value for " + paths[index] + "  paths = " + Arrays.toString(paths));
                return null;
            }
            index++;
        }
        return jsonObject;
    }

    /**
     * 获取参数注解的名称
     *
     * @param annotations 参数的注解
     * @return name or null
     */
    @Nullable
    private String getParameterName(Annotation[] annotations) {
        String result = null;
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == JsonPath.class) {
                JsonPath jsonPath = (JsonPath) annotation;
                result = jsonPath.value();
                break;
            }
        }
        return result;
    }
}
