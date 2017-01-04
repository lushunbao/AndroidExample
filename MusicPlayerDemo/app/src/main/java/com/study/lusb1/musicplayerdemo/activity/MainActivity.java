package com.study.lusb1.musicplayerdemo.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.study.lusb1.musicplayerdemo.R;
import com.study.lusb1.musicplayerdemo.model.Mp3Info;
import com.study.lusb1.musicplayerdemo.model.MySongListAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView allSongs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        allSongs = (ListView)findViewById(R.id.song_list);
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
        else{
            MySongListAdapter adapter = new MySongListAdapter(this,getMp3Info());
            allSongs.setAdapter(adapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

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
}
