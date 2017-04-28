package com.study.lusb1.mysinablog.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sina.weibo.sdk.constant.WBConstants;
import com.study.lusb1.mysinablog.R;
import com.study.lusb1.mysinablog.activity.MainActivity;
import com.study.lusb1.mysinablog.activity.MyApplication;
import com.study.lusb1.mysinablog.beans.MyUser;
import com.study.lusb1.mysinablog.util.MyLog;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * Created by lusb1 on 2017/4/10.
 */

public class HomeFragment extends Fragment {

    private boolean isDebug = false;
    public static final String TAG = "MySinaBlog.HomeFragment";

    private ListView user_time_line_list;
    private MyAdapter myAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_frag,null);
        user_time_line_list = (ListView)view.findViewById(R.id.user_time_line_list);
        myAdapter = new MyAdapter(getContext());
        user_time_line_list.setAdapter(myAdapter);
        bindOnDataChangeListener();
        return view;
    }



    public void bindOnDataChangeListener(){
        ((MainActivity)getActivity()).setOnDataChangedListener(new MainActivity.OnDataChangedListener() {
            @Override
            public void onDataChanged(ArrayList<String> msgList) {
                MyLog.d(isDebug,TAG,"msgList:"+msgList);
                myAdapter.setMsgList(msgList);
                myAdapter.notifyDataSetChanged();
            }
        });
    }

    private class MyAdapter extends BaseAdapter{
        private Context context;
        private LayoutInflater layoutInflater;
        private ArrayList<String> msgList = new ArrayList<>();

        private MyAdapter(Context context){
            this.layoutInflater = LayoutInflater.from(context);
        }

        private void setMsgList(ArrayList<String> msgList){
            this.msgList = new ArrayList<>(msgList);
        }

        @Override
        public int getCount() {
            return msgList.size();
        }

        @Override
        public Object getItem(int position) {
            return msgList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if(convertView == null){
                holder = new Holder();
                convertView = layoutInflater.inflate(R.layout.user_time_line_item,null);
                holder.user_time_line_text = (TextView)convertView.findViewById(R.id.user_time_line_text);
                convertView.setTag(holder);
            }
            else{
                holder = (Holder) convertView.getTag();
            }
            holder.user_time_line_text.setText(msgList.get(position));
            return convertView;
        }

        private class Holder{
            private TextView user_time_line_text;
        }
    }
}
