package com.example.devel.newsclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.devel.newsclient.R;
import com.example.devel.newsclient.utils.SharedPreferencesUtils;

/**
 * Description:欢迎界面
 * Created by devel on 11/23/2016.
 */

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
    }

    private void initView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean isFirstOpen = SharedPreferencesUtils.getBoolean(SplashActivity.this, "is_first_open", true);
                if (isFirstOpen) {
                    //是第一次打开APP，进入引导页面
                    SharedPreferencesUtils.saveBoolean(SplashActivity.this, "is_first_open", false);
                    Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    //不是第一次打开APP，进入欢迎页面
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 1500);
    }
}
