package com.example.devel.newsclient.view;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Description:旋转动画
 * Created by devel on 10/21/2016.
 */

public class RatatePageTransformer implements ViewPager.PageTransformer{
    private static float MIN_ROTATION = 25f;

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setRotation(0);
        } else if (position <= 0) { // [-1,0]
            //当前页面中心轴 角度
            view.setPivotX(pageWidth/2);
            view.setPivotY(view.getMeasuredHeight());
            view.setRotation(MIN_ROTATION*position);
        } else if (position <= 1) { // (0,1]
            //下一页面中心轴 角度
            view.setPivotX(pageWidth/2);
            view.setPivotY(view.getMeasuredHeight());
            view.setRotation(MIN_ROTATION*position);
        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setRotation(0);
        }
    }
}
