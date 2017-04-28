package com.study.lusb1.mysinablog.beans;

/**
 * Created by lushunbao on 2017/3/13.
 */

public class Constants {
    public static final String APP_KEY = "1735888183";
    public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    public static final String SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";

    public final static String USER_PREF_NAME = "user_pref";

    //base url
    public static final String WEIBO_URL_BASE = "https://api.weibo.com/2/";
    //读取用户信息url
    public static final String USER_INFO_URL = "https://api.weibo.com/2/users/show.json";
    //读取用户发表的微博url
    public static final String USER_TIMELINE = "https://api.weibo.com/2/statuses/user_timeline.json";
}
