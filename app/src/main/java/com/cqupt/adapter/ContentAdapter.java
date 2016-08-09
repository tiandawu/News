package com.cqupt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqupt.bean.ContentModel;
import com.cqupt.news.R;

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
 * 创建日期：2015/7/8 12:51
 * <p/>
 * 描述：
 * <p/>
 * <p/>
 * 修订历史：
 * <p/>
 * ===================================================================
 */
public class ContentAdapter extends BaseAdapter {

    private Context context;
    private List<ContentModel> list;

    public ContentAdapter(Context context, List<ContentModel> list) {
        super();
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (list != null) {
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHold hold;
        if (convertView == null) {
            hold = new ViewHold();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.content_item, null);
            convertView.setTag(hold);
        }else {
            hold=(ViewHold) convertView.getTag();
        }

        hold.imageView=(ImageView) convertView.findViewById(R.id.item_imageview);
        hold.textView=(TextView) convertView.findViewById(R.id.item_textview);

        hold.imageView.setImageResource(list.get(position).getImageView());
        hold.textView.setText(list.get(position).getText());
        return convertView;
    }

    static class ViewHold {
        public ImageView imageView;
        public TextView textView;
    }

}
