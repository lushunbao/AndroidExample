package com.study.lusb1.mysinablog.service;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.study.lusb1.mysinablog.beans.User;
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

    //add user
    public void insertUser(User user){
        db = myDatabaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues(5);
        contentValues.put(User.USER_ID,user.getUserId());
        contentValues.put(User.USER_NAME,user.getUserName());
        contentValues.put(User.TOKEN,user.getToken());
        contentValues.put(User.TOKEN_SECRET,user.getTokenSecret());
        contentValues.put(User.IS_DEFAULT,user.getIsDefault());
        db.insert(User.TB_NAME,null,contentValues);
        db.close();
    }

    //get user
    public User getUserByUserId(String id){
        db = myDatabaseHelper.getWritableDatabase();
        User user = null;
        Cursor cursor = db.query(User.TB_NAME,new String[]{User.USER_ID,User.USER_NAME,User.TOKEN,User.TOKEN_SECRET,User.IS_DEFAULT},
                User.USER_ID + "=?",new String[]{id},null,null,null);
        if(cursor != null){
            String userId = cursor.getString(cursor.getColumnIndex(User.USER_ID));
            String userName = cursor.getString(cursor.getColumnIndex(User.USER_NAME));
            String token = cursor.getString(cursor.getColumnIndex(User.TOKEN));
            String tokenSecret = cursor.getString(cursor.getColumnIndex(User.TOKEN_SECRET));
            String isDefault = cursor.getString(cursor.getColumnIndex(User.IS_DEFAULT));
            user = new User(userId,userName,token,tokenSecret,isDefault,null);
        }
        db.close();
        return user;
    }

    //get all users
    public ArrayList<User> getAllUsers(){
        db = myDatabaseHelper.getWritableDatabase();
        ArrayList<User> users = new ArrayList<>();
        Cursor cursor = db.query(User.TB_NAME,new String[]{User.USER_ID,User.USER_NAME,User.TOKEN,User.TOKEN_SECRET,User.IS_DEFAULT},
                null,null,null,null,null);
        while(cursor.moveToNext()){
            String userId = cursor.getString(cursor.getColumnIndex(User.USER_ID));
            String userName = cursor.getString(cursor.getColumnIndex(User.USER_NAME));
            String token = cursor.getString(cursor.getColumnIndex(User.TOKEN));
            String tokenSecret = cursor.getString(cursor.getColumnIndex(User.TOKEN_SECRET));
            String isDefault = cursor.getString(cursor.getColumnIndex(User.IS_DEFAULT));
            User user = new User(userId,userName,token,tokenSecret,isDefault,null);
            users.add(user);
        }
        db.close();
        return users;
    }
}
