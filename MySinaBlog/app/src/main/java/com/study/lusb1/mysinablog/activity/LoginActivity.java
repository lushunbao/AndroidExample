package com.study.lusb1.mysinablog.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.study.lusb1.mysinablog.R;
import com.study.lusb1.mysinablog.beans.MyUser;
import com.study.lusb1.mysinablog.model.Task;
import com.study.lusb1.mysinablog.service.MainService;


import java.util.ArrayList;
import java.util.HashMap;

public class LoginActivity extends BaseActivity implements IWeiboActivity {

    private static final int AUTH_REQUEST = 1;
    private Button btn_login;
    private Button btn_choose_user;
    private TextView user_name;

    private ListView user_list;
    private TextView user_token;
    private MyAdapter myAdapter;
    private Dialog user_dialog;

    private MyUser currentUser = null;
    private SharedPreferences sharedPreferences = null;

    private final static String USER_PREF_NAME = "user_pref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        init();
        sharedPreferences = getSharedPreferences(USER_PREF_NAME,MODE_PRIVATE);
        checkIfUserExistsInPreferences();
    }

    @Override
    public void init() {
        btn_login = (Button)findViewById(R.id.btn_login);
        user_name = (TextView)findViewById(R.id.user_name);
        btn_choose_user = (Button)findViewById(R.id.btn_choose_user);

        user_dialog = new Dialog(this,R.style.auth_dialog_style);
        View v = View.inflate(this,R.layout.user_list,null);
        user_dialog.setContentView(v);
        user_list = (ListView)v.findViewById(R.id.user_list);
        myAdapter = new MyAdapter(this);
        user_list.setAdapter(myAdapter);
        user_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("lusb1","item clicked");
                user_dialog.dismiss();
                currentUser = myAdapter.getItem(position);
                user_name.setText(myAdapter.getItem(position).getUserName());
            }
        });
    }

    @Override
    public void refresh(Object... params) {
        ArrayList<MyUser> myUsers = (ArrayList<MyUser>) params[0];
        if(myUsers == null || myUsers.size() == 0){
            //查询数据库中没有用户
            Log.d("lusb1","there is no user in database");
            Intent intent = new Intent(LoginActivity.this,AuthActivity.class);
            startActivityForResult(intent,AUTH_REQUEST);
        }
        else{
            //查询数据库中有用户，将第一个用户设置为当前用户，之后需要添加默认用户的逻辑
            Log.d("lusb1","there user in database");
            myAdapter.setUser(myUsers);
            currentUser = myUsers.get(0);
            user_name.setText(currentUser.getUserName());
            myAdapter.notifyDataSetChanged();
        }
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                //检查有没有用户，存在当前用户的情况下，登陆
                if(currentUser != null){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user_name",currentUser.getUserName());
                    editor.putString("token",currentUser.getToken());
                    editor.putString("token_secret",currentUser.getTokenSecret());
                    editor.putString("user_id",currentUser.getUserId());
                    editor.putString("is_default",currentUser.getIsDefault());
                    startLoginToMainBlogPage();
                }
                break;
            case R.id.btn_choose_user:
                Log.d("lusb1","dialog show");
                user_dialog.show();
                break;
        }
    }

    public void startLoginToMainBlogPage() {
        Intent mainPage = new Intent(this,MainActivity.class);
        startActivity(mainPage);
    }

    public void checkIfUserExistsInDatabases(){
        MainService.newTask(LoginActivity.this,new Task(Task.WEIBO_AUTH,null));
    }

    public void checkIfUserExistsInPreferences(){
        if(sharedPreferences.getString("user_name",null) != null){
            currentUser.setUserName(sharedPreferences.getString("user_name",null));
            currentUser.setToken(sharedPreferences.getString("token",null));
            currentUser.setTokenSecret(sharedPreferences.getString("token_secret",null));
            currentUser.setUserId(sharedPreferences.getString("user_id",null));
            currentUser.setIsDefault(sharedPreferences.getString("is_default",null));
            //头像为空
            currentUser.setUserIcon(null);
        }
        else{
            checkIfUserExistsInDatabases();
        }
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
//                        myAdapter.addUserToken(uid);
//                        Log.d("lusb1",uid+" RESULT_OK");
//                        myAdapter.notifyDataSetChanged();
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
                convertView.setTag(holder);
            }
            else{
                holder = (ViewHolder) convertView.getTag();
            }
            holder.user_token.setText(userList.get(position).getUserName());
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
        }
    }
}
