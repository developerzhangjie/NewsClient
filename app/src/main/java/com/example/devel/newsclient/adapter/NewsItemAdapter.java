package com.example.devel.newsclient.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.devel.newsclient.R;
import com.example.devel.newsclient.bean.NewsItemData;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Description:新闻条目的适配器
 * Created by devel on 11/29/2016.
 */

public class NewsItemAdapter extends MyBaseAdapter<NewsItemData.DataBean.NewsBean> {

    private final BitmapUtils bitmapUtils;

    public NewsItemAdapter(List<NewsItemData.DataBean.NewsBean> mNews, Context mContext) {
        super(mNews, mContext);
        bitmapUtils = new BitmapUtils(mContext);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(mContext, R.layout.layout_news_item, null);
        }
        ImageView iv_img = (ImageView) view.findViewById(R.id.iv_img);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        TextView tv_pub_date = (TextView) view.findViewById(R.id.tv_pub_date);
        NewsItemData.DataBean.NewsBean newsBean = mData.get(i);
        if (newsBean.isRead) {
            tv_title.setTextColor(Color.RED);
        } else {
            tv_title.setTextColor(Color.BLACK);
        }
        tv_title.setText(newsBean.getTitle());
        tv_pub_date.setText(newsBean.getPubdate());
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.ARGB_4444);
        bitmapUtils.display(iv_img, newsBean.getListimage());
        return view;
    }
}
