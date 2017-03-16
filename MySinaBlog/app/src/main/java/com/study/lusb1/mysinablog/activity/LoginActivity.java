package com.study.lusb1.mysinablog.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.study.lusb1.mysinablog.R;
import com.study.lusb1.mysinablog.beans.User;
import com.study.lusb1.mysinablog.model.Task;
import com.study.lusb1.mysinablog.service.MainService;

import java.util.ArrayList;

public class LoginActivity extends BaseActivity implements IWeiboActivity,View.OnClickListener {

    private Button btn_login;
    private Button btn_choose_user;
    private TextView user_name;

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
    }

    @Override
    public void refresh(Object... params) {
        ArrayList<User> users = (ArrayList<User>) params[0];
        if(users == null || users.size() == 0){
            Intent intent = new Intent(LoginActivity.this,AuthActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
//                Intent intent = new Intent(LoginActivity.this,AuthActivity.class);

                break;
        }
    }

    public void checkIfUserExists(){
        MainService.newTask(LoginActivity.this,new Task(Task.WEIBO_LOGIN,null));
    }
}
