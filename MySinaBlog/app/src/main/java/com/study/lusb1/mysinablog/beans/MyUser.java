package com.study.lusb1.mysinablog.beans;

import android.graphics.drawable.Drawable;

import java.sql.Blob;

/**
 * Created by lushunbao on 2017/3/16.
 */

public class MyUser {
    //User表名
    public static final String TB_NAME="tb_user";
    //User表列
    public static final String ID = "_id";
    public static final String USER_ID = "userId";
    public static final String USER_NAME = "userName";
    public static final String TOKEN = "token";
    public static final String TOKEN_SECRET = "tokenSecret";
    public static final String IS_DEFAULT = "isDefault";
    public static final String USER_ICON = "userIcon";
    //建User表sql
    public static final String CREATE_TB_USER = "CREATE TABLE IF NOT EXISTS "+ TB_NAME + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
            USER_ID + " TEXT ," +
            USER_NAME + " TEXT ," +
            TOKEN + " TEXT ," +
            TOKEN_SECRET + " TEXT ," +
            IS_DEFAULT + " TEXT ," +
            USER_ICON + " BLOB )";
    //删除User表sql
    public static final String DROP_TB_USER = "DROP TABLE" + TB_NAME;

    private String userId;
    private String userName;
    private String token;
    private String tokenSecret;
    private String isDefault;
    private Drawable userIcon;

    public MyUser(String userId, String userName, String token, String tokenSecret, String isDefault, Drawable userIcon){
        this.userId = userId;
        this.userName = userName;
        this.token = token;
        this.tokenSecret = tokenSecret;
        this.isDefault = isDefault;
        this.userIcon = userIcon;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public Drawable getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(Drawable userIcon) {
        this.userIcon = userIcon;
    }
}
