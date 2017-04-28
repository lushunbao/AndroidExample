package com.study.lusb1.alarmmanagertest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

/**
 * Created by lusb1 on 2017/4/24.
 */

public class MyReceiver extends BroadcastReceiver {
    private static final String WAKE_UP_ACTION = "com.study.lusb1.alarmmanagertest.wakeup";

    private static final int DOZE_WAKE_LOCK = 0x00000040;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals("WAKE_UP_ACTION")){
            Log.d("lusb1","receive msg");
            PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wl = pm.newWakeLock(DOZE_WAKE_LOCK,"wakeup");
            wl.acquire();
            wl.release();
        }
    }
}
