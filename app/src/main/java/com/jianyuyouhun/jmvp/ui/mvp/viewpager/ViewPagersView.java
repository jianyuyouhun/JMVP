package com.jianyuyouhun.jmvp.ui.mvp.viewpager;

import com.jianyuyouhun.jmvplib.mvp.BaseJView;

import java.util.List;

/**
 *
 * Created by wangyu on 2017/9/11.
 */

public interface ViewPagersView extends BaseJView {
    void onGetTitlesSuccess(List<String> titles);
}
