package com.study.lusb1.mysinablog.util;

import android.util.Log;

/**
 * Created by lusb1 on 2017/4/26.
 */

public class MyLog {
    public static void d(boolean debug,String tag,String msg){
        if(debug) Log.d(tag,msg);
    }
}
