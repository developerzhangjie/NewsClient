package com.example.devel.newsclient.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.devel.newsclient.activity.MainActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * Description:Fragment的父类
 * Created by devel on 11/24/2016.
 */

public abstract class BaseFragment extends Fragment {

    private View view;
    public Context mContext;
    public SlidingMenu mSlidingMenu;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mSlidingMenu = ((MainActivity) getActivity()).getSlidingMenu();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = initView(inflater);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    public View getRootView() {
        return view;
    }

    public abstract View initView(LayoutInflater inflater);

    public abstract void initData();
}
