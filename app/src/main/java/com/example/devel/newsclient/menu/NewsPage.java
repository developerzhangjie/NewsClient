package com.example.devel.newsclient.menu;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.example.devel.newsclient.R;
import com.example.devel.newsclient.bean.NewsCenter;
import com.example.devel.newsclient.home.BasePage;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:菜单栏中的新闻界面
 * Created by devel on 11/26/2016.
 */

public class NewsPage extends BasePage {
    private NewsCenter.DataBean mDataBean;

    public NewsPage(Context context) {
        super(context);
    }

    public NewsPage(Context mContext, NewsCenter.DataBean dataBean) {
        super(mContext);
        this.mDataBean = dataBean;
    }

    @ViewInject(R.id.indicator)
    private TabPageIndicator mIndicator;
    @ViewInject(R.id.pager)
    private ViewPager mPager;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.simple_tabs, null);
        ViewUtils.inject(this, view);
        return view;
    }

    /**
     * 父类BasePage中的自定义方法
     */
    @Override
    public void onResume() {
        /**
         * 解决右滑时菜单跟新闻条目冲突的bug，当条目的位置是0时，菜单可以滑动；
         * 其余的位置，菜单都不能滑动。
         */
        if (mCurrentItem == 0) {
            mSm.setTouchModeAbove(SlidingMenu.RIGHT);
        } else {
            mSm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

    private List<NewsItemPage> mNewsItemPages = new ArrayList<>();
    private int mCurrentItem = 0;

    @Override
    public void initData() {
        for (NewsCenter.DataBean.ChildrenBean children : mDataBean.getChildren()) {
            mNewsItemPages.add(new NewsItemPage(mContext, children.getUrl()));
        }
        mPager.setAdapter(new NewsPageAdapter());
        //跟ViewPager绑定在一起
        mIndicator.setViewPager(mPager);
        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentItem = position;
                NewsItemPage newsItemPage = mNewsItemPages.get(position);
                if (!isLoading) {
                    newsItemPage.initData();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mIndicator.setCurrentItem(mCurrentItem);
        mIndicator.setVisibility(View.VISIBLE);
        //手动调用
        mNewsItemPages.get(0).initData();
        mNewsItemPages.get(0).isLoading = true;
    }

    private class NewsPageAdapter extends PagerAdapter {
        /**
         * 获取标题
         *
         * @param position
         * @return
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return mDataBean.getChildren().get(position).getTitle();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mNewsItemPages.get(position).getRootView());
            return mNewsItemPages.get(position).getRootView();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mNewsItemPages.get(position).getRootView());
        }

        @Override
        public int getCount() {
            return mNewsItemPages.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
