package com.cqupt.news;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cqupt.adapter.ContentAdapter;
import com.cqupt.adapter.NewsAdapter;
import com.cqupt.bean.ContentModel;
import com.cqupt.bean.NewsBean;
import com.cqupt.listview.ReFreshListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity implements ReFreshListView.IRefreshListener {

    private DrawerLayout drawerLayout;
    private RelativeLayout leftLayout;
    private RelativeLayout rightLayout;
    private List<ContentModel> list;
    private ContentAdapter adapter;


    //是否退出程序
    private static boolean isExit = false;
    //定时促发器
    private static Timer mTimer = null;
    private ReFreshListView mListView;
//    private static final String htmlUrl = "http://www.dz.tt/news.html";

    private List<NewsBean> mList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isNetworkAvailable()) {
            Toast.makeText(getApplicationContext(), "请检查网络！", Toast.LENGTH_LONG).show();
            return;
        }


        mListView = (ReFreshListView) findViewById(R.id.list_view);


        mList = (ArrayList<NewsBean>) getIntent().getSerializableExtra("mList");
        new NewsAsyncTask().execute();


        mListView.setInterface(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ItemActivity.class);
                intent.putExtra("contentUrl", mList.get(position - 1).getContentUrl());
                startActivity(intent);
            }
        });


        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        leftLayout = (RelativeLayout) findViewById(R.id.left);
        rightLayout = (RelativeLayout) findViewById(R.id.right);
        ListView listView = (ListView) leftLayout.findViewById(R.id.left_listview);
        initData();
        adapter = new ContentAdapter(this, list);
        listView.setAdapter(adapter);

    }

    /**
     * 检查当前网络是否可用
     */

    public boolean isNetworkAvailable() {
        Context context = MainActivity.this.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    private void initData() {
        list = new ArrayList<>();
        list.add(new ContentModel(R.mipmap.doctoradvice2, "新闻"));
        list.add(new ContentModel(R.mipmap.infusion_selected, "订阅"));
        list.add(new ContentModel(R.mipmap.mypatient_selected, "图片"));
        list.add(new ContentModel(R.mipmap.mywork_selected, "视频"));
        list.add(new ContentModel(R.mipmap.nursingcareplan2, "跟帖"));
        list.add(new ContentModel(R.mipmap.personal_selected, "投票"));
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

    /**
     * 设置刷新数据
     */
    public void setRefreshData() {
        for (int i = 0; i < 1; i++) {
            NewsBean newsBean = new NewsBean();
            newsBean.setImageUrl("http://img.lesu.cc/201410/amcdz/upload/images20150703/e468ff62bc8b62eec221de4addc86ec9.jpg");
            newsBean.setTitle("wo 是新数据" + i);
            newsBean.setPublishTime(i + "");
            mList.add(0, newsBean);
        }
    }


    /**
     * 实现网络的异步访问，获取html页面的数据
     */
    class NewsAsyncTask extends AsyncTask<Void, Void, List<NewsBean>> {

//        @Override
//        protected List<NewsBean> doInBackground(String... strings) {
//            try {
//                mList = HtmlUtils.htmlParseToNewsInfo(strings[0]);
//            } catch (Exception e) {
//                e.printStackTrace();
//                Toast.makeText(MainActivity.this, "请检查网络!", Toast.LENGTH_SHORT).show();
//            }
//            return mList;
//        }

        @Override
        protected List<NewsBean> doInBackground(Void... voids) {
            return mList;
        }

        @Override
        protected void onPostExecute(List<NewsBean> newsBeans) {
            super.onPostExecute(newsBeans);
            NewsAdapter adapter = new NewsAdapter(MainActivity.this, newsBeans, mListView);
            mListView.setAdapter(adapter);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
