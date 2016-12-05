package com.example.devel.newsclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.devel.newsclient.R;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import static com.example.devel.newsclient.R.id.imgbtn_left;
import static com.example.devel.newsclient.R.id.imgbtn_right;

/**
 * Description:详情页面
 * Created by devel on 12/2/2016.
 */

public class DetailActivity extends Activity implements View.OnClickListener {

    private WebView mWebView;
    private View mLoadingView;
    private String mUrl;
    private WebSettings webSettings;
    private int textSize = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShareSDK.initSDK(DetailActivity.this);
        initView();
        initData();
    }

    private void initView() {
        setContentView(R.layout.act_news_detail);
        mWebView = (WebView) findViewById(R.id.news_detail_wv);
        mLoadingView = findViewById(R.id.loading_view);
        Button btn_left = (Button) findViewById(R.id.btn_left);
        btn_left.setVisibility(View.GONE);
        ImageButton mBack = (ImageButton) findViewById(imgbtn_left);
        mBack.setImageResource(R.drawable.back);
        mBack.setOnClickListener(this);
        TextView txt_title = (TextView) findViewById(R.id.txt_title);
        txt_title.setText("新闻详情");
        ImageButton imgbtn_text = (ImageButton) findViewById(R.id.imgbtn_text);
        imgbtn_text.setVisibility(View.GONE);
        //字体大小
        ImageButton imbnTextSize = (ImageButton) findViewById(imgbtn_right);
        imbnTextSize.setImageResource(R.drawable.icon_textsize);
        imbnTextSize.setOnClickListener(this);
        ImageButton imbnShare = (ImageButton) findViewById(R.id.btn_right);
        imbnShare.setImageResource(R.drawable.icon_share);
        imbnShare.setOnClickListener(this);
        //获取网页设置
        webSettings = mWebView.getSettings();
    }

    private void initData() {
        Intent intent = getIntent();
        if (null != intent) {
            mUrl = intent.getStringExtra("url");
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    mLoadingView.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    mLoadingView.setVisibility(View.VISIBLE);
                }
            });
            mWebView.loadUrl(mUrl);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //返回上一个界面
            case R.id.imgbtn_left:
                finish();
                break;
            //字体大小
            case R.id.imgbtn_right:
                textSize = (++textSize) % 4;
                Log.d("i", "onClick: " + textSize);
                setTextSize(textSize);
                break;
            //分享
            case R.id.btn_right:
                showShare();
                break;

        }
    }

    /**
     * 设置字体大小
     *
     * @param i 字体大小 默认100
     */
    private void setTextSize(int i) {
        switch (i) {
            case 0:
                webSettings.setTextZoom(80);
                break;
            case 1:
                webSettings.setTextZoom(100);
                break;
            case 2:
                webSettings.setTextZoom(120);
                break;
            case 3:
                webSettings.setTextZoom(140);
                break;
        }
    }

    /**
     * 分享
     */
    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("分享标题");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
        // 启动分享GUI
        oks.show(this);
    }
}
