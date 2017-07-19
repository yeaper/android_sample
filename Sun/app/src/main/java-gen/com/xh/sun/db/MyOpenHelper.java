package com.xh.sun.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.xh.sun.dao.DaoMaster;

/**
 *
 */
public class MyOpenHelper extends DaoMaster.OpenHelper {


    public MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        switch (newVersion) {
            case 1000:
            case 1001:
                //MoodDiaryDataDao.createTable(db, true);

        }
    }
}