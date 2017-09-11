package com.jianyuyouhun.jmvplib.view.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.jianyuyouhun.jmvplib.utils.Logger;

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
     * @param position  指定位置
     * @return 指定位置的fragment对象
     */
    @Nullable
    public Fragment getFragmentByPosition(int position) {
        String name = "";
        try {
            Class cls = FragmentPagerAdapter.class;
            Method[] methods = cls.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals("makeFragmentName")) {
                    if (!method.isAccessible()) {
                        method.setAccessible(true);
                    }
                    name = (String) method.invoke(cls, viewGroup.getId(), position);
                }
            }
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return manager.findFragmentByTag(name);
    }
}
