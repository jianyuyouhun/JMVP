package com.jianyuyouhun.jmvp.mvp.db;

import android.database.sqlite.SQLiteDatabase;

import com.jianyuyouhun.jmvplib.app.JApp;
import com.jianyuyouhun.jmvplib.app.broadcast.LightBroadcast;
import com.jianyuyouhun.jmvplib.mvp.BaseJModelImpl;
import com.jianyuyouhun.jmvplib.utils.http.executor.SingleThreadPool;

/**
 * 数据库操作model
 * Created by wangyu on 2017/6/26.
 */

public class DataBaseModel extends BaseJModelImpl {

    private SingleThreadPool threadPool = new SingleThreadPool();
    private DataBaseSQLiteOpenHelper sqLiteOpenHelper;

    public static final int GUEST_UID = 1001;
    @Override
    public void onModelCreate(JApp app) {
        super.onModelCreate(app);
        initDataBase();
    }

    private void initDataBase() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (sqLiteOpenHelper != null) {
                    sqLiteOpenHelper.close();
                }
                sqLiteOpenHelper = new DataBaseSQLiteOpenHelper(GUEST_UID);
                sqLiteOpenHelper.getWritableDatabase();
            }
        };
        threadPool.execute(runnable);
    }

    /**
     * 提交任务到数据库执行
     *
     * @param dbTask 数据库任务
     * @param <Data> 数据类型
     */
    public <Data> void submitDBTask(final DBTask<Data> dbTask) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase writableDatabase = sqLiteOpenHelper.getWritableDatabase();
                final Data data = dbTask.runOnDBThread(writableDatabase);
                LightBroadcast.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        dbTask.runOnUIThread(data);
                    }
                });
            }
        };
        threadPool.execute(runnable);
    }

    /**
     * 数据库任务
     *
     * @param <Data> 数据库返回结果
     */
    public interface DBTask<Data> {
        /**
         * 在数据库线程执行的任务
         *
         * @param sqLiteDatabase 数据库
         * @return 数据库执行结果 Data
         */
        Data runOnDBThread(SQLiteDatabase sqLiteDatabase);

        /**
         * 数据库执行完成后,  提交到UI线程执行的任务
         *
         * @param data 数据执行结果, runOnDBThread返回
         */
        void runOnUIThread(Data data);
    }
}
