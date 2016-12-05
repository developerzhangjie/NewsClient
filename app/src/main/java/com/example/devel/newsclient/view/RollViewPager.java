package com.example.devel.newsclient.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.devel.newsclient.R;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Description:热门新闻（新闻中心中滚动的新闻条目）
 * Created by devel on 11/29/2016.
 */

public class RollViewPager extends ViewPager {

    private BitmapUtils mBitmapUtils;
    private int downX;
    private int downY;
    private int currentX;
    private int currentY;
    private long downTimeMillis;
    private long currentTimeMillis;
    private ViewPageOnTouchListener mViewPageOnTouchListener;

    public interface ViewPageOnTouchListener {
        void onPageClickListener();
    }

    public RollViewPager(Context context) {
        super(context);
    }

    private Context mContext;
    private List<View> mDotList;

    public RollViewPager(Context context, List<View> mDotList, final ViewPageOnTouchListener mViewPageOnTouchListener) {
        super(context);
        this.mContext = context;
        this.mDotList = mDotList;
        this.mViewPageOnTouchListener = mViewPageOnTouchListener;
        mBitmapUtils = new BitmapUtils(mContext);
        mBitmapUtils.configDefaultBitmapConfig(Bitmap.Config.ARGB_4444);
        /**
         * 设置新闻条目的点击事件，长按，条目停止滚动；点击，进入详情。
         * 事件大于500毫秒，就是长按，否则，就是点击。
         */
        RollViewPager.this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //停止滚动
                        handler.removeCallbacksAndMessages(null);
                        downTimeMillis = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        start();
                        currentTimeMillis = System.currentTimeMillis();
                        if (currentTimeMillis - downTimeMillis < 500) {
                           // Toast.makeText(mContext, "点击", Toast.LENGTH_SHORT).show();
                            mViewPageOnTouchListener.onPageClickListener();
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        start();
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 设置热门新闻的标题
     *
     * @param top_news_title 显示标题的TextView
     * @param topNewsTitle   新闻的标题
     */
    private TextView mTopNewsTitle;
    private List<String> mTopNewsList;

    public void setTopNewsTitle(TextView top_news_title, List<String> topNewsTitle) {
        if (null != top_news_title && null != topNewsTitle && topNewsTitle.size() > 0) {
            //默认初始化，显示第一个标题
            top_news_title.setText(topNewsTitle.get(0));
            this.mTopNewsTitle = top_news_title;
            this.mTopNewsList = topNewsTitle;
        }
    }

    /**
     * 设置热门新闻的背景图
     *
     * @param topNewsImage 热门新闻的背景图的url地址
     */
    private List<String> mTopNewsImage;

    public void setTopNewsImage(List<String> topNewsImage) {
        this.mTopNewsImage = topNewsImage;
    }

    /**
     * 启动轮播图
     */
    private boolean hasAdapter = false;
    //点的上一个位置
    private int oldPosition = 0;

    public void start() {
        if (!hasAdapter) {
            hasAdapter = true;
            RollViewPagerAdapter adapter = new RollViewPagerAdapter();
            RollViewPager.this.setAdapter(adapter);
            RollViewPager.this.addOnPageChangeListener(new OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    mCurrentItem = position;
                    //标题滚动
                    if (null != mTopNewsList && mTopNewsList.size() > 0) {
                        mTopNewsTitle.setText(mTopNewsList.get(position));
                    }
                    //点滚动
                    if (null != mDotList && mDotList.size() > 0) {
                        mDotList.get(position).setBackgroundResource(R.drawable.dot_focus);
                        mDotList.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
                    }
                    oldPosition = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCurrentItem = (mCurrentItem + 1) % mTopNewsImage.size();
                handler.obtainMessage().sendToTarget();
            }
        }, 1500);
    }

    /**
     * 停止点
     */
    public void stop() {
        handler.removeCallbacksAndMessages(null);
    }

    private class RollViewPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mTopNewsImage.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(mContext, R.layout.viewpager_item, null);
            ImageView image = (ImageView) view.findViewById(R.id.image);
            mBitmapUtils.display(image, mTopNewsImage.get(position));
            container.addView(view);
            return view;
        }
    }

    private int mCurrentItem;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            RollViewPager.this.setCurrentItem(mCurrentItem, false);
            start();
        }
    };

    private boolean isMove = false;

    /**
     * 分发事件，处理新闻滚动条目左右滑动和上下滑动的冲突
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getX();
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                currentX = (int) ev.getX();
                currentY = (int) ev.getY();
                if (Math.abs(currentX - downX) > Math.abs(currentY - downY)) {
                    //左右滑动ViewPage
                    isMove = false;
                } else {
                    //上下滑动ListView
                    isMove = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        getParent().requestDisallowInterceptTouchEvent(!isMove);
        return super.dispatchTouchEvent(ev);
    }
}
