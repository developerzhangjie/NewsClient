package com.example.devel.newsclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.devel.newsclient.R;
import com.example.devel.newsclient.view.DepthPageTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:引导界面
 * Created by devel on 11/23/2016.
 */

public class GuideActivity extends Activity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private ViewPager mViewPager;
    private int[] imageRes = {R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};
    private List<ImageView> mImageList;
    private Button btn;
    private LinearLayout mLlAllPoint;
    private View mPoint;
    private int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        mViewPager.addOnPageChangeListener(this);
        btn.setOnClickListener(this);
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        btn = (Button) findViewById(R.id.btn);
        // 所有的点
        mLlAllPoint = (LinearLayout) findViewById(R.id.ll_all_point);
        // 选中的点
        mPoint = findViewById(R.id.point);

    }

    private void initData() {
        mImageList = new ArrayList<>();
        //添加背景图片
        for (int i = 0; i < imageRes.length; i++) {
            /*
            ImageView imageView = new ImageView(this);
            一定写在for循环里面，不然会报 the specified child already has a parent you must call removeview on the child's parent first
            折腾了整整一上午
             */
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(imageRes[i]);
            mImageList.add(imageView);
            //初始化点
            View view = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10, 10);
            view.setLayoutParams(params);
            view.setBackgroundResource(R.drawable.dot_normal);
            if (i != 0) {
                params.leftMargin = 30;
            }
            mLlAllPoint.addView(view);
        }
        GuideAdapter guideAdapter = new GuideAdapter();
        mViewPager.setAdapter(guideAdapter);
        //设置动画
        mViewPager.setPageTransformer(true, new DepthPageTransformer());
        //获取间距
        mPoint.post(new Runnable() {
            @Override
            public void run() {
                width = mLlAllPoint.getChildAt(1).getLeft() - mLlAllPoint.getChildAt(0).getLeft();
            }
        });
    }

    //页面左右滑动方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        float newPosition = (width * (position + positionOffset));
        mPoint.setTranslationX(newPosition);
    }

    //页面选中方法
    @Override
    public void onPageSelected(int position) {
        //最后一张欢迎页设置按钮可见
        if (position == mImageList.size() - 1) {
            btn.setVisibility(View.VISIBLE);
        } else {
            btn.setVisibility(View.INVISIBLE);
        }
    }

    //页面状态改变方法
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private class GuideAdapter extends PagerAdapter {
        //移除当前对象
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //mViewPager.removeViewChild(position);
            container.removeView((View) object);
        }

        //获取图片添加到ViewPager中
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mImageList.get(position));
            //mViewPager.addViewChild(mImageList.get(position),position);
            return mImageList.get(position);
        }

        //判断是否由对象生成界面
        @Override
        public int getCount() {
            return mImageList.size();
        }

        //获取当前窗体界面数
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
