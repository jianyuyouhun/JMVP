package com.jianyuyouhun.jmvp.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.jianyuyouhun.jmvp.ui.mvp.viewpager.ViewPagerFragment;
import com.jianyuyouhun.jmvplib.view.adapter.BaseFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by wangyu on 2017/9/11.
 */

public class TestFragmentPagerAdapter extends BaseFragmentPagerAdapter {

    private List<String> titles = new ArrayList<>();

    public TestFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setTitles(@NonNull List<String> data) {
        titles.clear();
        titles.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return ViewPagerFragment.newInstance(titles.get(position));
    }

    @Override
    public int getCount() {
        return titles.size();
    }
}
