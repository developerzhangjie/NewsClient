package com.example.devel.newsclient.home;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.example.devel.newsclient.R;
import com.example.devel.newsclient.activity.MainActivity;
import com.example.devel.newsclient.bean.NewsCenter;
import com.example.devel.newsclient.menu.InteractPage;
import com.example.devel.newsclient.menu.NewsPage;
import com.example.devel.newsclient.menu.PicturesPage;
import com.example.devel.newsclient.menu.TopicPage;
import com.example.devel.newsclient.utils.HMAPI;
import com.example.devel.newsclient.utils.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static android.content.ContentValues.TAG;

/**
 * Description:新闻中心
 * Created by devel on 11/24/2016.
 */

@RequiresApi(api = Build.VERSION_CODES.M)
public class NewsCenterPage extends BasePage {

    private BasePage basePage;

    public NewsCenterPage(Context context) {
        super(context);
    }

    /**
     * 获取帧布局
     */
    @ViewInject(R.id.news_center_fl)
    private FrameLayout news_center_fl;

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.news_center_frame, null);
        ViewUtils.inject(this, view);
        initTitleBar(view);
        return view;
    }

    @Override
    public void initData() {
        //判断有没有缓存,有缓存就加载数据
        String result = SharedPreferencesUtils.getString(mContext, HMAPI.NEW_CENTER, "");
        if (!TextUtils.isEmpty(result)) {
            processData(result);
        }
        //获取新闻
        getNewsData();
    }

    /**
     * 父类中的自定义模拟生命周期的方法
     */
    @Override
    public void onResume() {
        //外层的if判断是提高代码的健壮性
        if (null != mMenuLists && mMenuLists.size() > 0) {
            //            BasePage basePage = mMenuLists.get(index);
            BasePage basePage1 = mMenuLists.get(0);
            basePage1.onResume();
        }
    }

    private void getNewsData() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, HMAPI.NEW_CENTER, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.d(TAG, responseInfo.result);
                //缓存联网成功后获取到的json数据,以便在没有网络的情况下，不至于没有数据显示,用户体验不好
                SharedPreferencesUtils.saveString(mContext, HMAPI.NEW_CENTER, responseInfo.result);
                //加载数据
                processData(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.d(TAG, "onFailure: 失败了");
            }
        });
    }

    /**
     * 封装所有的title,也就是新闻标题
     * private Set<String> mTitleLists = new LinkedHashSet<>();
     * 这行代码的容器以前是List,但是增加了缓存机制之后，有网络的情况下且有缓存的情况下，打开新闻中心
     * 旁边的菜单栏会有2个List集合，装着相同的标题，执行顺序只这样的：有缓存-->processData()-->
     * getNewsData()-->processData()，执行了2次processData()，而且List允许元素重复，所以会有2组标题。
     * 我的解决办法是换成Set,因为Set不允许元素重复，重复的问题解决了，但是来了一个新的问题，那就是标题的顺序
     * 变得随机排列了，因为Set是无序的。所以进一步改进，需要找一个既不允许元素重复又能保证元素的顺序的容器，
     * 找到了LinkedHashSet。这样就解决了上面出现的2个问题，并没有出现其他Bug。
     * 如果有更好的解决办法，就进一步改进。
     */
    //封装所有的title,也就是新闻标题
    private Set<String> mTitleLists = new LinkedHashSet<>();
    //封装所有的菜单栏中的条目
    private List<BasePage> mMenuLists = new ArrayList<>();

    /**
     * 处理服务器返回的json数据
     *
     * @param result: 服务器返回的json数据
     */
    private void processData(String result) {
        Gson gson = new Gson();
        NewsCenter newsCenter = gson.fromJson(result, NewsCenter.class);
        for (NewsCenter.DataBean dataBean : newsCenter.getData()) {
            //获取新闻标题
            mTitleLists.add(dataBean.getTitle());
        }
        //将获取到的新闻标题传递到菜单栏进行显示,也就是初始化菜单
        ((MainActivity) mContext).getMenuFragment2().initNewsCenterMenu(mTitleLists);
        //添加具体的菜单栏页面到集合中
        mMenuLists.clear();
        mMenuLists.add(new NewsPage(mContext, newsCenter.getData().get(0)));
        mMenuLists.add(new TopicPage(mContext));
        mMenuLists.add(new PicturesPage(mContext));
        mMenuLists.add(new InteractPage(mContext));
        //默认选中新闻
        switchFragment(0);
    }

    /**
     * 当菜单栏中的某个条目被点击时，用来在某个具体的Page中切换界面
     * news_center_fl.removeAllViews()的作用是将之前的页面移除
     *
     * @param i 菜单栏中被点击条目的位置，也就是position
     */
    //    private int index;
    private boolean picType = false;

    public void switchFragment(int i) {
        //根据点击的条目位置获取获取相应的界面
        basePage = mMenuLists.get(i);
        news_center_fl.removeAllViews();
        news_center_fl.addView(basePage.getRootView());
        switch (i) {
            case 0:
                imgbtn_right.setVisibility(View.INVISIBLE);
                txt_title.setText("新闻");
                break;
            case 1:
                imgbtn_right.setVisibility(View.INVISIBLE);
                txt_title.setText("专题");
                break;
            case 2:
                imgbtn_right.setImageResource(R.drawable.icon_pic_list_type);
                /**
                 * imgbtn_right设置点击事件
                 * 1.切换按钮的样式，默认是listView，点击——>gridView，再点击——>listView
                 * 这里需要一个变量标记样式，定义为 boolean picType.
                 * true显示listView，false显示gridView
                 * 2.切换组图样式
                 * listView true 点击 按钮变成gridView 组图由gridView变为ListView
                 * gridView false 点击 按钮变成listView 组图由listView变成gridView
                 */
                imgbtn_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (picType) {
                            imgbtn_right.setImageResource(R.drawable.icon_pic_grid_type);
                            //选择listView组图
                            Log.d(TAG, "onClick: listView");
                            ((PicturesPage) basePage).switchPicType(picType);
                            picType = false;
                        } else {
                            imgbtn_right.setImageResource(R.drawable.icon_pic_list_type);
                            //选择gridView组图
                            Log.d(TAG, "onClick: gridView");
                            ((PicturesPage) basePage).switchPicType(picType);
                            picType = true;
                        }
                    }
                });
                txt_title.setText("图片");
                break;
            case 3:
                imgbtn_right.setVisibility(View.INVISIBLE);
                txt_title.setText("互动");
                break;
        }
        basePage.initData();
        //        index = i;
    }
}
