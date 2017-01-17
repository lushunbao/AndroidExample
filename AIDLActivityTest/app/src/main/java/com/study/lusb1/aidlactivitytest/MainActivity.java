
package com.study.lusb1.aidlactivitytest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.study.lusb1.aidlservice.ICalcAIDL;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "client";

    private Button bind_service;
    private Button unbind_service;
    private Button btn_add;
    private Button btn_min;

    private ICalcAIDL mCalcAidl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        bind_service = (Button)findViewById(R.id.bind_service);
        unbind_service = (Button)findViewById(R.id.unbind_service);
        btn_add = (Button)findViewById(R.id.btn_add);
        btn_min = (Button)findViewById(R.id.btn_min);
    }

    private ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG,"serviceConnected");
            mCalcAidl = ICalcAIDL.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAG,"serviceDisConnected");
            mCalcAidl = null;
        }
    };

    private void bindCalcService(){
        Log.d(TAG,"try to bind service");
        Intent intent = new Intent();
//        intent.setAction("com.lusb1.study.calc");
        intent.setPackage("com.study.lusb1.aidlservice");
        bindService(intent,mServiceConn, Context.BIND_AUTO_CREATE);
    }

    public void onClick(View v) throws Exception{
        int id = v.getId();
        switch(id){
            case R.id.bind_service:
                bindCalcService();
                break;
            case R.id.unbind_service:
                unbindService(mServiceConn);
                break;
            case R.id.btn_add:
                if(mCalcAidl != null){
                    Toast.makeText(MainActivity.this,mCalcAidl.add(1,2)+"",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this,"service is not connected correctly",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btn_min:
                if(mCalcAidl != null){
                    Toast.makeText(MainActivity.this,mCalcAidl.min(1,2)+"",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this,"service is not connected correctly",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
