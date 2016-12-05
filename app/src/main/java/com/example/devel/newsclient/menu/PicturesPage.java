package com.example.devel.newsclient.menu;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;

import com.example.devel.newsclient.R;
import com.example.devel.newsclient.adapter.PictureAdapter;
import com.example.devel.newsclient.bean.Picture;
import com.example.devel.newsclient.home.BasePage;
import com.example.devel.newsclient.utils.GsonTools;
import com.example.devel.newsclient.utils.HMAPI;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Description:菜单栏中的组图界面
 * Created by devel on 11/26/2016.
 */

public class PicturesPage extends BasePage {
    @ViewInject(R.id.lv_photo_list)
    private ListView lv_photo_list;
    @ViewInject(R.id.gv_photo_grid)
    private GridView gv_photo_grid;
    private PictureAdapter pictureAdapter;

    public PicturesPage(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.photo, null);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, HMAPI.PHOTO_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.d(TAG, "onSuccess: 获取到了图片地址");
                Log.d(TAG, "onSuccess: " + responseInfo.result);
                processData(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.d(TAG, "onFailure: 获取图片地址失败");
            }
        });
    }

    /**
     * 解析数据
     *
     * @param result
     */
    //private List<Picture.DataBean.NewsBean> mImageList = new ArrayList<>();

    private void processData(String result) {
        Picture picture = GsonTools.changeGsonToBean(result, Picture.class);
        List<Picture.DataBean.NewsBean> news = picture.getData().getNews();
       /* mImageList.clear();
        for (Picture.DataBean.NewsBean newsItem : news) {
            mImageList.add(newsItem);
        }*/
        //展示数据
        pictureAdapter = new PictureAdapter(news, mContext);
        lv_photo_list.setAdapter(pictureAdapter);
    }

    /**
     * 选择显示组图的样式ListView和GridView样式
     * false:显示GridView
     * true:显示ListView
     *
     * @param type
     */
    public void switchPicType(boolean type) {
        if (type) {
            Log.d(TAG, "switchPicType: 这是ListView组图");
            lv_photo_list.setVisibility(View.VISIBLE);
            gv_photo_grid.setVisibility(View.INVISIBLE);
            lv_photo_list.setAdapter(pictureAdapter);
        } else {
            Log.d(TAG, "switchPicType: 这是GridView组图");
            gv_photo_grid.setVisibility(View.VISIBLE);
            lv_photo_list.setVisibility(View.INVISIBLE);
            gv_photo_grid.setAdapter(pictureAdapter);
        }
    }
}
