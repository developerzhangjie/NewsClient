package com.example.devel.newsclient.menu;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devel.newsclient.R;
import com.example.devel.newsclient.activity.DetailActivity;
import com.example.devel.newsclient.adapter.NewsItemAdapter;
import com.example.devel.newsclient.bean.NewsItemData;
import com.example.devel.newsclient.home.BasePage;
import com.example.devel.newsclient.utils.CommonUtil;
import com.example.devel.newsclient.utils.HMAPI;
import com.example.devel.newsclient.utils.SharedPreferencesUtils;
import com.example.devel.newsclient.view.RollViewPager;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Description:新闻条目
 * Created by devel on 11/28/2016.
 */

public class NewsItemPage extends BasePage {
    private String url;
    private NewsItemAdapter mAdapter;
    private View topView;
    @ViewInject(R.id.top_news_viewpager)
    private LinearLayout top_news_viewpager;
    @ViewInject(R.id.lv_item_news)
    private PullToRefreshListView lv_item_news;
    @ViewInject(R.id.dots_ll)
    private LinearLayout dots_ll;
    @ViewInject(R.id.top_news_title)
    private TextView top_news_title;
    private RollViewPager mViewPager;
    String moreUrl;


    private String[] mStrings = {"Abbaye de Belloc"};

    public NewsItemPage(Context context) {
        super(context);
    }

    public NewsItemPage(Context mContext, String url) {
        super(mContext);
        this.url = url;
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.frag_item_news, null);
        topView = View.inflate(mContext, R.layout.layout_roll_view, null);
        ViewUtils.inject(this, view);
        ViewUtils.inject(this, topView);
        lv_item_news.onRefreshing(false);
        lv_item_news.getRefreshableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NewsItemData.DataBean.NewsBean itemAtPosition = (NewsItemData.DataBean.NewsBean) lv_item_news.getRefreshableView().getItemAtPosition(i);
                if (!itemAtPosition.isRead) {
                    itemAtPosition.isRead = true;
                }
                /**
                 * 跳转到详情页面
                 */
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("url", itemAtPosition.getUrl());
                mContext.startActivity(intent);
                mAdapter.notifyDataSetChanged();
            }
        });
        /**
         * 下拉刷新
         */
        lv_item_news.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                Log.d("TAG", "onRefresh: 刷新le------------------");
                String label = DateUtils.formatDateTime(mContext, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                // Do work to refresh the list here.
                new GetDataTask().execute();
            }
        });

        /**
         * 滑动到最后一条触发的事件
         */
        lv_item_news.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                getNewsItemData(false, moreUrl);
            }
        });
        return view;
    }

    @Override
    public void initData() {
        //获取缓存数据
        String string = SharedPreferencesUtils.getString(mContext, HMAPI.BASE_URL + url, "");
        if (!TextUtils.isEmpty(string)) {
            processNewsItemData(string, true);
        }
        //获取新闻条目的数据
        getNewsItemData(true, moreUrl);
    }

    private void getNewsItemData(final boolean isRefresh, final String url) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, HMAPI.BASE_URL + url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.d(TAG, "onSuccess: 获取新闻条目数据成功");
                //缓存数据
                if (isRefresh) {
                    SharedPreferencesUtils.saveString(mContext, HMAPI.BASE_URL + url, responseInfo.result);
                    Log.d("url", "onSuccess: URL是" + url);
                }
                processNewsItemData(responseInfo.result, isRefresh);

            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.d(TAG, "onFailure: 获取新闻条目数据失败");
            }
        });
    }

    /**
     * 解析json
     *
     * @param result json数据
     */
    //新闻的数据
    List<NewsItemData.DataBean.NewsBean> mNews = new ArrayList<>();

    private void processNewsItemData(String result, boolean isRefresh) {
        Gson gson = new Gson();
        NewsItemData newsItemData = gson.fromJson(result, NewsItemData.class);
        if (null != newsItemData && newsItemData.getRetcode() == 200) {
            if (isRefresh) {
                if (null != mViewPager) {
                    mViewPager.stop();
                }
                //获取到热门新闻
                List<NewsItemData.DataBean.TopnewsBean> topnewsBeen = newsItemData.getData().getTopnews();
                //初始化轮播图的点
                initDot(topnewsBeen.size());
                //获取到热门新闻的标题
                List<String> topNewsTitle = new ArrayList<>();
                //获取到热门新闻的背景图
                List<String> topNewsImage = new ArrayList<>();
                for (NewsItemData.DataBean.TopnewsBean top : topnewsBeen) {
                    topNewsTitle.add(top.getTitle());
                    topNewsImage.add(top.getTopimage());
                }
                isLoading = true;
                for (NewsItemData.DataBean.NewsBean news : newsItemData.getData().getNews()) {
                    mNews.add(news);
                }
                //展示数据
                mViewPager = new RollViewPager(mContext, mDotList, new RollViewPager.ViewPageOnTouchListener() {
                    @Override
                    public void onPageClickListener() {
                        Toast.makeText(mContext, "点击", Toast.LENGTH_SHORT).show();
                    }
                });
                //设置显示热门新闻标题
                mViewPager.setTopNewsTitle(top_news_title, topNewsTitle);
                //设置显示热门新闻的背景图
                mViewPager.setTopNewsImage(topNewsImage);
                mViewPager.start();
                top_news_viewpager.removeAllViews();
                top_news_viewpager.addView(mViewPager);
            }
            /**
             * 判断是否已经有头，解决乱跳bug
             */
            /*if (lv_item_news.getRefreshableView().getHeaderViewsCount() < 1) {
                lv_item_news.getRefreshableView().addHeaderView(topView);
                Log.d("TAG", "加载了头");
            } else {
                Log.d("TAG", "加载了头哈哈哈哈");

            }*/
            int headerViewsCount = lv_item_news.getRefreshableView().getHeaderViewsCount();
            Log.d("TAG", "数字啊" + headerViewsCount);
            if (headerViewsCount < 1) {
                lv_item_news.getRefreshableView().addHeaderView(topView);

            } else {
                Log.d("TAG", "processNewsItemData: 不添加");
            }
            moreUrl = newsItemData.getData().getMore();
            //下拉刷新，加载数据
            if (isRefresh) {
                mNews.clear();
                mNews.addAll(newsItemData.getData().getNews());
            } else {
                mNews.addAll(newsItemData.getData().getNews());
            }

            if (null == mAdapter) {
                mAdapter = new NewsItemAdapter(mNews, mContext);
                lv_item_news.getRefreshableView().setAdapter(mAdapter);
                Log.d("TAG", "设置adapter");
            } else {
                mAdapter.notifyDataSetChanged();
                Log.d("TAG", "刷新adapter");
            }
        }
    }

    //用来存放点的集合
    List<View> mDotList = new ArrayList<>();

    /**
     * 动态初始化轮播图的点
     *
     * @param size 点的个数，根据轮播图的个数来添加
     */
    private void initDot(int size) {
        dots_ll.removeAllViews();
        mDotList.clear();
        for (int i = 0; i < size; i++) {
            View view = new View(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(CommonUtil.dip2px(mContext, 5), CommonUtil.dip2px(mContext, 5));
            //设置左右的间距
            params.leftMargin = CommonUtil.dip2px(mContext, 5);
            if (i == 0) {
                view.setBackgroundResource(R.drawable.dot_focus);
            } else {
                view.setBackgroundResource(R.drawable.dot_normal);
            }
            view.setLayoutParams(params);
            dots_ll.addView(view);
            mDotList.add(view);
        }
    }


    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Log.d(TAG, "doInBackground: 这是在睡觉");
                Thread.sleep(1500);
            } catch (InterruptedException e) {
            }
            return mStrings;
        }

        @Override
        protected void onPostExecute(String[] result) {
            Log.d(TAG, "onPostExecute: 找我his在大擦冯绍峰");
            getNewsItemData(true, moreUrl);
            mAdapter.notifyDataSetChanged();
            // Call onRefreshComplete when the list has been refreshed.
            lv_item_news.onRefreshComplete();
        }
    }
}
