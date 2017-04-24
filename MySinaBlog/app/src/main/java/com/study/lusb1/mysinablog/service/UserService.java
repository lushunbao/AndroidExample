package com.study.lusb1.mysinablog.service;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.study.lusb1.mysinablog.beans.MyUser;
import com.study.lusb1.mysinablog.db.MyDatabaseHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.util.ArrayList;

/**
 * Created by lushunbao on 2017/3/16.
 */

public class UserService {
    private MyDatabaseHelper myDatabaseHelper;
    private SQLiteDatabase db;
    private byte[] icon_res;

    public UserService(MyDatabaseHelper myDatabaseHelper){
        this.myDatabaseHelper = myDatabaseHelper;
    }

    //add myUser
    public void insertUser(MyUser myUser){
        if(getUserByUserId(myUser.getUserId()) == null){
            db = myDatabaseHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues(5);
            contentValues.put(MyUser.USER_ID, myUser.getUserId());
            contentValues.put(MyUser.USER_NAME, myUser.getUserName());
            contentValues.put(MyUser.TOKEN, myUser.getToken());
            contentValues.put(MyUser.TOKEN_SECRET, myUser.getTokenSecret());
            contentValues.put(MyUser.IS_DEFAULT, myUser.getIsDefault());
            contentValues.put(MyUser.USER_ICON, getResFromDrawable(myUser.getUserIcon()));
            db.insert(MyUser.TB_NAME,null,contentValues);
            db.close();
        }
    }

    //get user
    public MyUser getUserByUserId(String id){
        db = myDatabaseHelper.getWritableDatabase();
        MyUser myUser = null;
        Cursor cursor = db.query(MyUser.TB_NAME,new String[]{MyUser.USER_ID, MyUser.USER_NAME, MyUser.TOKEN, MyUser.TOKEN_SECRET, MyUser.IS_DEFAULT, MyUser.USER_ICON},
                MyUser.USER_ID + "=?",new String[]{id},null,null,null);
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            String userId = cursor.getString(cursor.getColumnIndex(MyUser.USER_ID));
            String userName = cursor.getString(cursor.getColumnIndex(MyUser.USER_NAME));
            String token = cursor.getString(cursor.getColumnIndex(MyUser.TOKEN));
            String tokenSecret = cursor.getString(cursor.getColumnIndex(MyUser.TOKEN_SECRET));
            String isDefault = cursor.getString(cursor.getColumnIndex(MyUser.IS_DEFAULT));
            Drawable user_icon = getDrawableFromRes(cursor.getBlob(cursor.getColumnIndex(MyUser.USER_ICON)));
            myUser = new MyUser(userId,userName,token,tokenSecret,isDefault,user_icon);
        }
        db.close();
        return myUser;
    }

    //get all users
    public ArrayList<MyUser> getAllUsers(){
        db = myDatabaseHelper.getWritableDatabase();
        ArrayList<MyUser> myUsers = new ArrayList<>();
        Cursor cursor = db.query(MyUser.TB_NAME,new String[]{MyUser.USER_ID, MyUser.USER_NAME, MyUser.TOKEN, MyUser.TOKEN_SECRET, MyUser.IS_DEFAULT, MyUser.USER_ICON},
                null,null,null,null,null);
        if(cursor != null && cursor.getCount() > 0){
            while(cursor.moveToNext()){
                String userId = cursor.getString(cursor.getColumnIndex(MyUser.USER_ID));
                String userName = cursor.getString(cursor.getColumnIndex(MyUser.USER_NAME));
                String token = cursor.getString(cursor.getColumnIndex(MyUser.TOKEN));
                String tokenSecret = cursor.getString(cursor.getColumnIndex(MyUser.TOKEN_SECRET));
                String isDefault = cursor.getString(cursor.getColumnIndex(MyUser.IS_DEFAULT));
                Drawable user_icon = getDrawableFromRes(cursor.getBlob(cursor.getColumnIndex(MyUser.USER_ICON)));
                MyUser myUser = new MyUser(userId,userName,token,tokenSecret,isDefault,user_icon);
                myUsers.add(myUser);
            }
        }

        db.close();
        return myUsers;
    }

    //decode bitmap to byte array while insert
    public byte[] getResFromDrawable(Drawable user_icon){
        if(user_icon == null) return null;
        BitmapDrawable bitmapDrawable = (BitmapDrawable) user_icon;
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        icon_res = byteArrayOutputStream.toByteArray();
        String write_in = new String(icon_res);
        Log.d("lusb1","icon save to database:"+write_in);
        return icon_res;
    }

    //decode byte array to bitmap while getUser
    public Drawable getDrawableFromRes(byte[] res){
        if(res == null) return null;
        icon_res = res;
        BitmapDrawable bitmapDrawable = null;
//        try{
//            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) res.getBinaryStream();
//            icon_res = new byte[byteArrayInputStream.available()];
//            byteArrayInputStream.read(icon_res,0,icon_res.length);
//            String read_out = new String(icon_res);
//            Log.d("lusb1","icon save to database:"+read_out);
//            Bitmap bitmap = BitmapFactory.decodeStream(byteArrayInputStream);
//            bitmapDrawable = new BitmapDrawable(bitmap);
//        }
//        catch(Exception e){
//            e.printStackTrace();
//            Log.d("lusb1","error while read icon from database");
//        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(icon_res);
        Bitmap bitmap = BitmapFactory.decodeStream(byteArrayInputStream);
        bitmapDrawable = new BitmapDrawable(bitmap);
        String read_out = new String(icon_res);
        Log.d("lusb1","icon read from database:"+read_out);
        return bitmapDrawable;
    }
}
