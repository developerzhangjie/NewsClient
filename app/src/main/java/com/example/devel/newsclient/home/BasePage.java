package com.example.devel.newsclient.home;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.devel.newsclient.R;
import com.example.devel.newsclient.activity.MainActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * Description:Paged的父类
 * Created by devel on 11/24/2016.
 */

public abstract class BasePage implements View.OnClickListener {
    public Context mContext;
    public View view;
    public SlidingMenu mSm;
    public TextView txt_title;
    /**
     * 当页面切换时，是否加载数据
     *
     * @param isLoading
     */
    public boolean isLoading = false;
    public ImageButton imgbtn_right;

    public BasePage(Context context) {
        this.mContext = context;
        mSm = ((MainActivity) mContext).getSlidingMenu();
        view = initView();
    }

    public View getRootView() {
        return view;
    }

    /**
     * 初始化标题
     *
     * @param view
     */
    public void initTitleBar(View view) {
        //隐藏左边和右边的按钮
        Button btn_left = (Button) view.findViewById(R.id.btn_left);
        btn_left.setVisibility(View.GONE);
        ImageButton btn_right = (ImageButton) view.findViewById(R.id.btn_right);
        btn_right.setVisibility(View.GONE);
        //给左边的按你设置图片
        ImageButton imgbtn_left = (ImageButton) view.findViewById(R.id.imgbtn_left);
        imgbtn_left.setImageResource(R.drawable.img_menu);
        //设置点击事件
        if (null != imgbtn_left) {
            imgbtn_left.setOnClickListener(this);
        }
        //设置文本标题
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        imgbtn_right = (ImageButton) view.findViewById(R.id.imgbtn_right);
    }

    public abstract View initView();

    public abstract void initData();

    @Override
    public void onClick(View view) {
        mSm.toggle();
    }

    /**
     * 这是个自定义的模拟生命周期的方法，是用来解决新闻条目滑动时出现的bug，
     * 比如在新闻中心，将新闻条目向右滑动几个，然后点击首页，再点击新闻中心，
     * 向左滑动，此时新闻条目不会响应，菜单栏会响应这个事件。
     */
    public void onResume() {

    }
}
