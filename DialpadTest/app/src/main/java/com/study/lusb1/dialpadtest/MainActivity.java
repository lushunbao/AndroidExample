package com.study.lusb1.dialpadtest;

import android.animation.ValueAnimator;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.android.volley.RequestQueue;

public class MainActivity extends AppCompatActivity {

    private final static int MSG_ORI = 1;

    private Button btnToLeft;
    private Button btnToRight;
    private LinearLayout left_space;
    private LinearLayout right_space;
    private LinearLayout.LayoutParams left_params;
    private LinearLayout.LayoutParams right_params;
    private SensorManager sensorManager;
    private Sensor sensor;
    private boolean nowIsTowardsLeft = false;
    private MySensorListener mySensorListener;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case MSG_ORI:
                    break;
            }
        }
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try{
                Log.d("lusb1","into runnable");
                Log.d("lusb1",nowIsTowardsLeft + "\t" + left_params.weight);
                if(nowIsTowardsLeft && left_params.weight == 1){
                    runValueAnim(1,0);
                }
                else if(!nowIsTowardsLeft && left_params.weight == 0){
                    runValueAnim(0,1);
                }
                handler.postDelayed(this,1000);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mySensorListener = new MySensorListener();
        handler.postDelayed(runnable,1000);
    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(mySensorListener);
        super.onPause();
    }

    @Override
    protected void onResume() {
        sensorManager.registerListener(mySensorListener,sensor,SensorManager.SENSOR_DELAY_GAME);
        super.onResume();
    }

    private void initView(){
        btnToLeft = (Button)findViewById(R.id.btn_to_left);
        btnToRight = (Button)findViewById(R.id.btn_to_right);
        left_space = (LinearLayout)findViewById(R.id.left_space);
        right_space = (LinearLayout)findViewById(R.id.right_space);
        left_params = (LinearLayout.LayoutParams)left_space.getLayoutParams();
        right_params = (LinearLayout.LayoutParams)right_space.getLayoutParams();
    }

    public void onClick(View v){
        int id = v.getId();
        switch(id){
            case R.id.btn_to_left:
//                if(!nowIsTowardsLeft){runValueAnim(1,0);}

                break;
            case R.id.btn_to_right:
//                if(nowIsTowardsLeft){runValueAnim(0,1);}
                break;
        }
    }

    private void runValueAnim(int from,int to){
        final ValueAnimator animLeft = ValueAnimator.ofFloat(from,to);
        animLeft.setDuration(300);
        animLeft.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                left_params.weight = (float)animLeft.getAnimatedValue();
                right_params.weight = 1 - left_params.weight;
                left_space.setLayoutParams(left_params);
                right_space.setLayoutParams(right_params);
            }
        });
        animLeft.start();
    }

    private class MySensorListener implements SensorEventListener{

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            adjustDialpadPosition(x,y,z);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }
//true if left
    private void adjustDialpadPosition(float x,float y,float z){
//        Log.d("lusb1",x+"\t"+y+"\t"+z);
        float xy = (float)Math.sqrt(x*x+y*y);
        double angle = Math.atan2(xy,z);
        angle = angle*180/Math.PI;
        if(angle>20 && Math.abs(x)>3){
            if(x>0){
                nowIsTowardsLeft = true;
            }
            else{
                nowIsTowardsLeft = false;
            }
        }
    }
}
