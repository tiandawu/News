package com.cqupt.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqupt.bean.NewsBean;
import com.cqupt.listview.ReFreshListView;
import com.cqupt.news.R;
import com.cqupt.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * ===================================================================
 * <p/>
 * 版权：重庆邮电大学教育集团 版权所有 （c）  2015
 * <p/>
 * 作者：小甜甜
 * <p/>
 * 版本：1.0
 * <p/>
 * 创建日期：2015/7/5 15:42
 * <p/>
 * 描述：
 * 自定义ListView适配器
 * <p/>
 * <p/>
 * 修订历史：
 * <p/>
 * ===================================================================
 */
public class NewsAdapter extends BaseAdapter {
    private List<NewsBean> mList;
    private LayoutInflater mInflater;
    public static ImageLoader mImageLoader;
    public static String[] URLS;

    public NewsAdapter(Context context, List<NewsBean> data, ReFreshListView listView) {
        mList = data;
        mInflater = LayoutInflater.from(context);
        mImageLoader = new ImageLoader(listView);
        URLS = new String[data.size()];
        for (int i = 0; i < data.size(); i++) {
            URLS[i] = data.get(i).getImageUrl();
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.list_item, null);
            viewHolder.title = (TextView) view.findViewById(R.id.tv_title);
            viewHolder.publishTime = (TextView) view.findViewById(R.id.tv_publish_time);
            viewHolder.image = (ImageView) view.findViewById(R.id.iv_icon);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.image.setImageResource(R.mipmap.ic_launcher);
        String url = mList.get(i).getImageUrl();
        viewHolder.image.setTag(url);
//        Log.i("tag===", url);
//                mImageLoader.showImageByThread(viewHolder.image, url);
        mImageLoader.showImageByAsyncTask(viewHolder.image, url);
        viewHolder.title.setText(mList.get(i).getTitle());
        viewHolder.publishTime.setText(mList.get(i).getPublishTime());
        return view;
    }

    class ViewHolder {
        public TextView title;
        public ImageView image;
        public TextView publishTime;
    }


//    private List<NewsBean> mList;
//    private LayoutInflater inflater;
//
//    public NewsAdapter(List<NewsBean> mList, Context context,ReFreshListView listView) {
//        this.mList = mList;
//        this.inflater = LayoutInflater.from(context);
//    }
//
//    @Override
//    public int getCount() {
//        return mList.size();
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return mList.get(i);
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return i;
//    }
//
//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//        view = inflater.inflate(R.layout.list_item, null);
//        TextView textView = (TextView) view.findViewById(R.id.tv_title);
//        ImageView imageView = (ImageView) view.findViewById(R.id.iv_icon);
//        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);
//
//
//        textView.setText(mList.get(i).getTitle());
//        imageView.setImageResource(R.mipmap.ic_launcher);
//        tvContent.setText(mList.get(i).getPublishTime());
//        return view;
//    }
//
//
//    public void onDataChanged(ArrayList<NewsBean> mList) {
//        this.mList = mList;
//        this.notifyDataSetChanged();
//    }
}
