package com.cqupt.news;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.cqupt.adapter.NewsAdapter;
import com.cqupt.bean.NewsBean;
import com.cqupt.listview.ReFreshListView;
import com.cqupt.utils.HtmlUtils;

import java.util.List;


public class MainActivity extends Activity implements ReFreshListView.IRefreshListener {
    private ReFreshListView mListView;
    private static final String htmlUrl = "http://www.dz.tt/news.html";

    private List<NewsBean> mList = null;


//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            int i = msg.what;
//            if (i == 1) {
//                content.setText(str);
//                NewsAdapter myListViewAdapter = new NewsAdapter(list, MainActivity.this);
//                mListView.setAdapter(myListViewAdapter);
//            }
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ReFreshListView) findViewById(R.id.list_view);

        new NewsAsyncTask().execute(htmlUrl);


        mListView.setInterface(this);
//        mListView.setAdapter();

//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    list = HtmlUtils.htmlParseToNewsInfo("http://www.dz.tt/news.html");
//                    Log.i("集合大小==", list.size() + "");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                Message msg = new Message();
//                msg.what = 1;
//                handler.sendMessage(msg);
//
//            }
//        }.start();


//        Toast.makeText(MainActivity.this, "ok", Toast.LENGTH_LONG).show();
//
//
    }

    /**
     * 实现ReFreshListView中的刷新数据接口
     */
//    @Override
//    public void onRefresh() {
//
//    }
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
        for (int i = 0; i < 4; i++) {
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
