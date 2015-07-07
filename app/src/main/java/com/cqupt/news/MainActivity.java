package com.cqupt.news;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.cqupt.adapter.NewsAdapter;
import com.cqupt.bean.NewsBean;
import com.cqupt.listview.ReFreshListView;
import com.cqupt.utils.HtmlUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity implements ReFreshListView.IRefreshListener {
    //是否退出程序
    private static boolean isExit = false;
    //定时促发器
    private static Timer mTimer = null;
    private ReFreshListView mListView;
    private static final String htmlUrl = "http://www.dz.tt/news.html";

    private List<NewsBean> mList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ReFreshListView) findViewById(R.id.list_view);

        new NewsAsyncTask().execute(htmlUrl);


        mListView.setInterface(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ItemActivity.class);
                intent.putExtra("contentUrl", mList.get(position - 1).getContentUrl());
                startActivity(intent);
            }
        });
    }

    /**
     * 按两次返回键退出程序
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isExit == false) {
                isExit = true;
                if (mTimer != null) {
                    mTimer.cancel();//将原任务从队列中移除
                }
                //从新实例一个定时器
                mTimer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        isExit = false;
                    }
                };

                Toast.makeText(MainActivity.this, "再按一次退出程序！", Toast.LENGTH_SHORT).show();
                //延时两秒促发task任务
                mTimer.schedule(task, 2000);
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }

        return super.onKeyUp(keyCode, event);
    }

    /**
     * 实现ReFreshListView中的刷新数据接口
     */
    @Override
    public void onRefresh() {

        //获取最新数据
        setRefreshData();
        //通知界面显示
        NewsAdapter adapter = new NewsAdapter(MainActivity.this, mList, mListView);
        mListView.setAdapter(adapter);
        //通知ListView刷新数据完毕
        mListView.reFreshComplete();
    }


    public void setRefreshData() {
        for (int i = 0; i < 2; i++) {
            NewsBean newsBean = new NewsBean();
            newsBean.setImageUrl("http://img.lesu.cc/201410/amcdz/upload/images20150703/e468ff62bc8b62eec221de4addc86ec9.jpg");
            newsBean.setTitle("wo 是新数据" + i);
            newsBean.setPublishTime(i + "");
            mList.add(0, newsBean);
        }
    }


    /**
     * 实现网络的异步访问
     */
    class NewsAsyncTask extends AsyncTask<String, Void, List<NewsBean>> {

        @Override
        protected List<NewsBean> doInBackground(String... strings) {
            try {
                mList = HtmlUtils.htmlParseToNewsInfo(strings[0]);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "请检查网络!", Toast.LENGTH_SHORT).show();
            }
            return mList;
        }

        @Override
        protected void onPostExecute(List<NewsBean> newsBeans) {
            super.onPostExecute(newsBeans);
            NewsAdapter adapter = new NewsAdapter(MainActivity.this, newsBeans, mListView);
            mListView.setAdapter(adapter);
        }
    }
}
