package com.study.lusb1.mysinablog.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends AppCompatActivity {
    private MyApplication myApplication;
    private BaseActivity baseActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(myApplication == null){
            myApplication = (MyApplication) getApplication();
        }
        addActivity();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void addActivity(){
        myApplication.addActivity(this);
    }

    public void removeActivity(){
        myApplication.removeActivity(this);
    }

    public void clearAllActivity(){

        myApplication.clearAllActivity();
    }
}
