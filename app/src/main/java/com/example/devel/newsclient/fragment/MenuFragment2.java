package com.example.devel.newsclient.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.devel.newsclient.R;
import com.example.devel.newsclient.activity.MainActivity;
import com.example.devel.newsclient.adapter.MyBaseAdapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Description:屏幕左边的菜单栏
 * Created by devel on 11/26/2016.
 */

public class MenuFragment2 extends BaseFragment {


    public static final int NEWSCENTER = 1;
    public static final int SERVICE = 2;
    public static final int AFFAIRS = 3;
    @ViewInject(R.id.lv_menu_smart_service)
    private ListView lv_menu_smart_service;
    @ViewInject(R.id.lv_menu_govaffairs)
    private ListView lv_menu_govaffairs;
    @ViewInject(R.id.lv_menu_news_center)
    private ListView lv_menu_news_center;
    private MenuAdapter mMenuAdapter;
    private MenuAdapter serviceAdapter;
    private MenuAdapter govaffairsAdapter;

    @Override
    public View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.layout_left_menu, null);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        //设置点击事件，来改变条目的颜色
        lv_menu_news_center.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /**
                 * i 就是点击条目的位置
                 * 思路就是 当点击的条目与getView()中选中的条目相同时，改变颜色，否则，不改变
                 * 如何比较两个位置呢？
                 * 当点击时，把点击的position通过adapter传递到getView()中，然后进行比较，
                 * 改变颜色。点击事件中要做的就是获取点击的position，传递到getView()中，
                 * 需要定义一个函数来实现这个功能
                 */
                mMenuAdapter.setOnClickPosition(i);
                //mSlidingMenu 来自父类
                mSlidingMenu.toggle();
                /**
                 *点击条目时，都是在一个确定的Page下进行的操作，比如都是在新闻中心，都在智慧服务，
                 * 所以需要跳转到所在的Page,所在的Page需要用帧布局，每加载一个界面，就需要把以前的界面移除，
                 * 保证只有一个界面。
                 * 如何进行界面选择呢？
                 * 在具体的Page中定义一个方法：switchFragment(),在这个方法中来进行具体的操作。
                 * 如何跳转到相应的Page呢？
                 * 当然是new对象啊，不过这样消耗内存，因为之前在HomePage.class中new过每个Page,所以
                 * 在HmePage中定义一个获取相应的Page的方法即可。
                 * 那如何获取HomePage.class呢？
                 * 当然是new对象啊，还是耗内存，因为之前在MainActivity.class中new过HomePage对象，所以
                 * 只需要在MainActivity中定义一个getHomePage()方法来获取HomePage对象即可。
                 * 如何获取MainActivity呢？
                 * 通过mContext来强转。
                 * */
                ((MainActivity) mContext).getHomeFragment().getNewsCenterPage().switchFragment(i);
            }
        });

        //服务点击事件
        lv_menu_smart_service.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                serviceAdapter.setOnClickPosition(position);
                mSlidingMenu.toggle();
            }
        });
        //指南点击事件
        lv_menu_govaffairs.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                govaffairsAdapter.setOnClickPosition(position);
                mSlidingMenu.toggle();
            }
        });
    }

    /**
     * 初始化菜单
     */
    private List<String> mMenuNewsLists = new ArrayList<>();


    public void initNewsCenterMenu(Set<String> title) {
        mMenuNewsLists.clear();
        mMenuNewsLists.addAll(title);
        //不用每次都new MenuAdapter,省内存
        if (mMenuAdapter == null) {
            mMenuAdapter = new MenuAdapter(mMenuNewsLists, mContext);
            lv_menu_news_center.setAdapter(mMenuAdapter);
        } else {
            mMenuAdapter.notifyDataSetChanged();
        }
    }


    private class MenuAdapter extends MyBaseAdapter<String> {
        private int mClickPosition;

        MenuAdapter(List<String> mMenuLists, Context context) {
            super(mMenuLists, context);
        }

        void setOnClickPosition(int i) {
            mClickPosition = i;
            notifyDataSetChanged();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = View.inflate(getActivity(), R.layout.layout_item_menu, null);
            }
            ImageView iv_menu_item = (ImageView) view.findViewById(R.id.iv_menu_item);
            TextView tv_menu_item = (TextView) view.findViewById(R.id.tv_menu_item);
            /**
             * mData来自父类，它是封装了来自新闻中心，智慧服务，政务指南的数据的集合。
             * 在创建Adapter时，需要将mData当做参数传入构造函数，这样就能根据不同界面
             * 获取相应界面的数据，进行展示。
             */
            String title = mData.get(i);
            //position  就是选中条目的位置
            if (mClickPosition == i) {
                // 红色
                iv_menu_item.setBackgroundResource(R.drawable.menu_arr_select);
                tv_menu_item.setTextColor(Color.RED);
                // 设置背景颜色
                view.setBackgroundResource(R.drawable.menu_item_bg_select);
            } else {
                // 白色
                iv_menu_item.setBackgroundResource(R.drawable.menu_arr_normal);
                tv_menu_item.setTextColor(Color.WHITE);
                // 设置透明色
                view.setBackgroundResource(R.drawable.transparent);
            }
            tv_menu_item.setText(title);
            return view;
        }
    }

    /*
     设置菜单的类型 包括：1新闻中心，2智慧服务，3政务指南
   */
    public void setMenuType(int type) {
        // 选择不同的菜单类型
        switchMenuType(type);
    }

    private void switchMenuType(int mMenuType) {
        // 新闻中心隐藏
        lv_menu_news_center.setVisibility(View.GONE);
        // 服务隐藏
        lv_menu_smart_service.setVisibility(View.GONE);
        // 政务隐藏
        lv_menu_govaffairs.setVisibility(View.GONE);
        switch (mMenuType) {
            case 1:
                //新闻
                lv_menu_news_center.setVisibility(View.VISIBLE);
                if (mMenuAdapter == null) {
                    mMenuAdapter = new MenuAdapter(mMenuNewsLists, mContext);
                    lv_menu_news_center.setAdapter(mMenuAdapter);
                } else {
                    mMenuAdapter.notifyDataSetChanged();
                }
                break;
            case 2:
                // 服务
                lv_menu_smart_service.setVisibility(View.VISIBLE);
                List<String> arrayList = new ArrayList<>();
                arrayList.add("教育");
                if (serviceAdapter == null) {
                    serviceAdapter = new MenuAdapter(arrayList, mContext);
                    lv_menu_smart_service.setAdapter(serviceAdapter);
                } else {
                    serviceAdapter.notifyDataSetChanged();
                }
                break;
            case 3:
                // 政务
                lv_menu_govaffairs.setVisibility(View.VISIBLE);
                List<String> arrayList2 = new ArrayList<>();
                arrayList2.add("交通管理");
                arrayList2.add("黑马程序员");
                if (govaffairsAdapter == null) {
                    govaffairsAdapter = new MenuAdapter(arrayList2, mContext);
                    lv_menu_govaffairs.setAdapter(govaffairsAdapter);
                } else {
                    govaffairsAdapter.notifyDataSetChanged();
                }
                break;
        }
    }
}
