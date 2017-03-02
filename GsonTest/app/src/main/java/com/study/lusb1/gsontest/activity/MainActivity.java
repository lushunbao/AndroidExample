package com.study.lusb1.gsontest.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.study.lusb1.gsontest.R;
import com.study.lusb1.gsontest.beans.Person;
import com.study.lusb1.gsontest.util.GsonUtil;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int READ_FINISH = 1;

    private TextView jsonInfo;

    private String personInfo = "";

    private Person singlePerson;

    private List<Person> personList = new ArrayList<>();

    private MyHandler myHandler = new MyHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        new MyReadThread().start();
    }

    public void initView(){
        jsonInfo = (TextView)findViewById(R.id.jsonInfo);
    }

    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case READ_FINISH:
                    jsonInfo.setText(personList.toString());
                    break;
            }
        }
    }

    public class MyReadThread extends Thread{
        @Override
        public void run() {
            try{
                //read simple object
//                InputStreamReader reader = new InputStreamReader(getResources().openRawResource(R.raw.simpleobject));
//                singlePerson = GsonUtil.buildClassFromJson(reader,Person.class);


                InputStreamReader reader = new InputStreamReader(getResources().openRawResource(R.raw.simplelist));
                personList = GsonUtil.buildListFromJson(reader,Person.class);
                Log.d("lusb1",personList.toString());
                myHandler.sendEmptyMessage(1);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
