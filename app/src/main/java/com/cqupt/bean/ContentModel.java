package com.cqupt.bean;

/**
 * ===================================================================
 * <p/>
 * 版权：重庆邮电大学教育集团 版权所有 （c）  2015
 * <p/>
 * 作者：小甜甜
 * <p/>
 * 版本：1.0
 * <p/>
 * 创建日期：2015/7/8 12:50
 * <p/>
 * 描述：
 * <p/>
 * <p/>
 * 修订历史：
 * <p/>
 * ===================================================================
 */
public class ContentModel {
    private int imageView;
    private String text;

    public ContentModel(int imageView, String text) {
        super();
        this.imageView = imageView;
        this.text = text;
    }

    public int getImageView() {
        return imageView;
    }

    public void setImageView(int imageView) {
        this.imageView = imageView;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
