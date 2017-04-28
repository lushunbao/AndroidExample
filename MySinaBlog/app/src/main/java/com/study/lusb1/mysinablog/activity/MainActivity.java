package com.study.lusb1.mysinablog.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.study.lusb1.mysinablog.R;
import com.study.lusb1.mysinablog.fragment.HomeFragment;
import com.study.lusb1.mysinablog.fragment.InfoFragment;
import com.study.lusb1.mysinablog.beans.Task;
import com.study.lusb1.mysinablog.service.MainService;
import com.study.lusb1.mysinablog.util.MyLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.study.lusb1.mysinablog.beans.Constants.USER_PREF_NAME;

/**
 * Created by lushunbao on 2017/4/13.
 */

public class MainActivity extends BaseActivity implements IWeiboActivity {

    private boolean isDebug = false;
    public static final String TAG = "MySinaBlog.MainActivity";

    private ViewPager viewPager;
    private TabPageAdapter tabPageAdapter;
    private RadioGroup radioGroup;
    private RadioButton btn_tab_home;
    private RadioButton btn_tab_info;
    private List<Fragment> fragments = new ArrayList<>();
    private FragmentManager fm;
    private ArrayList<String> msgList = null;

    private String accessToken = "";
    private String userId = "";
    private SharedPreferences mSharedPreferences;


    private OnDataChangedListener onDataChangedListener;
    //监听数据变化的回调接口，MainActivity负责在合适的时候调用，关联的fragment负责实现调用时发生的逻辑
    public interface OnDataChangedListener{
        void onDataChanged(ArrayList<String> msgList);
    }
    public void setOnDataChangedListener(OnDataChangedListener onDataChangedListener){
        this.onDataChangedListener = onDataChangedListener;
    }

    private boolean isViewPagerScrolling = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_blog_layout);
        init();
        MyLog.d(isDebug,TAG,"onCreate");
        readUserTimeLine();
    }

    @Override
    public void onBackPressed() {
        //main界面直接退出
        MyLog.d(isDebug,TAG,"onBackPressed");
        new AlertDialog.Builder(this).setTitle("确认退出吗？")
                .setPositiveButton("确定",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.clearAllActivity();
                    }
                })
                .setNegativeButton("取消",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    @Override
    public void init(){
        fm = getSupportFragmentManager();
        viewPager = (ViewPager)findViewById(R.id.frag_page);
        radioGroup = (RadioGroup)findViewById(R.id.tab_group);
        btn_tab_home = (RadioButton)findViewById(R.id.tab_home);
        btn_tab_info = (RadioButton)findViewById(R.id.tab_info);
        HomeFragment homeFragment = new HomeFragment();
        InfoFragment infoFragment = new InfoFragment();
        fragments.add(homeFragment);
        fragments.add(infoFragment);
        tabPageAdapter = new TabPageAdapter(fragments,fm);
        viewPager.setAdapter(tabPageAdapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                MyLog.d(isDebug,TAG,"onPageSelected");
                //页面切换完毕之后调用
                if(isViewPagerScrolling){
                    isViewPagerScrolling = false;
                    MyLog.d(isDebug,TAG,"isViewPagerScrolling");
                    MyLog.d(isDebug,TAG,"position:"+position);
                    updateRadioGroupState(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                MyLog.d(isDebug,TAG,"onPageScrollStateChanged");
                MyLog.d(isDebug,TAG,"state:"+state);
                if(state == 1){
                    isViewPagerScrolling = true;
                }
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                MyLog.d(isDebug,TAG,"check changed");
                switch (checkedId){
                    case R.id.tab_home:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.tab_info:
                        viewPager.setCurrentItem(1);
                        break;
                }
            }
        });
    }

    @Override
    public void refresh(Object... params) {
        msgList = new ArrayList<>((ArrayList<String>)params[0]);
        if(msgList != null && msgList.size() != 0){
            MyLog.d(isDebug,TAG,"msgList:"+msgList);
            onDataChangedListener.onDataChanged(msgList);
        }
    }

    public void readUserTimeLine(){
        //读取保存在SharedPreferences中的用户信息
        mSharedPreferences = getSharedPreferences(USER_PREF_NAME,MODE_PRIVATE);
        userId = mSharedPreferences.getString("user_id",null);
        accessToken = mSharedPreferences.getString("access_token",null);
        //用户信息不为空时去建立网络连接，获取信息
        if(userId != null && accessToken != null){
            HashMap<String,Object> taskParams = new HashMap<>();
            taskParams.put("uid",userId);
            taskParams.put("accessToken",accessToken);
            MainService.newTask(this,new Task(Task.READ_USER_TIMELINE,taskParams));
        }
    }

    private void updateRadioGroupState(int position) {
        switch(position){
            case 0:
                btn_tab_home.toggle();
                break;
            case 1:
                btn_tab_info.toggle();
                break;
        }
    }



    private class TabPageAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList;

        private TabPageAdapter(List<Fragment> fragments,FragmentManager fm){
            super(fm);
            this.fragmentList = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
}
