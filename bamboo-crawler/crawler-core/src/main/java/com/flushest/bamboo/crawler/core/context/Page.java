package com.flushest.bamboo.crawler.core.context;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by Administrator on 2017/11/12 0012.
 */
public class Page {
    private String html;
    private Document document;


    public Page(String html) {
        this.html = html;
        document = Jsoup.parse(html);
    }

    public String getHtml() {
        return html;
    }

    public Document getDocument() {
        return document;
    }



}
