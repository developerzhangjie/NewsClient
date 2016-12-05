package com.example.devel.newsclient.menu;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.devel.newsclient.home.BasePage;

/**
 * Description:菜单栏中的互动界面
 * Created by devel on 11/28/2016.
 */

public class InteractPage extends BasePage {
    public InteractPage(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        TextView textView = new TextView(mContext);
        textView.setText("互动");
        return textView;
    }

    @Override
    public void initData() {

    }
}
