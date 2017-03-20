package com.study.lusb1.mysinablog.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        init();
        checkIfUserExists();
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
                user_name.setText(myAdapter.getItem(position));
            }
        });
    }

    @Override
    public void refresh(Object... params) {
        ArrayList<MyUser> myUsers = (ArrayList<MyUser>) params[0];
        if(myUsers == null || myUsers.size() == 0){
            Intent intent = new Intent(LoginActivity.this,AuthActivity.class);
            startActivityForResult(intent,AUTH_REQUEST);
        }
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
//                Intent intent = new Intent(LoginActivity.this,AuthActivity.class);
                Log.d("lusb1","onClick");
                user_dialog.show();
                break;
        }
    }

    public void checkIfUserExists(){
        MainService.newTask(LoginActivity.this,new Task(Task.WEIBO_AUTH,null));
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
        private ArrayList<String> userTokenList = new ArrayList<>();


        public MyAdapter(Context context){
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return userTokenList.size();
        }

        @Override
        public String getItem(int position) {
            return userTokenList.get(position);
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
            holder.user_token.setText(userTokenList.get(position));
            return convertView;
        }

        public void addUserToken(String userToken){
            userTokenList.add(userToken);
        }

        public class ViewHolder{
            public TextView user_token;
        }
    }
}
