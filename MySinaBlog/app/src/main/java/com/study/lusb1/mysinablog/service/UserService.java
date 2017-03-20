package com.study.lusb1.mysinablog.service;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.study.lusb1.mysinablog.beans.MyUser;
import com.study.lusb1.mysinablog.db.MyDatabaseHelper;

import java.util.ArrayList;

/**
 * Created by lushunbao on 2017/3/16.
 */

public class UserService {
    private MyDatabaseHelper myDatabaseHelper;
    private SQLiteDatabase db;

    public UserService(MyDatabaseHelper myDatabaseHelper){
        this.myDatabaseHelper = myDatabaseHelper;
    }

    //add myUser
    public void insertUser(MyUser myUser){
        db = myDatabaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues(5);
        contentValues.put(MyUser.USER_ID, myUser.getUserId());
        contentValues.put(com.study.lusb1.mysinablog.beans.MyUser.USER_NAME, myUser.getUserName());
        contentValues.put(com.study.lusb1.mysinablog.beans.MyUser.TOKEN, myUser.getToken());
        contentValues.put(com.study.lusb1.mysinablog.beans.MyUser.TOKEN_SECRET, myUser.getTokenSecret());
        contentValues.put(com.study.lusb1.mysinablog.beans.MyUser.IS_DEFAULT, myUser.getIsDefault());
        db.insert(com.study.lusb1.mysinablog.beans.MyUser.TB_NAME,null,contentValues);
        db.close();
    }

    //get user
    public MyUser getUserByUserId(String id){
        db = myDatabaseHelper.getWritableDatabase();
        MyUser myUser = null;
        Cursor cursor = db.query(com.study.lusb1.mysinablog.beans.MyUser.TB_NAME,new String[]{com.study.lusb1.mysinablog.beans.MyUser.USER_ID, com.study.lusb1.mysinablog.beans.MyUser.USER_NAME, com.study.lusb1.mysinablog.beans.MyUser.TOKEN, com.study.lusb1.mysinablog.beans.MyUser.TOKEN_SECRET, com.study.lusb1.mysinablog.beans.MyUser.IS_DEFAULT},
                com.study.lusb1.mysinablog.beans.MyUser.USER_ID + "=?",new String[]{id},null,null,null);
        if(cursor != null){
            String userId = cursor.getString(cursor.getColumnIndex(com.study.lusb1.mysinablog.beans.MyUser.USER_ID));
            String userName = cursor.getString(cursor.getColumnIndex(com.study.lusb1.mysinablog.beans.MyUser.USER_NAME));
            String token = cursor.getString(cursor.getColumnIndex(com.study.lusb1.mysinablog.beans.MyUser.TOKEN));
            String tokenSecret = cursor.getString(cursor.getColumnIndex(com.study.lusb1.mysinablog.beans.MyUser.TOKEN_SECRET));
            String isDefault = cursor.getString(cursor.getColumnIndex(com.study.lusb1.mysinablog.beans.MyUser.IS_DEFAULT));
            myUser = new MyUser(userId,userName,token,tokenSecret,isDefault,null);
        }
        db.close();
        return myUser;
    }

    //get all users
    public ArrayList<MyUser> getAllUsers(){
        db = myDatabaseHelper.getWritableDatabase();
        ArrayList<MyUser> myUsers = new ArrayList<>();
        Cursor cursor = db.query(com.study.lusb1.mysinablog.beans.MyUser.TB_NAME,new String[]{com.study.lusb1.mysinablog.beans.MyUser.USER_ID, com.study.lusb1.mysinablog.beans.MyUser.USER_NAME, com.study.lusb1.mysinablog.beans.MyUser.TOKEN, com.study.lusb1.mysinablog.beans.MyUser.TOKEN_SECRET, com.study.lusb1.mysinablog.beans.MyUser.IS_DEFAULT},
                null,null,null,null,null);
        while(cursor.moveToNext()){
            String userId = cursor.getString(cursor.getColumnIndex(com.study.lusb1.mysinablog.beans.MyUser.USER_ID));
            String userName = cursor.getString(cursor.getColumnIndex(com.study.lusb1.mysinablog.beans.MyUser.USER_NAME));
            String token = cursor.getString(cursor.getColumnIndex(com.study.lusb1.mysinablog.beans.MyUser.TOKEN));
            String tokenSecret = cursor.getString(cursor.getColumnIndex(com.study.lusb1.mysinablog.beans.MyUser.TOKEN_SECRET));
            String isDefault = cursor.getString(cursor.getColumnIndex(com.study.lusb1.mysinablog.beans.MyUser.IS_DEFAULT));
            MyUser myUser = new MyUser(userId,userName,token,tokenSecret,isDefault,null);
            myUsers.add(myUser);
        }
        db.close();
        return myUsers;
    }
}
