package com.jianyuyouhun.jmvp.mvp.httpTest;

import com.jianyuyouhun.jmvplib.mvp.BaseJView;

/**
 * http测试view
 * Created by jianyuyouhun on 2017/4/26.
 */

public interface HttpTestView extends BaseJView{
    void showDialog();
    void hideDialog();
    void showHtml(String htmlString);
    String getUrl();
}
