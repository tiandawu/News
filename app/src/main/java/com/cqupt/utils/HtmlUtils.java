package com.cqupt.utils;

import android.util.Log;

import com.cqupt.bean.NewsBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * html工具类
 */
public class HtmlUtils {
    /**
     * 从html页面的内容中获取需要的信息：标题，内容概要，图片url，详细页面链接
     *
     * @return 封装的java bean集合！
     */
    public static List<NewsBean> htmlParseToNewsInfo(String htmlUrl) throws Exception {

        //获取HTML页面
        Document doc = Jsoup.connect(htmlUrl).get();
        //获取class="sliderbox"的html页面内容
        Elements sliderbox = doc.getElementsByClass("sliderbox");
        //获取a标签的内容
        Elements links = sliderbox.select("a[href]");
        //创建集合
        List<NewsBean> list = new ArrayList<>();
        for (Element link : links) {
            //创建NewsBean对象，用于封装信息
            NewsBean newsBean = new NewsBean();
            //拼装成完整的url路径
            String contentUrl = "http://www.dz.tt/" + link.select("a").attr("href");
            newsBean.setContentUrl(contentUrl);
            //获取图片的路径
            String imageUrl = link.select("img").attr("src");
            newsBean.setImageUrl(imageUrl);
            //获取标题
            String title = link.getElementsByClass("newstitle").text();
            newsBean.setTitle(title);
            //获取内容概要
//            String briefContent = link.getElementsByClass("newscontent").text();
//            newsBean.setBriefContent(briefContent);
            //获取新闻发布的时间
            String time = link.getElementsByClass("wd").select("span").text();
            newsBean.setPublishTime(time);

            //将封装好的NewsBean放进list集合
            list.add(newsBean);
        }

        return list;
    }
}
