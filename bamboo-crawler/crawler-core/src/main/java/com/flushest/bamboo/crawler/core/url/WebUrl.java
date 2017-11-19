package com.flushest.bamboo.crawler.core.url;

import com.flushest.bamboo.crawler.core.enums.Protocol;
import com.flushest.bamboo.common.crawler.exception.IllegalUrlException;
import com.flushest.bamboo.framework.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2017/11/12 0012.
 */
public class WebURL implements Serializable{
    private static final Logger logger = LoggerFactory.getLogger(WebURL.class);

    private URLParser urlParser;
    private Protocol protocol;
    private URL url;

    public WebURL(String url) {
        Assert.notHasText(url,"url must be not null");
        this.urlParser = new URLParser(url);
        this.protocol = Protocol.protocol(url);
    }

    private void init() {
        try {
            url = new URL(getUrl());
        } catch (MalformedURLException e) {
            logger.error("URL格式有误",e);
            throw new IllegalUrlException(getUrl());
        }
    }

    public URL createURL() {
        return url;
    }

    public String getUrl() {
        return urlParser.getUrl();
    }

    public URLParser getUrlParser() {
        return urlParser;
    }

    public Protocol getProtocol() {
        return protocol;
    }
}