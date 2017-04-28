package com.study.lusb1.mysinablog.activity;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.sina.weibo.sdk.api.share.Base;
import com.study.lusb1.mysinablog.service.MainService;
import com.study.lusb1.mysinablog.util.MyLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lusb1 on 2017/4/20.
 */

public class MyApplication extends Application {

    private boolean isDebug = false;
    public static final String TAG = "MySinaBlog.MyApplication";

    private List<Activity> activities;

    @Override
    public void onCreate() {
        super.onCreate();
        activities = new ArrayList<>();
    }

    public void clearAllActivity(){
        stopService(new Intent(getApplicationContext(), MainService.class));
        for(Activity activity : activities){
            MyLog.d(isDebug,TAG,activity.getLocalClassName()+"finish");
            activity.finish();
        }
        activities.clear();
        AppExit();
    }

    public void addActivity(Activity activity){
        if(!activities.contains(activity)){
            activities.add(activity);
        }
    }

    public void removeActivity(Activity activity){
        if(activities.contains(activity)){
            activities.remove(activity);
            activity.finish();
        }
    }

    public void AppExit(){
        try{
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        catch (Exception e){
            e.printStackTrace();
            MyLog.d(isDebug,TAG,"退出异常");
        }
    }
}
