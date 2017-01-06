package com.study.lusb1.musicplayerdemo.activity;

import android.Manifest;
import android.content.Intent;
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
    Button btn_play_pause;
    TextView song_name_text;
    TextView artist_text;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                currentPosition = position;
                changeDetail(currentPosition);
                btn_play_pause.setBackgroundResource(R.drawable.btn_pause);
                intent = new Intent(MainActivity.this,PlayerService.class);
                intent.putExtra("playState",playState);
                intent.putExtra("mode","play");
                intent.putExtra("position",currentPosition);
                startService(intent);
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
        btn_play_pause = (Button)findViewById(R.id.btn_play);
        allSongs = (ListView)findViewById(R.id.song_list);
        song_name_text = (TextView)findViewById(R.id.song_name);
        artist_text = (TextView)findViewById(R.id.song_time);
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
                    btn_play_pause.setBackgroundResource(R.drawable.btn_play);
                    intent = new Intent(MainActivity.this,PlayerService.class);
                    intent.putExtra("playState",playState);
                    intent.putExtra("mode","pause");
                    intent.putExtra("position",currentPosition);
                    startService(intent);
                }
                else{
                    isPlaying = true;
                    btn_play_pause.setBackgroundResource(R.drawable.btn_pause);
                    changeDetail(currentPosition);
                    intent = new Intent(MainActivity.this,PlayerService.class);
                    intent.putExtra("playState",playState);
                    intent.putExtra("mode","play");
                    intent.putExtra("position",currentPosition);
                    startService(intent);
                }
                break;
            case R.id.btn_next:
                if(isPlaying){
                    playState = 0;
                    currentPosition = (currentPosition+1+amount)%amount;
                    changeDetail(currentPosition);
                    intent = new Intent(MainActivity.this,PlayerService.class);
                    intent.putExtra("playState",playState);
                    intent.putExtra("mode","play");
                    intent.putExtra("position",currentPosition);
                    startService(intent);
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
                    currentPosition = (currentPosition-1+amount)%amount;
                    changeDetail(currentPosition);
                    intent = new Intent(MainActivity.this,PlayerService.class);
                    intent.putExtra("playState",playState);
                    intent.putExtra("mode","play");
                    intent.putExtra("position",currentPosition);
                    startService(intent);
                }
                else{
                    playState = 0;
                    currentPosition = (currentPosition-1+amount)%amount;
                    changeDetail(currentPosition);
                }
                break;
        }
    }

    //change the detail in the head to show a song's name and it's artist
    public void changeDetail(int currentPosition){
        Mp3Info mp3Info = musicList.get(currentPosition);
        String song_name = mp3Info.getTitle();
        String artist_name = mp3Info.getArtist();
        song_name_text.setText(song_name);
        artist_text.setText(artist_name);
    }
}
