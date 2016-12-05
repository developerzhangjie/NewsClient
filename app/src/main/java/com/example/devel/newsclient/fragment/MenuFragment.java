package com.example.devel.newsclient.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.devel.newsclient.R;
import com.example.devel.newsclient.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Created by devel on 11/24/2016.
 */

public class MenuFragment extends Fragment implements AdapterView.OnItemClickListener {

    private Context mContext;
    private ListView mListView;
    private ArrayAdapter<String> adapter;
    private Fragment fragment;

    //初始化
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    //相当于setContentView
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_menuframent, null);
        mListView = (ListView) view.findViewById(R.id.listView);
        return view;
    }

    //初始化数据
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //适配器
        adapter = new ArrayAdapter<>(mContext, android.R.layout.test_list_item, android.R.id.text1, initData());
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
    }

    private List<String> initData() {
        List<String> mData = new ArrayList<>();
        mData.add("新闻");
        mData.add("娱乐");
        mData.add("体育");
        return mData;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                fragment = new Fragment1();
                break;
            case 1:
                fragment = new Fragment2();
                break;
            case 2:
                fragment = new Fragment3();
                break;
        }
        switchFragment(fragment);
    }

    private void switchFragment(Fragment fragment) {
        if (getActivity() instanceof MainActivity){
            ((MainActivity)getActivity()).selectFragment(fragment);
        }
    }
}
