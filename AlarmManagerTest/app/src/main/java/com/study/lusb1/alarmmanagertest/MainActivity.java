package com.study.lusb1.alarmmanagertest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private static final String WAKE_UP_ACTION = "com.study.lusb1.alarmmanagertest.wakeup";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置定时任务，发送广播
//        Intent intent = new Intent();
//        intent.setAction(WAKE_UP_ACTION);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);
//
//
//        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//        am.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+5000,pendingIntent);

//        PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
//        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK,"wakeup");
//        wl.acquire();
//        wl.release();

        Intent doze_action = new Intent();
        doze_action.setAction("com.android.systemui.doze.notification_pulse");
        sendBroadcast(doze_action);
    }
}
