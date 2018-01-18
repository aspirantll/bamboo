package com.flushest.bamboo.framework.resource;

import com.flushest.bamboo.framework.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/12 0012.
 */
public class WebURL implements Serializable{
    private static final Logger logger = LoggerFactory.getLogger(WebURL.class);

    private URLParser urlParser;
    private String protocol;

    public WebURL(String url) {
        Assert.notHasText(url,"url must be not null");
        this.urlParser = new URLParser(url);
        this.protocol = urlParser.getProtocol();
    }

    public String getUrl() {
        return urlParser.getUrl();
    }

    public URLParser getUrlParser() {
        return urlParser;
    }

    public String getProtocol() {
        return protocol;
    }

    @Override
    public String toString() {
        return getUrl();
    }
}