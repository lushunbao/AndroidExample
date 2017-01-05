package com.study.lusb1.musicplayerdemo.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.study.lusb1.musicplayerdemo.model.Mp3Info;
import com.study.lusb1.musicplayerdemo.util.Mp3LoaderUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lusb1 on 2017/1/5.
 */

public class PlayerService extends Service {
    private List<Mp3Info> musicList = new ArrayList<>();
    private MediaPlayer mediaPlayer;

    boolean isPlaying = false;

    @Override
    public void onCreate() {
        super.onCreate();
        musicList = Mp3LoaderUtil.getMp3List(this);
        mediaPlayer = new MediaPlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try{
//            int position = intent.getIntExtra("position",0);
            String url = musicList.get(0).getUrl();
//            Log.d("lusb1",url);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
//            mediaPlayer.start();
//            Log.d("lusb1","onStart");
            String mode = intent.getStringExtra("mode");
            if(mode != null && !mode.equals("")){
                switch(mode){
                    case "play":
                        isPlaying = true;
                        mediaPlayer.start();
                        break;
                    case "pause":
                        isPlaying = false;
                        mediaPlayer.pause();
                        break;
                }
            }
        }
        catch(Exception e){

            e.printStackTrace();
            Log.d("lusb1",e+"");
        }

        return super.onStartCommand(intent, flags, startId);
    }


}
