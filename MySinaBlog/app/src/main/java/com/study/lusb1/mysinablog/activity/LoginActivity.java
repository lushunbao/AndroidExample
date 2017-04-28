package com.study.lusb1.mysinablog.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.study.lusb1.mysinablog.R;
import com.study.lusb1.mysinablog.beans.MyUser;
import com.study.lusb1.mysinablog.beans.Task;
import com.study.lusb1.mysinablog.service.MainService;
import com.study.lusb1.mysinablog.util.MyLog;
import com.study.lusb1.mysinablog.view.CircleImageView;


import java.util.ArrayList;
import java.util.HashMap;

import static com.study.lusb1.mysinablog.beans.Constants.USER_PREF_NAME;

public class LoginActivity extends BaseActivity implements IWeiboActivity {

    private boolean isDebug = false;
    public static final String TAG = "MySinaBlog.LoginActivity";

    private static final int AUTH_REQUEST = 1;
    private Button btn_login;
    private Button btn_choose_user;
    private Button btn_add_account;
    private TextView user_name;
    private CircleImageView user_head;

    private ListView user_list;
    private TextView user_token;
    private MyAdapter myAdapter;
    private Dialog user_dialog;

    private MyUser currentUser = null;
    private SharedPreferences sharedPreferences = null;
    private ArrayList<MyUser> myUsers = null;

    private final static int SET_CUR_USER = 2;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case SET_CUR_USER:
                    myAdapter.setUser(myUsers);
                    myAdapter.notifyDataSetChanged();
                    setCurrentUser(myUsers,0);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        init();
        sharedPreferences = getSharedPreferences(USER_PREF_NAME,MODE_PRIVATE);
        checkIfUserExistsInDatabases();
    }

    @Override
    public void init() {
        MyLog.d(isDebug,TAG,"login activity init");
        btn_login = (Button)findViewById(R.id.btn_login);
        user_name = (TextView)findViewById(R.id.user_name);
        user_head = (CircleImageView) findViewById(R.id.user_head_img);
        btn_choose_user = (Button)findViewById(R.id.btn_choose_user);
        btn_add_account = (Button)findViewById(R.id.btn_add_acount);
        initDialog();
    }

    @Override
    public void refresh(Object... params) {
        myUsers = new ArrayList<>((ArrayList<MyUser>) params[0]);
        MyLog.d(isDebug,TAG,"refresh myUsers"+myUsers);
        Message msg = mHandler.obtainMessage();
        if(myUsers == null || myUsers.size() == 0){
            //查询数据库中没有用户
            MyLog.d(isDebug,TAG,"there is no user in database");
            Intent intent = new Intent(LoginActivity.this,AuthActivity.class);
            startActivityForResult(intent,AUTH_REQUEST);
        }
        else{
            //查询数据库中有用户，将第一个用户设置为当前用户，之后需要添加默认用户的逻辑
            MyLog.d(isDebug,TAG,"there is user in database");
            msg.what = SET_CUR_USER;
            mHandler.sendMessage(msg);
        }
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                //检查有没有用户，存在当前用户的情况下，登陆
                if(currentUser != null && currentUser.getUserName() != null){
                    MyLog.d(isDebug,TAG,"current user is not null login to the main page");
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("access_token",currentUser.getToken());
                    editor.putString("user_id",currentUser.getUserId());
                    editor.commit();
                    startLoginToMainBlogPage();
                }
                break;
            case R.id.btn_choose_user:
                user_dialog.show();
                reLayoutDialog(user_dialog);
                break;
            case R.id.btn_add_acount:
                //添加新账户
                Intent intent = new Intent(LoginActivity.this,AuthActivity.class);
                startActivityForResult(intent,AUTH_REQUEST);
                break;
        }
    }

    public void startLoginToMainBlogPage() {
        Intent mainPage = new Intent(this,MainActivity.class);
        startActivity(mainPage);
    }

    public void setCurrentUser(ArrayList<MyUser> myUsers,int position){
        MyUser user = myUsers.get(position);
        currentUser = new MyUser();
        currentUser.setUserName(user.getUserName());
        currentUser.setToken(user.getToken());
        currentUser.setTokenSecret(user.getTokenSecret());
        currentUser.setUserId(user.getUserId());
        currentUser.setIsDefault(user.getIsDefault());
        currentUser.setUserIcon(user.getUserIcon());

        myAdapter.setUser(myUsers);
        myAdapter.notifyDataSetChanged();
        user_name.setText(currentUser.getUserName());
        user_head.setImageDrawable(currentUser.getUserIcon());
    }

    public void checkIfUserExistsInDatabases(){
        MainService.newTask(LoginActivity.this,new Task(Task.WEIBO_AUTH,null));
    }

    //检查是不是存在已经登陆的用户，目前还没想好头像的问题如何处理，如果都存文件还不如直接从数据库读取
    //目前未使用此方法
    public void checkIfUserExistsInPreferences(){
        if(sharedPreferences.getString("user_id",null) != null){
            MainService.newTask(LoginActivity.this,new Task(Task.WEIBO_AUTH,null));
        }
        else{
            checkIfUserExistsInDatabases();
        }
    }

    public void initDialog(){
        user_dialog = new Dialog(this,R.style.auth_dialog_style);
        View v = View.inflate(this,R.layout.user_list,null);
        user_dialog.setContentView(v);
        user_list = (ListView)v.findViewById(R.id.user_list);
        myAdapter = new MyAdapter(this);
        user_list.setAdapter(myAdapter);
        user_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyLog.d(isDebug,TAG,"user item clicked");
                user_dialog.dismiss();
                setCurrentUser(myAdapter.getUser(),position);
            }
        });
    }

    public void reLayoutDialog(Dialog dialog){
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = getPixelsFromDp(200);
        dialog.getWindow().setAttributes(params);
    }

    public int getPixelsFromDp(int size){

        DisplayMetrics metrics =new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        return(size * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;

    }

    @Override
    public void onBackPressed() {
        //login界面直接退出
        MyLog.d(isDebug,TAG,"onBackPressed");
        this.clearAllActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case AUTH_REQUEST:
                if(resultCode == RESULT_OK){
                    if(data != null){
                        String uid = data.getStringExtra("uid");
                        String accessToken = data.getStringExtra("accessToken");
                        //读取授权用户相关信息
                        HashMap<String,Object> map = new HashMap<>();
                        map.put("uid",uid);
                        map.put("accessToken",accessToken);
                        MainService.newTask(LoginActivity.this,new Task(Task.READ_USER_INFO,map));
                    }
                }
                break;
        }
    }

    private class MyAdapter extends BaseAdapter{
        private Context context;
        private LayoutInflater layoutInflater;
        private ArrayList<MyUser> userList = new ArrayList<>();


        public MyAdapter(Context context){
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return userList.size();
        }

        @Override
        public MyUser getItem(int position) {
            return userList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                holder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.user_list_item,null);
                holder.user_token = (TextView)convertView.findViewById(R.id.user_token);
                holder.user_head = (ImageView)convertView.findViewById(R.id.user_head_img_item);
                convertView.setTag(holder);
            }
            else{
                holder = (ViewHolder) convertView.getTag();
            }
            holder.user_token.setText(userList.get(position).getUserName());
            holder.user_head.setImageDrawable(userList.get(position).getUserIcon());
            return convertView;
        }

        public void addUser(MyUser myUser){
            userList.add(myUser);
        }

        public void setUser(ArrayList<MyUser> myUsers){
            userList = new ArrayList<>(myUsers);
        }

        public ArrayList<MyUser> getUser(){
            return userList;
        }

        public class ViewHolder{
            public TextView user_token;
            public ImageView user_head;
        }
    }
}
