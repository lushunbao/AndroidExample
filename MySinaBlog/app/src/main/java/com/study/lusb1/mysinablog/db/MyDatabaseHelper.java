package com.study.lusb1.mysinablog.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.study.lusb1.mysinablog.beans.MyUser;

/**
 * Created by lushunbao on 2017/3/16.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DB_NAME = "myweibo";
    private static final int VERSION = 1;

    public MyDatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MyUser.CREATE_TB_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
