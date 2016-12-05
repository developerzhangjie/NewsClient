package com.example.devel.newsclient.fragment;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.devel.newsclient.R;
import com.example.devel.newsclient.activity.MainActivity;
import com.example.devel.newsclient.home.BasePage;
import com.example.devel.newsclient.home.GovAffairsPage;
import com.example.devel.newsclient.home.HomePage;
import com.example.devel.newsclient.home.NewsCenterPage;
import com.example.devel.newsclient.home.SettingPage;
import com.example.devel.newsclient.home.SmartServicePage;
import com.example.devel.newsclient.view.CustomViewPager;
import com.example.devel.newsclient.view.LazyViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnRadioGroupCheckedChange;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Created by devel on 11/24/2016.
 */

public class HomeFragment extends BaseFragment implements LazyViewPager.OnPageChangeListener {
    @ViewInject(R.id.main_radio)
    private RadioGroup mMainRadio;
    @ViewInject(R.id.view_pager)
    private CustomViewPager mViewPager;
    private List<BasePage> mDataLists;
    private MenuFragment2 mMenuFragment2;
    private int mCurrentItem;

    @OnRadioGroupCheckedChange(R.id.main_radio)
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        mMenuFragment2 = (MenuFragment2) ((MainActivity) mContext).getSupportFragmentManager().findFragmentByTag("MENU");
        switch (checkedId) {
            /**
             * 点击mMainRadio后会执行mViewPager的onPageSelected方法，然后根据点击的条目，
             * 选择加载不同的数据，所以initData()是抽象方法。
             */
            // 首页
            case R.id.rb_function:
                // 第二个参数：去掉平滑的效果
                mViewPager.setCurrentItem(0, false);
                // 设置侧滑菜单不能滑动
                ((MainActivity) getActivity()).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
//                mDataLists.get(0).onResume();
                break;
            // 新闻中心
            case R.id.rb_news_center:
                mViewPager.setCurrentItem(1, false);
                // 设置侧滑菜单不能滑动
                ((MainActivity) getActivity()).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                // 判断当前的菜单fragment是否有值
                if (null != mMenuFragment2) {
                    //设置菜单类型
                    mMenuFragment2.setMenuType(MenuFragment2.NEWSCENTER);
                }
                //解决新闻条目滑动时的bug
                if (null != mDataLists && mDataLists.size() > 0) {
                    mDataLists.get(1).onResume();
                }
                break;
            // 智慧服务
            case R.id.rb_smart_service:
                mViewPager.setCurrentItem(2, false);
                ((MainActivity) getActivity()).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                if (null != mMenuFragment2) {
                    mMenuFragment2.setMenuType(MenuFragment2.SERVICE);
                }
//                mDataLists.get(2).onResume();
                break;
            // 政务指南
            case R.id.rb_gov_affairs:
                mViewPager.setCurrentItem(3, false);
                ((MainActivity) getActivity()).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                if (null != mMenuFragment2) {
                    mMenuFragment2.setMenuType(MenuFragment2.AFFAIRS);
                }
//                mDataLists.get(3).onResume();
                break;
            // 设置
            case R.id.rb_setting:
                mViewPager.setCurrentItem(4, false);
                // 设置侧滑菜单不能滑动
                ((MainActivity) getActivity()).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
//                mDataLists.get(4).onResume();
                break;
        }
    }

    @Override
    public View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.frag_home2, null);
        //注解
        ViewUtils.inject(this, view);
        mCurrentItem = R.id.rb_function;
        return view;
    }

    public NewsCenterPage getNewsCenterPage() {
        return (NewsCenterPage) mDataLists.get(1);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void initData() {
        mDataLists = new ArrayList<>();
        mDataLists.add(new HomePage(getActivity()));
        mDataLists.add(new NewsCenterPage(getActivity()));
        mDataLists.add(new SmartServicePage(getActivity()));
        mDataLists.add(new GovAffairsPage(getActivity()));
        mDataLists.add(new SettingPage(getActivity()));
        HomeAdapter homeAdapter = new HomeAdapter();
        mViewPager.setAdapter(homeAdapter);
        mViewPager.setOnPageChangeListener(this);
        mMainRadio.check(mCurrentItem);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        BasePage basePage = mDataLists.get(position);
        if (!basePage.isLoading) {
            basePage.isLoading = true;
            basePage.initData();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class HomeAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mDataLists.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mDataLists.get(position).getRootView());
            return mDataLists.get(position).getRootView();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mDataLists.get(position).getRootView());
        }
    }
}
