package com.jianyuyouhun.jmvp.ui.mvp.viewpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jianyuyouhun.inject.annotation.FindViewById;
import com.jianyuyouhun.inject.annotation.OnLongClick;
import com.jianyuyouhun.jmvp.R;
import com.jianyuyouhun.jmvplib.app.BaseFragment;

/**
 *
 * Created by wangyu on 2017/9/11.
 */

public class ViewPagerFragment extends BaseFragment {

    private static final String KEY_TITLE = "title";

    private String mTitleString;

    @FindViewById(R.id.fragment_title)
    private TextView mTitle;

    /**
     * use {@link #newInstance(String)} please
     */
    @Deprecated
    public ViewPagerFragment() {}

    public String getTitleString() {
        return mTitleString;
    }

    public static ViewPagerFragment newInstance(String title) {
        ViewPagerFragment viewPagerFragment = new ViewPagerFragment();
        Bundle argument = new Bundle();
        argument.putString(KEY_TITLE, title);
        viewPagerFragment.setArguments(argument);
        return viewPagerFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitleString = getArguments().getString(KEY_TITLE, "");
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_view_pager;
    }

    @Override
    protected void onCreateView(View rootView, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        mTitle.setText(mTitleString);
    }

    @OnLongClick(R.id.fragment_title)
    private boolean onTitleClick(View view) {
        getBaseActivity().showToast(mTitleString);
        return true;
    }
}
