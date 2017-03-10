package com.example.lushunbao.mysqlitetest.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lushunbao on 2017/3/8.
 */

public class MySqliteHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "Customer_DB";
    private static final int DB_VERSION = 3;

    public MySqliteHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE IF NOT EXISTS Customer (_id integer primary key autoincrement,_name varchar(20),_age integer)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTable = "DROP TABLE IF EXISTS Customer";
        db.execSQL(dropTable);
        onCreate(db);
    }
}
