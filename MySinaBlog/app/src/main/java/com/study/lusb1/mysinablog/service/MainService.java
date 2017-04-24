package com.study.lusb1.mysinablog.service;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.study.lusb1.mysinablog.activity.IWeiboActivity;
import com.study.lusb1.mysinablog.beans.Constants;
import com.study.lusb1.mysinablog.beans.MyUser;
import com.study.lusb1.mysinablog.db.MyDatabaseHelper;
import com.study.lusb1.mysinablog.model.Task;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Exchanger;

public class MainService extends Service implements Runnable {
    //task queue
    private static Queue<Task> tasks = new LinkedList<>();
    //activity list
    private static ArrayList<Activity> activityList = new ArrayList<>();
    //switch of the runnable
    private boolean isRun = false;
    //time of thread sleep
    private static final int SLEEP_TIME = 1000;

    private ArrayList<MyUser> myUsers = new ArrayList<>();

    private UserService userService = null;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case Task.WEIBO_AUTH:
                    IWeiboActivity loginActivityFromAuth = (IWeiboActivity)getActivityByName("LoginActivity");
                    loginActivityFromAuth.refresh(msg.obj);
                    break;
                case Task.READ_USER_INFO:
                    IWeiboActivity loginActivityAfterSave = (IWeiboActivity)getActivityByName("LoginActivity");
                    loginActivityAfterSave.refresh(msg.obj);
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
    public void onDestroy() {
        isRun = false;
        super.onDestroy();
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
        if(userService == null){
            userService = new UserService(new MyDatabaseHelper(MainService.this));
        }
        switch(task.getTaskId()){
            case Task.WEIBO_AUTH:
                Log.d("lusb1","check if there is any user");
                myUsers = userService.getAllUsers();
                msg.obj = myUsers;
                break;
            case Task.READ_USER_INFO:
                Log.d("lusb1","read user info");
                String accessToken = (String)task.getTaskParams().get("accessToken");
                String uid = (String)task.getTaskParams().get("uid");
                //parse the user info from json
                MyUser userInfo = readUserInfo(accessToken,uid);
                //try to save the user info to database
                if(userService != null && userService.getUserByUserId(uid) == null && userInfo != null){
                    myUsers.add(userInfo);
                    userService.insertUser(userInfo);
                }
                msg.obj = myUsers;
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

    private MyUser readUserInfo(String accessToken,String uid){
        MyUser myUser = null;
        String result = "";
        try {
            URL url = new URL(Constants.USER_INFO_URL+"?access_token="+accessToken+"&uid="+uid);
            Log.d("lusb1",url.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.connect();
            Log.d("lusb1",conn.getResponseCode()+"");
            if(conn.getResponseCode() == 200){
                InputStream is = conn.getInputStream();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1020];
                int len;
                while((len = is.read(buffer)) != -1){
                    byteArrayOutputStream.write(buffer,0,len);
                }
                byte[] res = byteArrayOutputStream.toByteArray();
                is.close();
                byteArrayOutputStream.close();
                result = new String(res);

                //json解析
                JSONObject jsonObject = new JSONObject(result);
                //用户名
                String userName = jsonObject.getString("screen_name");
                //获取头像
                String user_head = jsonObject.getString("avatar_large");
                URL head_url = new URL(user_head);
                Log.d("lusb1","user head url:"+user_head);
                HttpURLConnection head_connection = (HttpURLConnection)head_url.openConnection();
                Bitmap user_head_image = BitmapFactory.decodeStream(head_connection.getInputStream());
                BitmapDrawable user_head_drawable = new BitmapDrawable(getResources(),user_head_image);
                myUser = new MyUser(uid,userName,accessToken,null,"1",user_head_drawable);
                Log.d("lusb1",userName);
                return myUser;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
