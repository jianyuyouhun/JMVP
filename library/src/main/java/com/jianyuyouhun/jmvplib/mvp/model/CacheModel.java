package com.jianyuyouhun.jmvplib.mvp.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.jianyuyouhun.jmvplib.app.JApp;
import com.jianyuyouhun.jmvplib.mvp.BaseJModelImpl;

import java.util.Map;

/**
 * 缓存model
 * Created by wangyu on 2017/4/25.
 */

public class CacheModel extends BaseJModelImpl {

    private static final String JAPP_CACHE_NAME = "japp_cache_data";

    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    private String uId;
    private String guestId = "";

    @Override
    public void onModelCreate(JApp app) {
        super.onModelCreate(app);
        sp = app.getSharedPreferences(JAPP_CACHE_NAME, Context.MODE_PRIVATE);
        spEditor = sp.edit();
        spEditor.apply();
    }

    @Override
    public void onAllModelCreate() {
        super.onAllModelCreate();
        setGuestId("guest");
    }

    /**
     * 设置用户id
     * @param userId    userId
     */
    public void setUserId(String userId) {
        this.uId = userId;
    }

    /**
     * 设置访客id
     * @param guestId
     */
    public void setGuestId(@NonNull String guestId) {
        this.guestId = guestId;
    }

    /**
     * 获取实际缓存的key
     * @param key   原key
     * @return      拼接用户id的key
     */
    private String getCurrentKey(String key) {
        if (!TextUtils.isEmpty(uId)) {
            key = key + "_" + uId;
        } else {
            key = key + "_" + guestId;
        }
        return key;
    }

    /**
     * SP中写入String类型value
     *
     * @param key   键
     * @param value 值
     */
    public void putString(String key, String value) {
        key = getCurrentKey(key);
        spEditor.putString(key, value).apply();
    }

    /**
     * SP中读取String
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值{@code null}
     */
    public String getString(String key) {
        return getString(key, null);
    }

    /**
     * SP中读取String
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public String getString(String key, String defaultValue) {
        key = getCurrentKey(key);
        return sp.getString(key, defaultValue);
    }

    /**
     * SP中写入int类型value
     *
     * @param key   键
     * @param value 值
     */
    public void putInt(String key, int value) {
        key = getCurrentKey(key);
        spEditor.putInt(key, value).apply();
    }

    /**
     * SP中读取int
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    public int getInt(String key) {
        return getInt(key, -1);
    }

    /**
     * SP中读取int
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public int getInt(String key, int defaultValue) {
        key = getCurrentKey(key);
        return sp.getInt(key, defaultValue);
    }

    /**
     * SP中写入long类型value
     *
     * @param key   键
     * @param value 值
     */
    public void putLong(String key, long value) {
        key = getCurrentKey(key);
        spEditor.putLong(key, value).apply();
    }

    /**
     * SP中读取long
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    public long getLong(String key) {
        return getLong(key, -1L);
    }

    /**
     * SP中读取long
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public long getLong(String key, long defaultValue) {
        key = getCurrentKey(key);
        return sp.getLong(key, defaultValue);
    }

    /**
     * SP中写入float类型value
     *
     * @param key   键
     * @param value 值
     */
    public void putFloat(String key, float value) {
        key = getCurrentKey(key);
        spEditor.putFloat(key, value).apply();
    }

    /**
     * SP中读取float
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    public float getFloat(String key) {
        return getFloat(key, -1f);
    }

    /**
     * SP中读取float
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public float getFloat(String key, float defaultValue) {
        key = getCurrentKey(key);
        return sp.getFloat(key, defaultValue);
    }

    /**
     * SP中写入boolean类型value
     *
     * @param key   键
     * @param value 值
     */
    public void putBoolean(String key, boolean value) {
        key = getCurrentKey(key);
        spEditor.putBoolean(key, value).apply();
    }

    /**
     * SP中读取boolean
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值{@code false}
     */
    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    /**
     * SP中读取boolean
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        key = getCurrentKey(key);
        return sp.getBoolean(key, defaultValue);
    }

    /**
     * SP中获取所有键值对
     *
     * @return Map对象
     */
    public Map<String, ?> getAll() {
        return sp.getAll();
    }

    /**
     * SP中移除该key
     *
     * @param key 键
     */
    public void remove(String key) {
        key = getCurrentKey(key);
        spEditor.remove(key).apply();
    }

    /**
     * SP中是否存在该key
     *
     * @param key 键
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    public boolean contains(String key) {
        key = getCurrentKey(key);
        return sp.contains(key);
    }

    /**
     * SP中清除所有数据
     */
    public void clear() {
        spEditor.clear().apply();
    }
}
