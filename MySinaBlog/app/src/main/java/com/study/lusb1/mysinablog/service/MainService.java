package com.study.lusb1.mysinablog.service;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.study.lusb1.mysinablog.activity.IWeiboActivity;
import com.study.lusb1.mysinablog.beans.Constants;
import com.study.lusb1.mysinablog.beans.MyUser;
import com.study.lusb1.mysinablog.db.MyDatabaseHelper;
import com.study.lusb1.mysinablog.beans.Task;
import com.study.lusb1.mysinablog.retrofit.RetrofitFactory;
import com.study.lusb1.mysinablog.util.MyLog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class MainService extends Service implements Runnable {

    private boolean isDebug = true;
    public static final String TAG = "MySinaBlog.MainService";

    private String accessToken = "";
    private String userId = "";

    //task queue
    private static Queue<Task> tasks = new LinkedList<>();
    //activity list
    private static ArrayList<Activity> activityList = new ArrayList<>();
    //switch of the runnable
    private boolean isRun = false;
    //time of thread sleep
    private static final int SLEEP_TIME = 1000;

    private RetrofitFactory retrofitFactory;

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
                case Task.READ_USER_TIMELINE:
                    IWeiboActivity mainActivityReadUserTimeline = (IWeiboActivity)getActivityByName("MainActivity");
                    mainActivityReadUserTimeline.refresh(msg.obj);
                    break;
                default:
                    break;
            }
        }
    };
    public MainService() {}

    @Override
    public void onCreate() {
        retrofitFactory = RetrofitFactory.getInstance(this);
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

    //执行进来的task
    private void doTask(Task task){
        Message msg = handler.obtainMessage();
        msg.what = task.getTaskId();
        if(userService == null){
            userService = new UserService(new MyDatabaseHelper(MainService.this));
        }
        switch(task.getTaskId()){
            case Task.WEIBO_AUTH:
                MyLog.d(isDebug,TAG,"check if there is any user");
                myUsers = userService.getAllUsers();
                msg.obj = myUsers;
                break;
            case Task.READ_USER_INFO:
                MyLog.d(isDebug,TAG,"read user info");
                accessToken = (String)task.getTaskParams().get("accessToken");
                userId = (String)task.getTaskParams().get("uid");
                //parse the user info from json
                MyUser userInfo = readUserInfo(accessToken,userId);
                //try to save the user info to database
                if(userService != null && userService.getUserByUserId(userId) == null && userInfo != null){
                    myUsers.add(userInfo);
                    userService.insertUser(userInfo);
                }
                msg.obj = myUsers;
                break;
            case Task.READ_USER_TIMELINE:
                MyLog.d(isDebug,TAG,"read user time line");
                accessToken = (String)task.getTaskParams().get("accessToken");
                userId = (String)task.getTaskParams().get("uid");
                ArrayList<String> msgList = readUserTimeLine(accessToken,userId);
                MyLog.d(isDebug,TAG,"msgList:"+msgList);
                msg.obj = msgList;
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

    //获取用户信息
    private MyUser readUserInfo(String accessToken,String uid){
        MyUser myUser = null;
        String result = "";
//        try {
//            URL url = new URL(Constants.USER_INFO_URL+"?access_token="+accessToken+"&uid="+uid);
//            MyLog.d(isDebug,TAG,url.toString());
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//            conn.setConnectTimeout(5000);
//            conn.setReadTimeout(5000);
//            conn.connect();
//            MyLog.d(isDebug,TAG,conn.getResponseCode()+"");
//            if(conn.getResponseCode() == 200){
//                result = buildStringFromHttpInputStream(conn.getInputStream());
//                //json解析
//                JSONObject jsonObject = new JSONObject(result);
//                //用户名
//                String userName = jsonObject.getString("screen_name");
//                //获取头像
//                String user_head = jsonObject.getString("avatar_large");
//                URL head_url = new URL(user_head);
//                MyLog.d(isDebug,TAG,"user head url:"+user_head);
//                HttpURLConnection head_connection = (HttpURLConnection)head_url.openConnection();
//                Bitmap user_head_image = BitmapFactory.decodeStream(head_connection.getInputStream());
//                BitmapDrawable user_head_drawable = new BitmapDrawable(getResources(),user_head_image);
//                myUser = new MyUser(uid,userName,accessToken,null,"1",user_head_drawable);
//                MyLog.d(isDebug,TAG,userName);
//                return myUser;
//            }
//        } catch (Exception e) {
//            MyLog.d(isDebug,TAG,"读取用户信息失败");
//            e.printStackTrace();
//        }

        myUser = retrofitFactory.readUserInfo(accessToken,uid);

        return myUser;
    }

    //获取用户自己的timeline
    private ArrayList<String> readUserTimeLine(String accessToken,String uid) {
        ArrayList<String> msgList = new ArrayList<>();
        String readFromHttp = "";

//        try{
//            URL user_time_line = new URL(Constants.USER_TIMELINE+"?access_token="+accessToken+"&uid="+uid);
//            MyLog.d(isDebug,TAG,user_time_line.toString());
//
//            HttpURLConnection conn = (HttpURLConnection) user_time_line.openConnection();
//            conn.setRequestMethod("GET");
//            conn.setConnectTimeout(5000);
//            conn.setReadTimeout(5000);
//            conn.connect();
//
//            MyLog.d(isDebug,TAG,conn.getResponseCode()+"");
//            if(conn.getResponseCode() == 200){
//                readFromHttp = buildStringFromHttpInputStream(conn.getInputStream());
//                MyLog.d(isDebug,TAG,"readFromHttp"+readFromHttp);
//
//                JSONObject statuses = new JSONObject(readFromHttp);
//                JSONArray statusArray = statuses.getJSONArray("statuses");
//                for(int i=0;i<statusArray.length();i++){
//                    JSONObject status = statusArray.getJSONObject(i);
//                    String msg = status.getString("text");
//                    MyLog.d(isDebug,TAG,msg);
//                    msgList.add(msg);
//                }
//            }
//        }
//        catch(Exception e){
//            e.printStackTrace();
//            MyLog.d(isDebug,TAG,"读取用户timeline失败");
//        }
        msgList = retrofitFactory.readUserTimeline(accessToken,uid);
        return msgList;
    }

    private String buildStringFromHttpInputStream(InputStream is){
        String res = "";

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        try{
            while((len = is.read(buffer)) != -1){
                os.write(buffer,0,len);
            }
            res = new String(os.toByteArray());
        }
        catch(Exception e){
            MyLog.d(isDebug,TAG,"decode inputstream failed");
            e.printStackTrace();
        }
        finally {
            try{
                is.close();
                os.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        return res;
    }
}
