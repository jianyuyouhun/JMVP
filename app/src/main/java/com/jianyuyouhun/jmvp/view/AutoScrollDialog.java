package com.jianyuyouhun.jmvp.view;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.jianyuyouhun.jmvp.R;
import com.jianyuyouhun.jmvp.mvp.common.WindowHelperModel;
import com.jianyuyouhun.jmvplib.utils.injecter.model.Model;
import com.jianyuyouhun.jmvplib.utils.injecter.model.ModelInjector;
import com.jianyuyouhun.jmvplib.utils.injecter.view.FindViewById;
import com.jianyuyouhun.jmvplib.utils.injecter.view.ViewInjector;

/**
 *
 * Created by wangyu on 2017/8/17.
 */

public class AutoScrollDialog extends Dialog {

    @FindViewById(R.id.all_layout)
    private RelativeLayout mAllLayout;

    @FindViewById(R.id.scroll_view)
    private ScrollView mScrollView;

    @FindViewById(R.id.dialog_background_view)
    private View mBackgroundView;

    @FindViewById(R.id.dialog_title)
    private TextView mTitle;

    @FindViewById(R.id.dialog_input_text)
    private EditText mInputText;

    @FindViewById(R.id.dialog_cancel)
    private TextView mButtonCancel;

    @FindViewById(R.id.dialog_ok)
    private TextView mButtonOk;

    @Model
    private WindowHelperModel mWindowHelperModel;

    private WindowHelperModel.IKeyBoardVisibleListener iKeyBoardVisibleListener = new WindowHelperModel.IKeyBoardVisibleListener() {
        @Override
        public void onSoftKeyBoardVisible(boolean visible, int windowBottom, int visibleHeight) {
            ViewGroup.LayoutParams lp = mAllLayout.getLayoutParams();
            if (visible) {
                lp.height = visibleHeight;
                mAllLayout.setLayoutParams(lp);
            } else {
                lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
                mAllLayout.setLayoutParams(lp);
            }
        }
    };

    public AutoScrollDialog(@NonNull Activity activity) {
        super(activity, R.style.Translucent);
        ModelInjector.injectModel(this);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        setOwnerActivity(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWindowStyle();
        setContentView(R.layout.dialog_auto_scroll_layout);
        ViewInjector.inject(this);
        registerListener();
    }

    private void registerListener() {
        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), mInputText.getText().toString(), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        mWindowHelperModel.addOnSoftKeyBoardVisibleListener(getOwnerActivity(), iKeyBoardVisibleListener);
    }

    private void setWindowStyle() {
        Window window = getWindow();
        if (window != null) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mWindowHelperModel.removeOnSoftKeyBoardVisibleListener(iKeyBoardVisibleListener);
    }
}
