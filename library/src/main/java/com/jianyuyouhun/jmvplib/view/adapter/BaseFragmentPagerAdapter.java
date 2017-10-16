package com.jianyuyouhun.jmvplib.view.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * baseFragment的viewPagerAdapter
 * Created by wangyu on 2017/6/9.
 */

public abstract class BaseFragmentPagerAdapter extends FragmentPagerAdapter {

    private FragmentManager manager;
    private ViewGroup viewGroup;

    public BaseFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        this.manager = fm;
    }

    @Override
    public void startUpdate(ViewGroup container) {
        super.startUpdate(container);
        this.viewGroup = container;
    }

    /**
     * 获取当前fragment实例
     *
     * @param position  指定位置，一般用于获取当前位置的fragment，其他的fragment可能已经被系统回收
     * @return 指定位置的fragment对象
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public Fragment getFragmentByPosition(int position) {
        String name = "";
        try {
            Class cls = FragmentPagerAdapter.class;
            Method method = cls.getDeclaredMethod("makeFragmentName", int.class, long.class);
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            name = (String) method.invoke(cls, viewGroup.getId(), position);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Fragment fragment = manager.findFragmentByTag(name);
        if (fragment == null) {
            return null;
        }
        return fragment;
    }
}
