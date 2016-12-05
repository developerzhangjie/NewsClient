package com.example.devel.newsclient.home;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

/**
 * Description:首页界面
 * Created by devel on 11/24/2016.
 */

public class HomePage extends BasePage {
    public HomePage(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        TextView textView = new TextView(mContext);
        textView.setText("首页");
        return textView;
    }

    @Override
    public void initData() {

    }
}
