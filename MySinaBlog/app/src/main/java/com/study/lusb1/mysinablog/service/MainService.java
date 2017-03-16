package com.study.lusb1.mysinablog.service;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.study.lusb1.mysinablog.activity.IWeiboActivity;
import com.study.lusb1.mysinablog.beans.User;
import com.study.lusb1.mysinablog.db.MyDatabaseHelper;
import com.study.lusb1.mysinablog.model.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class MainService extends Service implements Runnable {
    //task queue
    private static Queue<Task> tasks = new LinkedList<>();
    //activity list
    private static ArrayList<Activity> activityList = new ArrayList<>();
    //switch of the runnable
    private boolean isRun = false;
    //time of thread sleep
    private static final int SLEEP_TIME = 1000;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case Task.WEIBO_LOGIN:
                    IWeiboActivity activity = (IWeiboActivity)getActivityByName("LoginActivity");
                    activity.refresh(msg.obj);
                    break;
                default:
                    break;
            }
        }
    };
    public MainService() {}

    @Override
    public void onCreate() {
        isRun = true;
        Thread thread = new Thread(this);
        thread.start();
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void run() {
        while(isRun){
            Task task = null;
            if(!tasks.isEmpty()){
                //run the task and then remove it
                task = tasks.poll();
                if(task != null){
                    doTask(task);
                }
            }
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void newTask(Activity activity,Task task){
        activityList.add(activity);
        tasks.add(task);
    }

    private void doTask(Task task){
        Message msg = handler.obtainMessage();
        msg.what = task.getTaskId();
        switch(task.getTaskId()){
            case Task.WEIBO_LOGIN:
                Log.d("lusb1","check if there is any user");
                UserService userService = new UserService(new MyDatabaseHelper(MainService.this));
                ArrayList<User> users = userService.getAllUsers();
                msg.obj = users;
                break;
            default:
                break;
        }
        handler.sendMessage(msg);
    }

    private Activity getActivityByName(String name){
        if(activityList != null){
            for(Activity activity : activityList){
                if(activity.getClass().getName().indexOf(name) > 0){
                    return activity;
                }
            }
        }
        return null;
    }
}
