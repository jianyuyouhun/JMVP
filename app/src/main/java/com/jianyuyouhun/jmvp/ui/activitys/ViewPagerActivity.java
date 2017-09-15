package com.jianyuyouhun.jmvp.ui.activitys;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.jianyuyouhun.inject.annotation.FindViewById;
import com.jianyuyouhun.jmvp.R;
import com.jianyuyouhun.jmvp.app.App;
import com.jianyuyouhun.jmvp.ui.mvp.viewpager.ViewPagerFragment;
import com.jianyuyouhun.jmvp.ui.mvp.viewpager.ViewPagersModel;
import com.jianyuyouhun.jmvp.ui.mvp.viewpager.ViewPagersPresenter;
import com.jianyuyouhun.jmvp.ui.mvp.viewpager.ViewPagersView;
import com.jianyuyouhun.jmvplib.app.BaseMVPActivity;
import com.jianyuyouhun.jmvp.ui.adapter.TestFragmentPagerAdapter;

import java.util.List;

/**
 * ViewPager测试页面
 * Created by wangyu on 2017/9/11.
 */

public class ViewPagerActivity extends BaseMVPActivity<ViewPagersPresenter, ViewPagersModel> implements ViewPagersView {

    @FindViewById(R.id.view_pager)
    private ViewPager mViewPager;

    private TestFragmentPagerAdapter adapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_view_pagers;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerListener();
        showProgressDialog();
        mPresenter.onStart();
    }

    private void registerListener() {
        adapter = new TestFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Fragment fragment = adapter.getFragmentByPosition(position);
                if (fragment != null) {
                    ViewPagerFragment targetFragment = (ViewPagerFragment) fragment;
                    showToast(targetFragment.getTitleString());
                }
            }
        });
    }

    @Override
    public void showError(String error) {
        dismissProgressDialog();
        showToast(error);
    }

    @NonNull
    @Override
    protected ViewPagersModel initModel() {
        ViewPagersModel model = new ViewPagersModel();
        model.onModelCreate(App.getApp());
        return model;
    }

    @Override
    protected void bindModelAndView(ViewPagersModel mModel, ViewPagersPresenter mPresenter) {
        mPresenter.onBindModelView(mModel, this);
    }

    @Override
    public void onGetTitlesSuccess(List<String> titles) {
        dismissProgressDialog();
        adapter.setTitles(titles);
    }
}
