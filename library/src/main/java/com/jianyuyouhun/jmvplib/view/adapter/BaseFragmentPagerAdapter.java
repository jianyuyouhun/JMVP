package com.jianyuyouhun.jmvplib.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.jianyuyouhun.jmvplib.app.BaseFragment;

/**
 * baseFragment的viewPagerAdapter
 * Created by wangyu on 2017/6/9.
 */

public abstract class BaseFragmentPagerAdapter extends FragmentPagerAdapter {

    private FragmentManager manager;

    public BaseFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        this.manager = fm;
    }

    /**
     * 获取当前fragment实例
     *
     * @param container ViewPager对象
     * @param position  指定位置
     * @return 指定位置的fragment对象
     */
    protected BaseFragment getCurrentFragment(ViewGroup container, int position) {
        String name = makeFragmentName(container.getId(), position);
        Fragment fragment = manager.findFragmentByTag(name);
        return (BaseFragment) fragment;
    }

    /**
     * 从fragmentPagerAdapter中复用过来
     *
     * @return fragmentName
     */
    private String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }
}
