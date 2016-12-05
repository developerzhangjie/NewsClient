package com.example.devel.newsclient.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.devel.newsclient.R;
import com.example.devel.newsclient.fragment.HomeFragment;
import com.example.devel.newsclient.fragment.MenuFragment;
import com.example.devel.newsclient.fragment.MenuFragment2;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * Description:主界面
 * Created by devel on 11/23/2016.
 */
public class MainActivity extends SlidingFragmentActivity {

    private SlidingMenu mSlidingMenu;
    private MenuFragment fragment;
    private MenuFragment2 fragment2;
    private HomeFragment homeFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置内容页面
        setContentView(R.layout.activity_main);
        //设置旁边的菜单页面
        setBehindContentView(R.layout.activity_menu);
        mSlidingMenu = getSlidingMenu();
        //滑动模式
        mSlidingMenu.setMode(SlidingMenu.LEFT);
        //设置侧滑菜单的滑动模式
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        // 设置菜单页面出来之后，内容页面的剩余宽度
        mSlidingMenu.setBehindOffsetRes(R.dimen.sm_width);
        // 设置阴影图片
        mSlidingMenu.setShadowDrawable(R.drawable.shadow);
        // 设置阴影图片的宽度
        mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        MenuFragment2 menuFragment = new MenuFragment2();
        HomeFragment homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, homeFragment, "HOME").commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.menu, menuFragment, "MENU").commit();
    }

    public void selectFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
        //打开-->关闭 关闭-->打开
        mSlidingMenu.toggle();
    }

    public MenuFragment getMenuFragment() {
        fragment = (MenuFragment) getSupportFragmentManager().findFragmentByTag("MENU");
        return fragment;
    }

    public MenuFragment2 getMenuFragment2() {
        fragment2 = (MenuFragment2) getSupportFragmentManager().findFragmentByTag("MENU");
        return fragment2;
    }

    public HomeFragment getHomeFragment() {
        homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("HOME");
        return homeFragment;
    }
}
