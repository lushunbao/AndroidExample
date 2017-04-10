package com.study.lusb1.coderepo.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.study.lusb1.coderepo.R;
import com.study.lusb1.coderepo.fragment.HomeFragment;
import com.study.lusb1.coderepo.fragment.InfoFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabPageAdapter tabPageAdapter;
    private RadioGroup radioGroup;
    private List<Fragment> fragments = new ArrayList<>();
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        fm = getSupportFragmentManager();
        viewPager = (ViewPager)findViewById(R.id.frag_page);
        radioGroup = (RadioGroup)findViewById(R.id.tab_group);
        HomeFragment homeFragment = new HomeFragment();
        InfoFragment infoFragment = new InfoFragment();
        fragments.add(homeFragment);
        fragments.add(infoFragment);
        tabPageAdapter = new TabPageAdapter(fragments,fm);
        viewPager.setAdapter(tabPageAdapter);
        viewPager.setCurrentItem(0);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.tab_home:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.tab_info:
                        viewPager.setCurrentItem(1);
                }
            }
        });
    }

    private class TabPageAdapter extends FragmentPagerAdapter{
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
