package com.jianyuyouhun.jmvp.view.hangup;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jianyuyouhun.inject.ViewInjector;
import com.jianyuyouhun.inject.annotation.FindViewById;
import com.jianyuyouhun.inject.annotation.OnClick;
import com.jianyuyouhun.jmvp.R;
import com.jianyuyouhun.jmvp.app.server.notification.NotificationModel;
import com.jianyuyouhun.jmvp.util.WindowHelper;
import com.jianyuyouhun.jmvp.view.SimpleAnimationListener;
import com.jianyuyouhun.jmvplib.utils.CommonUtils;
import com.jianyuyouhun.jmvplib.view.ShadowLayout;

/**
 * 横幅视图控件
 * Created by wangyu on 2017/9/13.
 */

public class HangupTipsView extends LinearLayout implements NotificationModel.NotificationAction<String>, View.OnClickListener {

    private boolean isShown = false;

    private ObjectAnimator objectAnimator = null;

    private int timeSurplus = 10;
    private int TIME_COUNT = 5;

    @FindViewById(R.id.shadow_layout)
    private ShadowLayout mShadowLayout;

    @FindViewById(R.id.tips_all_layout)
    private LinearLayout mAllLayout;

    @FindViewById(R.id.tips_content_text)
    private TextView mContentText;

    private WindowManager.LayoutParams layoutParams;

    private Handler handler = new Handler(Looper.getMainLooper());

    public HangupTipsView(Context context) {
        this(context, null);
    }

    public HangupTipsView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HangupTipsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = inflate(context, R.layout.view_hangup_tips_layout, this);
        ViewInjector.inject(this, view);
        setVisibility(INVISIBLE);//隐藏用INVISIBLE，GONE的话调用show的时候无法计算出高度
        initLayoutParams();
        registerListener();
    }

    private void initLayoutParams() {
        int type;
        if (CommonUtils.getDeviceSdk() <= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
        } else {
            type = WindowManager.LayoutParams.TYPE_TOAST;
        }
        layoutParams = new WindowManager
                .LayoutParams(type);
        //默认坐标居中，所以需要手动设置坐标
        layoutParams.x = 0;
        layoutParams.y = 0;

        layoutParams.gravity = Gravity.CENTER | Gravity.TOP;
        layoutParams.format = PixelFormat.RGBA_8888;//背景透明
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //初始化后不首先获得窗口焦点。不妨碍设备上其他部件的点击、触摸事件。
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        try {
            WindowHelper.getWindowManager().addView(this, layoutParams);
            WindowHelper.getWindowManager().removeView(this);
        } catch (Exception e) {
            //当弹出横幅时遇上系统级弹窗（比如动态权限处理时的弹窗）时横幅会被强制关闭，
            //此时再调用addView会出现view has been added的BadTokenException。而此时view实际上并没有在Window里面，调用remove也会崩溃
            //目前还未能找到原因，所以加上try catch
            e.printStackTrace();
        }
    }

    private void registerListener() {
        mAllLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(null);
            }
        });
    }

    @Override
    public boolean isShown() {
        return isShown;
    }

    public void show(@Nullable final OnAnimationExecuteListener listener) {
        if (isShown()) {
            if (listener != null) {
                listener.onFinish();
            }
            return;
        }
        if (objectAnimator == null) {
            WindowHelper.getWindowManager().addView(this, layoutParams);
            setVisibility(VISIBLE);
            objectAnimator = ObjectAnimator.ofFloat(this, "translationY", -getMeasuredHeight(), 0);
            objectAnimator.setDuration(500);
            objectAnimator.setInterpolator(new BounceInterpolator());
            objectAnimator.addListener(new SimpleAnimationListener() {

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    objectAnimator = null;
                    isShown = true;
                    loopCountDown();
                    if (listener != null) {
                        listener.onFinish();
                    }
                }
            });
            objectAnimator.start();
        }
    }

    public void dismiss(@Nullable final OnAnimationExecuteListener listener) {
        if (!isShown) {
            if (listener != null) {
                listener.onFinish();
            }
            return;
        }
        if (objectAnimator == null) {
            objectAnimator = ObjectAnimator.ofFloat(this, "translationY", 0, -getMeasuredHeight());
            objectAnimator.setDuration(500);
            objectAnimator.setInterpolator(new AnticipateInterpolator());
            objectAnimator.addListener(new SimpleAnimationListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    setVisibility(INVISIBLE);
                    objectAnimator = null;
                    isShown = false;
                    timeSurplus = 0;
                    handler.removeCallbacks(countDownTask);
                    WindowHelper.getWindowManager().removeView(HangupTipsView.this);
                    if (listener != null) {
                        listener.onFinish();
                    }
                }
            });
            objectAnimator.start();
        }
    }

    private void loopCountDown() {
        handler.removeCallbacks(countDownTask);
        timeSurplus = TIME_COUNT;
        if (!isShown) {
            show(new OnAnimationExecuteListener() {
                @Override
                public void onFinish() {
                    handler.postDelayed(countDownTask, 1000);
                }
            });
        } else {
            handler.postDelayed(countDownTask, 1000);
        }
    }

    private Runnable countDownTask = new Runnable() {
        @Override
        public void run() {
            timeSurplus--;
            if (timeSurplus <= 0) {
                dismiss(null);
            } else {
                handler.postDelayed(countDownTask, 1000);
            }
        }
    };


    @Override
    public void onNewMsg(String msgInfo, int timeCount) {
        TIME_COUNT = timeCount;
        timeSurplus = TIME_COUNT;
        mContentText.setText(msgInfo);
        mShadowLayout.invalidateShadow();
        show(null);
    }

    @Override
    public void onClick(View v) {

    }
}
