package com.zmm.recyclerviewfoot;

import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.xdandroid.simplerecyclerview.Divider;
import com.xdandroid.simplerecyclerview.OnItemClickListener;
import com.xdandroid.simplerecyclerview.SimpleRecyclerView;
import com.xdandroid.simplerecyclerview.SimpleSwipeRefreshLayout;
import com.xdandroid.simplerecyclerview.UIUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SimpleSwipeRefreshLayout mSwipeContainer;
    SimpleRecyclerView mRecyclerView;
    BasicAdapter mAdapter;
    List<SampleBean> mSampleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        mSwipeContainer = (SimpleSwipeRefreshLayout)findViewById(R.id.swipe_container);
        mRecyclerView = (SimpleRecyclerView)findViewById(R.id.recycler_view);
        setupSwipeContainer();
        setupRecyclerView();
        initData();
    }

    void setupSwipeContainer() {
        mSwipeContainer.setColorSchemeResources(R.color.colorAccent);
        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {initData();}
        });

        //启动SwipeRefreshLayout样式下拉刷新转圈。
        //mSwipeContainer.setRefreshing(true);

        //启动自定义LoadingView布局。
        mRecyclerView.setLoadingView(findViewById(R.id.loading_view));
    }

    void setupRecyclerView() {
        //添加Divider
        mRecyclerView.addItemDecoration(new Divider(
                //分割线宽1dp
                UIUtils.dp2px(MainActivity.this, 1),
                //分割线颜色#DDDDDD
                Color.parseColor("#DDDDDD"),
                false,
                //分割线左侧留出20dp的空白，不绘制
                UIUtils.dp2px(MainActivity.this, 20), 0, 0, 0));

        mAdapter = new BasicAdapter() {

            protected void onLoadMore(Void v) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        List<SampleBean> moreSampleList = new ArrayList<>();
                        int j = 0;
                        for (int i = 0; i < 26; i++) {
                            char c = (char) (65 + i);
                            if (i % 8 == 0) {
                                moreSampleList.add(new SampleBean(SampleBean.TYPE_BANNER, null, null, BannerProvider.getMessage(j), BannerProvider.getImageResId(j)));
                                j++;
                            } else {
                                moreSampleList.add(new SampleBean(SampleBean.TYPE_TEXT, "Title " + String.valueOf(c), "Content " + String.valueOf(c), null, 0));
                            }
                        }
                        mSampleList.addAll(moreSampleList);
                        mAdapter.onAddedAll(moreSampleList.size());
                    }
                }, 1777);
            }

            protected boolean hasMoreElements(Void v) {
                return mSampleList != null && mSampleList.size() <= 666;
            }
        };

        //设置加载更多的Threshold, 即离最后一个还有多少项时就开始提前加载
        mAdapter.setThreshold(7);

        //设置点击事件的监听器
//        mAdapter.setOnItemClickListener(new OnItemClickListener() {
//            public void onItemClick(RecyclerView.ViewHolder holder, View view, int position, int viewType) {
//                Toast.makeText(MainActivity.this, "Clicked " + position, Toast.LENGTH_SHORT).show();
//            }
//        });
        mAdapter.setOnShortItemClickListener(new BasicAdapter.OnShortItemClickListener() {
            @Override
            public void OnShortItemClick(String title, String content, int position) {
                String s = "title="+title+",content="+content+",position="+position;
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });


        /**
         * true为使用 SwipeRefreshLayout 样式的加载更多转圈，以及设置转圈的颜色。false为使用 ProgressBar样式的加载更多转圈。
         * SwipeRefreshLayout 样式与系统版本无关。
         * ProgressBar的外观因系统版本而异，仅在 API 21 以上的 Android 系统中具有 Material Design 风格。
         */
        mAdapter.setUseMaterialProgress(true, new int[]{getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorPrimary)});

        //也可单独调用API设置转圈的颜色变化序列.
        //mAdapter.setColorSchemeColors(new int[]{getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorPrimary)});

        //可设置转圈所在圆形突起的背景色.
        //mAdapter.setProgressBackgroundColor(0xFFFAFAFA);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mRecyclerView.setAdapter(mAdapter);

        //设置EmptyView
        //mRecyclerView.setEmptyView(getActivity().findViewById(R.id.empty_view));

        //显示ErrorView
        //mRecyclerView.showErrorView(getActivity().findViewById(R.id.error_view));

        //隐藏ErrorView
        //mRecyclerView.hideErrorView();
    }

    void initData() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                mSampleList = new ArrayList<>();
                int j = 0;
                for (int i = 0; i < 26; i++) {
                    char c = (char) (65 + i);
                    if (i % 8 == 0) {
                        mSampleList.add(new SampleBean(SampleBean.TYPE_BANNER, null, null, BannerProvider.getMessage(j), BannerProvider.getImageResId(j)));
                        j++;
                    } else {
                        mSampleList.add(new SampleBean(SampleBean.TYPE_TEXT, "Title " + String.valueOf(c), "Content " + String.valueOf(c), null, 0));
                    }
                }
                mAdapter.setList(mSampleList);
                mSwipeContainer.setRefreshing(false);
            }
        }, 1777);
    }
}
