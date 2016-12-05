package com.example.devel.newsclient.home;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

/**
 * Description:设置界面
 * Created by devel on 11/24/2016.
 */

public class SettingPage extends BasePage {
    public SettingPage(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        TextView textView = new TextView(mContext);
        textView.setText("设置中心");
        return textView;
    }

    @Override
    public void initData() {

    }
}
