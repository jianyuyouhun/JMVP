package com.jianyuyouhun.jmvplib.app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.jianyuyouhun.jmvplib.utils.Logger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * 异常报告页面
 * Created by wangyu on 2017/4/6.
 */

public class ExceptionActivity extends AppCompatActivity {
    private static final String TAG = ExceptionActivity.class.getSimpleName();
    private TextView exceptionView;

    public static void showException(Throwable throwable) {
        JApp applicationContext = JApp.getInstance();
        if (applicationContext != null && BuildConfig.isDebug()) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            throwable.printStackTrace(new PrintStream(byteArrayOutputStream));
            String msg = new String(byteArrayOutputStream.toByteArray());

            try {
                Intent intent = new Intent(applicationContext, ExceptionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("msg", msg);
                applicationContext.startActivity(intent);
            } catch (Exception e) {
                Logger.w(TAG, "异常捕获页面未在manifest中声明");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        exceptionView = new TextView(getApplication());
        setContentView(exceptionView);
        exceptionView.setTextColor(Color.RED);
        handlerIntent(getIntent(), false);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handlerIntent(intent, true);
    }

    private void handlerIntent(Intent intent, boolean isNew) {
        String msg = intent.getStringExtra("msg");
        if (msg != null) {
            if (isNew)
                exceptionView.append("\n\n\n\n\n\n");

            exceptionView.append(msg);
        }
    }

}
