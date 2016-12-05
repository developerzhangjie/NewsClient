package com.example.devel.newsclient.home;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

/**
 * Description:智慧服务界面
 * Created by devel on 11/24/2016.
 */

public class SmartServicePage extends BasePage {
    public SmartServicePage(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        TextView textView = new TextView(mContext);
        textView.setText("智慧服务");
//        initTitleBar(view);
        return textView;
    }

    @Override
    public void initData() {

    }
}
