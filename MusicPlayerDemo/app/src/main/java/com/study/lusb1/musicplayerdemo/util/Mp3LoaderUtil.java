package com.study.lusb1.musicplayerdemo.util;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.study.lusb1.musicplayerdemo.model.Mp3Info;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lusb1 on 2017/1/5.
 */

public class Mp3LoaderUtil {
    //get music list from sd card
    public static List<Mp3Info> getMp3List(Context context){
        List<Mp3Info> musicList = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,null);
        for(int i=0;i<cursor.getCount();i++){
            cursor.moveToNext();
            Mp3Info mp3Info = new Mp3Info();
            long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
            String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
            if(isMusic != 0){
                mp3Info.setId(id);
                mp3Info.setTitle(title);
                mp3Info.setArtist(artist);
                mp3Info.setSize(size);
                mp3Info.setDuration(duration);
                mp3Info.setUrl(url);
                musicList.add(mp3Info);
            }
        }

        return musicList;
    }

    //format time from x(ms) to x(minute):x(second)
    //consider the time won't be more than 100 minute,i will fix it later
    public static String formatTime(long duration){
        String minute = duration/(1000*60) + "";
        String second = (duration%(1000*60))/1000 + "";
        if(minute.length()<2){
            minute = "0" + minute;
        }
        if(second.length()<2){
            second = "0" + second;
        }
        String res = minute + ":" + second;
        return res;
    }

}
