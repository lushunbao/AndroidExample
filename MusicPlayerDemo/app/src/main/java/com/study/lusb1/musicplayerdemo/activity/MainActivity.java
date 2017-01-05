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
import android.widget.Toast;

import com.study.lusb1.musicplayerdemo.R;
import com.study.lusb1.musicplayerdemo.model.Mp3Info;
import com.study.lusb1.musicplayerdemo.model.MySongListAdapter;
import com.study.lusb1.musicplayerdemo.service.PlayerService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //views in this activity
    ListView allSongs;
    Button btn_play_pause;

    //flags to indicate states
    boolean isPlaying = false;

    //the intent to trigger the service
    private Intent intent;
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
                intent = new Intent(MainActivity.this,PlayerService.class);
                Log.d("lusb1",position+"");
                intent.putExtra("position",position);
                startService(intent);
            }
        });
    }

    //read all songs from sd card
    public List<Mp3Info> getMp3Info(){
        List<Mp3Info> songList = new ArrayList<>();
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,null);
        for(int i=0;i<cursor.getCount();i++){
            Mp3Info mp3Info = new Mp3Info();
            cursor.moveToNext();
            //song id
            mp3Info.setId(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
            //song name
            mp3Info.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            //artist name
            mp3Info.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
            //song duration
            mp3Info.setDuration(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
            //song size
            mp3Info.setSize(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)));
            //song url
            mp3Info.setUrl(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
            //is Music
            int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
            //add songs to the list
            if(isMusic != 0){
                songList.add(mp3Info);
            }
        }
        return songList;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                MySongListAdapter adapter = new MySongListAdapter(this,getMp3Info());
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
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
        else{
            MySongListAdapter adapter = new MySongListAdapter(this,getMp3Info());
            allSongs.setAdapter(adapter);
        }
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.btn_play:
                if(isPlaying){
                    isPlaying = false;
                    btn_play_pause.setBackgroundResource(R.drawable.btn_play);
                    intent = new Intent(MainActivity.this,PlayerService.class);
                    intent.putExtra("mode","pause");
                    startService(intent);
                }
                else{
                    isPlaying = true;
                    btn_play_pause.setBackgroundResource(R.drawable.btn_pause);
                    intent = new Intent(MainActivity.this,PlayerService.class);
                    intent.putExtra("mode","play");
                    startService(intent);
                }
        }
    }
}
