package com.study.lusb1.mysinablog.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.study.lusb1.mysinablog.R;
import com.study.lusb1.mysinablog.fragment.HomeFragment;
import com.study.lusb1.mysinablog.fragment.InfoFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lushunbao on 2017/4/13.
 */

public class MainActivity extends BaseActivity {
    private ViewPager viewPager;
    private TabPageAdapter tabPageAdapter;
    private RadioGroup radioGroup;
    private RadioButton btn_tab_home;
    private RadioButton btn_tab_info;
    private List<Fragment> fragments = new ArrayList<>();
    private FragmentManager fm;

    private boolean isViewPagerScrolling = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_blog_layout);
        init();
    }

    private void init(){
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
                Log.d("lusb1","onPageSelected");
                //页面切换完毕之后调用
                if(isViewPagerScrolling){
                    isViewPagerScrolling = false;
                    Log.d("lusb1","isViewPagerScrolling");
                    Log.d("lusb1","position:"+position);
                    updateRadioGroupState(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d("lusb1","onPageScrollStateChanged");
                Log.d("lusb1","state:"+state);
                if(state == 1){
                    isViewPagerScrolling = true;
                }
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d("lusb1","check changed");
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

    private void updateRadioGroupState(int position) {
        switch(position){
            case 0:
                btn_tab_home.setChecked(true);
                break;
            case 1:
                btn_tab_info.setChecked(true);
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
