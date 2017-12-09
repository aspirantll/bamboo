package com.flushest.bamboo.crawler.core;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by Administrator on 2017/11/12 0012.
 */
public class Page {
    private String html;

    public Page(String html) {
        this.html = html;
    }

    public String getHtml() {
        return html;
    }

    public Document parseByJSoup() {
        return Jsoup.parse(html);
    }
}
