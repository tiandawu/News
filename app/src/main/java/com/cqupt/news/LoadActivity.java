package com.cqupt.news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.cqupt.bean.NewsBean;
import com.cqupt.utils.HtmlUtils;

import java.io.Serializable;
import java.util.List;


public class LoadActivity extends Activity {
    private List<NewsBean> mList = null;
    private static final String htmlUrl = "http://www.dz.tt/news.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);


        //在加载启动界面的时候获取数据
        new Thread() {
            @Override
            public void run() {
                super.run();

                try {
                    mList = HtmlUtils.htmlParseToNewsInfo(htmlUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();


//        SharedPreferences preferences = getSharedPreferences("isFistIn", MODE_PRIVATE);
//
//        boolean isFirstIn = preferences.getBoolean("isFistIn", true);
//        Toast.makeText(LoadActivity.this, "值；=" + isFirstIn, Toast.LENGTH_SHORT).show();
//        if (isFirstIn == true) {
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putBoolean("isFistIn", false);
//            editor.commit();
//        }


        //启动界面
        new Handler().postDelayed(new Runnable() {
            public void run() {
                /* Create an Intent that will start the Main WordPress Activity. */
                Intent mainIntent = new Intent(LoadActivity.this, MainActivity.class);
                mainIntent.putExtra("mList", (Serializable) mList);
                LoadActivity.this.startActivity(mainIntent);
                LoadActivity.this.finish();
            }
        }, 3000);
    }

}
