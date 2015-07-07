package com.cqupt.utils;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;
import android.widget.ImageView;

import com.cqupt.adapter.NewsAdapter;
import com.cqupt.listview.ReFreshListView;
import com.cqupt.news.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;


/**
 * ===================================================================
 * <p/>
 * 版权：重庆邮电大学教育集团 版权所有 （c）  2015
 * <p/>
 * 作者：小甜甜
 * <p/>
 * 版本：1.0
 * <p/>
 * 创建日期：2015/7/5 22:06
 * <p/>
 * 描述：
 * 图片加载类，用于加载网络图片
 * <p/>
 * <p/>
 * 修订历史：
 * 用于加载图片
 * <p/>
 * ===================================================================
 */
public class ImageLoader {
    private ImageView mImageView;
    private String mUrl;
    private ReFreshListView mListView;
    private Set<NewsAsyncTask> mTask;


    //创建Cache
    private LruCache<String, Bitmap> mCaches;

    /**
     * 构造函数
     *
     * @param listView
     */
    public ImageLoader(ReFreshListView listView) {
        this.mListView = listView;
        mTask = new HashSet<>();
        //获取运行最大内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 4;
        mCaches = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //在每次存入缓存的时候调用
                return value.getByteCount();
            }
        };
    }

    /**
     * 增加到缓存
     *
     * @param url
     * @param bitmap
     */
    public void addBitmapToCache(String url, Bitmap bitmap) {
        if (getBitmapFromCache(url) == null) {
            mCaches.put(url, bitmap);
        }
    }

    /**
     * 从缓存中获取数据
     *
     * @param url
     * @return
     */
    public Bitmap getBitmapFromCache(String url) {
        return mCaches.get(url);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mImageView.getTag().equals(mUrl)) {
                mImageView.setImageBitmap((Bitmap) msg.obj);
            }
        }
    };

    /**
     * 通过线程显示图片
     *
     * @param imageView
     * @param url
     */
    public void showImageByThread(ImageView imageView, final String url) {

        mImageView = imageView;
        mUrl = url;
        new Thread() {
            @Override
            public void run() {
                super.run();
                Bitmap bitmap = getBitmapFromURL(url);
                Message message = Message.obtain();
                message.obj = bitmap;
                mHandler.sendMessage(message);
            }
        }.start();
    }

    /**
     * 通过图片的url路径获取Bitmap
     *
     * @param urlString
     * @return
     */
    public Bitmap getBitmapFromURL(String urlString) {
        Bitmap bitmap;
        InputStream is = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(conn.getInputStream());
            bitmap = BitmapFactory.decodeStream(is);
            conn.disconnect();
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 异步任务显示图片
     *
     * @param imageView
     * @param url
     */
    public void showImageByAsyncTask(ImageView imageView, String url) {

        //从缓存中取出图片
        Bitmap bitmap = getBitmapFromCache(url);
        //如果缓存中不存在，就必须去下载
        if (bitmap == null) {
            imageView.setImageResource(R.mipmap.ic_launcher);
        } else {
            imageView.setImageBitmap(bitmap);
        }

    }


    /**
     * 用来加载从start到end的所有图片
     */
    public void loadImages(int start, int end) {

        if (NewsAdapter.URLS.length < end) {
            end = NewsAdapter.URLS.length;
        }
        for (int i = start; i < end; i++) {
            String url = NewsAdapter.URLS[i];
            //从缓存中取出图片
            Bitmap bitmap = getBitmapFromCache(url);
            //如果缓存中不存在，就必须去下载
            if (bitmap == null) {
                NewsAsyncTask task = new NewsAsyncTask(url);
                task.execute(url);
                mTask.add(task);
            } else {
                ImageView imageView = (ImageView) mListView.findViewWithTag(url);
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }


    /**
     * 取消加载任务
     */
    public void cancelAllTasks() {
        for (NewsAsyncTask task : mTask) {
            if (mTask != null) {
                task.cancel(false);
            }
        }
    }

    /**
     * 异步加载图片
     */
    private class NewsAsyncTask extends AsyncTask<String, Void, Bitmap> {
        //        private ImageView mImageView;
        private String mUrl;

        public NewsAsyncTask(String url) {
//            mImageView = imageView;
            mUrl = url;
        }


        @Override
        protected Bitmap doInBackground(String... strings) {

            String url = strings[0];
            //从网络获取图片
            Bitmap bitmap = getBitmapFromURL(url);
            if (bitmap != null) {
                //将不在缓存的图片加入缓存
                addBitmapToCache(url, bitmap);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ImageView imageView = (ImageView) mListView.findViewWithTag(mUrl);
            if (imageView != null && bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
            mTask.remove(this);
        }
    }
}
