package com.example.devel.newsclient.home;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

/**
 * Description:政务指南界面
 * Created by devel on 11/24/2016.
 */

public class GovAffairsPage extends BasePage {
    public GovAffairsPage(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        TextView textView = new TextView(mContext);
        textView.setText("政务");
        //        initTitleBar(view);
        return textView;
    }

    @Override
    public void initData() {

    }
}
