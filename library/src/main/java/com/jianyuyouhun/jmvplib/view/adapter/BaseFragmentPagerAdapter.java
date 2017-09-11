package com.jianyuyouhun.jmvplib.view.adapter;

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
     * @param position  指定位置
     * @return 指定位置的fragment对象
     */
    protected Fragment getFragmentByPosition(int position) {
        String name = "";
        Method targetMethod = null;
        try {
            targetMethod = FragmentPagerAdapter.class.getMethod("makeFragmentName", Integer.class, Long.class);
            if (targetMethod != null) {
                if (!targetMethod.isAccessible()) {
                    targetMethod.setAccessible(true);
                }
                name = (String) targetMethod.invoke(viewGroup.getId(), position);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Fragment fragment = manager.findFragmentByTag(name);
        return fragment;
    }
}
