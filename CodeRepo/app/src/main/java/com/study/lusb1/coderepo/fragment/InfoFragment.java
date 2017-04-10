package com.study.lusb1.coderepo.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.study.lusb1.coderepo.R;

/**
 * Created by lusb1 on 2017/4/10.
 */

public class InfoFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_frag,null);
        return view;
    }
}
