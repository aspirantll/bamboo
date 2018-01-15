package com.flushest.bamboo.crawler.core;

import com.flushest.bamboo.common.framework.exception.BambooRuntimeException;
import com.flushest.bamboo.crawler.core.process.ElementSelector;
import com.flushest.bamboo.framework.util.StringUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
