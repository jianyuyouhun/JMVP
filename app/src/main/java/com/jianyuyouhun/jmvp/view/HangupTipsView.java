package com.jianyuyouhun.jmvp.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.jianyuyouhun.jmvp.R;
import com.jianyuyouhun.jmvplib.utils.CommonUtils;

/**
 *
 * Created by wangyu on 2017/9/13.
 */

public class HangupTipsView extends TextView {

    @Override
    public boolean isShown() {
        return isShown;
    }

    private boolean isShown = false;

    private ObjectAnimator objectAnimator = null;

    public HangupTipsView(Context context) {
        this(context, null);
    }

    public HangupTipsView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HangupTipsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(Color.WHITE);
        setTextSize(17);
        setGravity(Gravity.CENTER);
        setVisibility(INVISIBLE);
        int padding = CommonUtils.dipToPx(context, 16);
        setPadding(padding, padding, padding, padding);
    }

    public void show() {
        if (isShown) {
            return;
        }
        setVisibility(VISIBLE);
        if (objectAnimator == null) {
            objectAnimator = ObjectAnimator.ofFloat(this, "translationY", -getMeasuredHeight(), 0);
            objectAnimator.setDuration(300);
            objectAnimator.addListener(new SimpleAnimationListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    objectAnimator = null;
                    isShown = true;
                }
            });
            objectAnimator.start();
        }
    }

    public void dismiss() {
        if (!isShown) {
            return;
        }
        if (objectAnimator == null) {
            objectAnimator = ObjectAnimator.ofFloat(this, "translationY", 0, -getMeasuredHeight());
            objectAnimator.setDuration(300);
            objectAnimator.addListener(new SimpleAnimationListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    setVisibility(GONE);
                    objectAnimator = null;
                    isShown = false;
                }
            });
            objectAnimator.start();
        }
    }
}
