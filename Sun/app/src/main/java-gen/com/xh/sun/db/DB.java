package com.xh.sun.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.xh.sun.dao.DaoMaster;
import com.xh.sun.dao.DaoSession;

/**
 * 数据库类
 * Created by yyp on 2016/8/17.
 */
public class DB {

    private static DaoSession instance = null;

    /**
     *
     * @param ctx
     * @param DbName  staffId
     * @return
     */
    public static DaoSession getInstance(Context ctx, String DbName) {
        if (null == instance) {
            MyOpenHelper helper = new MyOpenHelper(ctx, DbName + "-db", null);
            SQLiteDatabase db = helper.getWritableDatabase();

            DaoMaster daoMaster = new DaoMaster(db);
            instance = daoMaster.newSession();
        }

        return instance;
    }
}
