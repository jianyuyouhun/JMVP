package com.jianyuyouhun.jmvp.app.server.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jianyuyouhun.jmvp.app.App;

/**
 * 数据库,一个用户一个, 该类负责数据库升级, 建表的工作
 * Created by wangyu on 17/6/26.
 */
public class DataBaseSQLiteOpenHelper extends SQLiteOpenHelper {
    public static final int version = 1;

    DataBaseSQLiteOpenHelper(int uid) {
        super(App.getApp(), "japp_db_" + uid, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createUserTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void createUserTable(SQLiteDatabase db) {
        db.execSQL("create table user(" +
                " uid text," +
                " user_name text," +
                " user_age int[3]," +
                " PRIMARY KEY(uid))");
    }

}
