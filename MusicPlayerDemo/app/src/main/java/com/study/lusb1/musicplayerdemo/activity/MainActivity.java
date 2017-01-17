package com.study.lusb1.musicplayerdemo.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.study.lusb1.musicplayerdemo.R;
import com.study.lusb1.musicplayerdemo.model.Mp3Info;
import com.study.lusb1.musicplayerdemo.model.MySongListAdapter;
import com.study.lusb1.musicplayerdemo.service.PlayerService;
import com.study.lusb1.musicplayerdemo.util.Mp3LoaderUtil;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //views in this activity
    ListView allSongs;
    ImageButton btn_play_pause;
    ImageButton btn_round_play;
    TextView song_name_text;
    TextView song_duration;

    //flags to indicate states
    boolean isPlaying = false;

    //the intent to trigger the service
    private Intent intent;

    //position now in the play list
    private int currentPosition = 0;

    //music data
    private List<Mp3Info> musicList;
    //music amount
    private int amount = 0;

    //playing state : 0 for playing from the beginning,1 for playing from currentTime
    private int playState = 0;

    //playing mode : play or pause or stop
    private String mode = "play";

    //playing mode : 0 for one song repeat,1 for list repeat
    private int repeatMode = 1;

    //receiver for play position change while repeatMode is repeat all
    private PlayerReceiver playerReceiver = new PlayerReceiver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.lusb1.MainActivityReceiver");
        registerReceiver(playerReceiver,intentFilter);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        allSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isPlaying = true;
                playState = 0;
                mode = "play";
                currentPosition = position;
                changeDetail(currentPosition);
                btn_play_pause.setBackgroundResource(R.drawable.btn_pause);
                generateServiceIntent(playState,mode,currentPosition);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                musicList = Mp3LoaderUtil.getMp3List(this);
                MySongListAdapter adapter = new MySongListAdapter(this,musicList);
                allSongs.setAdapter(adapter);
            }
            else{
                Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void initView(){
        btn_play_pause = (ImageButton)findViewById(R.id.btn_play);
        btn_round_play = (ImageButton)findViewById(R.id.btn_round_play);
        allSongs = (ListView)findViewById(R.id.song_list);
        song_name_text = (TextView)findViewById(R.id.song_name);
        song_duration = (TextView)findViewById(R.id.song_time);
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
        else{
            musicList = Mp3LoaderUtil.getMp3List(this);
            amount = musicList.size();
            MySongListAdapter adapter = new MySongListAdapter(this,musicList);
            allSongs.setAdapter(adapter);
        }
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.btn_play:
                if(isPlaying){
                    isPlaying = false;
                    playState = 1;
                    mode = "pause";
                    btn_play_pause.setBackgroundResource(R.drawable.btn_play);
                    generateServiceIntent(playState,mode,currentPosition);
                }
                else{
                    isPlaying = true;
                    btn_play_pause.setBackgroundResource(R.drawable.btn_pause);
                    changeDetail(currentPosition);
                    mode = "play";
                    generateServiceIntent(playState,mode,currentPosition);
                }
                break;
            case R.id.btn_next:
                if(isPlaying){
                    playState = 0;
                    mode = "play";
                    currentPosition = (currentPosition+1+amount)%amount;
                    changeDetail(currentPosition);
                    generateServiceIntent(playState,mode,currentPosition);
                }
                else{
                    playState = 0;
                    currentPosition = (currentPosition+1+amount)%amount;
                    changeDetail(currentPosition);
                }
                break;
            case R.id.btn_prev:
                if(isPlaying){
                    playState = 0;
                    mode = "play";
                    currentPosition = (currentPosition-1+amount)%amount;
                    changeDetail(currentPosition);
                    generateServiceIntent(playState,mode,currentPosition);
                }
                else{
                    playState = 0;
                    currentPosition = (currentPosition-1+amount)%amount;
                    changeDetail(currentPosition);
                }
                break;
            case R.id.btn_round_play:
                if(repeatMode == 1){
                    repeatMode = 0;
                    btn_round_play.setBackgroundResource(R.drawable.btn_repeat_one);
                }
                else if(repeatMode == 0){
                    repeatMode = 1;
                    btn_round_play.setBackgroundResource(R.drawable.btn_round_play_selector);
                }
                Log.d("lusb1",repeatMode+"");
                Intent intent = new Intent("com.lusb1.MainServiceReceiver");
                intent.putExtra("repeatMode",repeatMode);
                sendBroadcast(intent);
                break;
        }
    }

    //change the detail in the head to show a song's name and it's artist
    public void changeDetail(int currentPosition){
        Mp3Info mp3Info = musicList.get(currentPosition);
        String song_name = mp3Info.getTitle();
        String duration = Mp3LoaderUtil.formatTime(mp3Info.getDuration());
        song_name_text.setText(song_name);
        song_duration.setText(duration);
    }

    public class PlayerReceiver extends BroadcastReceiver{
        public PlayerReceiver() {}

        @Override
        public void onReceive(Context context, Intent intent) {
            int currentPosition = intent.getIntExtra("currentPosition",0);
            changeDetail(currentPosition);
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(playerReceiver);
        super.onDestroy();
    }

    //generate the intent to start service,carry important information
    private void generateServiceIntent(int playState,String mode,int currentPosition){
        intent = new Intent(MainActivity.this,PlayerService.class);
        intent.putExtra("playState",playState);
        intent.putExtra("mode",mode);
        intent.putExtra("position",currentPosition);
        startService(intent);
    }
}
