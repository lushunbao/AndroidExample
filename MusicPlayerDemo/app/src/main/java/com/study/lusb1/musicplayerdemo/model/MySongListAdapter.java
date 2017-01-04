package com.study.lusb1.musicplayerdemo.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.study.lusb1.musicplayerdemo.R;

import java.util.List;

/**
 * Created by lusb1 on 2017/1/4.
 */

public class MySongListAdapter extends BaseAdapter {
    private List<Mp3Info> songList;
    private Context context;
    private LayoutInflater inflater;

    public MySongListAdapter(Context context,List<Mp3Info> songList){
        this.context = context;
        this.songList = songList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return songList.size();
    }

    @Override
    public Object getItem(int position) {
        return songList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        TextView song_name;
        TextView artist_name;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.song_item,null);
            song_name = (TextView)convertView.findViewById(R.id.item_name);
            artist_name = (TextView)convertView.findViewById(R.id.artist_name);
            viewHolder.artist_name = artist_name;
            viewHolder.song_name = song_name;
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
            song_name = viewHolder.song_name;
            artist_name = viewHolder.artist_name;
        }
        song_name.setText(songList.get(position).getTitle());
        artist_name.setText(songList.get(position).getArtist());
        return convertView;
    }

    private class ViewHolder{
        private TextView song_name;
        private TextView artist_name;
    }
}
