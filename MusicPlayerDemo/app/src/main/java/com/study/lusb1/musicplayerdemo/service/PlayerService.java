package com.study.lusb1.musicplayerdemo.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaDataSource;
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

    private boolean isPlaying = false;
    private int currentTime = 0;
    private String url;
    private String mode;
    private int currentPosition = 0;
    private int playState = 0;

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
            currentPosition = intent.getIntExtra("position",0);
            url = musicList.get(currentPosition).getUrl();
            mode = intent.getStringExtra("mode");
            playState = intent.getIntExtra("playState",0);
            if(mode != null && !mode.equals("")){
                switch(mode){
                    case "play":
                        isPlaying = true;
                        play(currentTime);
                        break;
                    case "pause":
                        isPlaying = false;
                        pause();
                        break;
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return super.onStartCommand(intent, flags, startId);
    }


    public void play(int currentTime){
        if(playState == 0){
            currentTime = 0;
        }
        try{
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MyPreparedListener(currentTime));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void pause(){
        if(mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            currentTime = mediaPlayer.getCurrentPosition();
        }
    }

    private final class MyPreparedListener implements MediaPlayer.OnPreparedListener{
        private int currentTime;

        public MyPreparedListener(int currentTime){
            this.currentTime = currentTime;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            mediaPlayer.start();
            if(currentTime > 0){
                mediaPlayer.seekTo(currentTime);
            }
        }
    }

}
