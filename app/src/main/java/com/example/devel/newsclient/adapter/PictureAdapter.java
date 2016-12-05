package com.example.devel.newsclient.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.devel.newsclient.R;
import com.example.devel.newsclient.bean.Picture;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Description:组图的适配器
 * Created by devel on 12/3/2016.
 */

public class PictureAdapter extends MyBaseAdapter<Picture.DataBean.NewsBean> {

    private final BitmapUtils bitmapUtils;

    public PictureAdapter(List mData, Context mContext) {
        super(mData, mContext);
        bitmapUtils = new BitmapUtils(mContext);
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.ARGB_4444);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (null == view) {
            view = View.inflate(mContext, R.layout.photo_item, null);
        }
        ImageView iv_photoitem_image = (ImageView) view.findViewById(R.id.iv_photoitem_image);
        TextView tv_photoitem_title = (TextView) view.findViewById(R.id.tv_photoitem_title);
        bitmapUtils.display(iv_photoitem_image, mData.get(i).getListimage());
        tv_photoitem_title.setText(mData.get(i).getTitle());
        return view;
    }
}
