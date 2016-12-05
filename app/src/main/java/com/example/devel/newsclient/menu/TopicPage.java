package com.example.devel.newsclient.menu;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.devel.newsclient.home.BasePage;

/**
 * Description:专题
 * Created by devel on 11/26/2016.
 */

public class TopicPage extends BasePage {
    public TopicPage(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        TextView textView = new TextView(mContext);
        textView.setText("专题");
        return textView;
    }

    @Override
    public void initData() {

    }
}
