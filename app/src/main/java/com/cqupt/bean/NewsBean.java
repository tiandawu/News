package com.cqupt.bean;

import java.io.Serializable;

/**
 * ===================================================================
 * <p/>
 * 版权：重庆邮电大学教育集团 版权所有 （c）  2015
 * <p/>
 * 作者：小甜甜
 * <p/>
 * 版本：1.0
 * <p/>
 * 创建日期：2015/7/3 17:50
 * <p/>
 * 描述：
 * 新闻的实体类
 * <p/>
 * <p/>
 * 修订历史：
 * <p/>
 * ===================================================================
 */
public class NewsBean implements Serializable {
    private String title;
    private String imageUrl;
    private String contentUrl;
    //    private String briefContent;
    private String publishTime;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

//    public String getBriefContent() {
//        return briefContent;
//    }

//    public void setBriefContent(String briefContent) {
//        this.briefContent = briefContent;
//    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    @Override
    public String toString() {
        return "NewsBean{" +
                "title='" + title + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", contentUrl='" + contentUrl + '\'' +
//                ", briefContent='" + briefContent + '\'' +
                '}';
    }
}
