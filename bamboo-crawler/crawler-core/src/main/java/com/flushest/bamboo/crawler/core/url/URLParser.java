package com.flushest.bamboo.crawler.core.url;

import com.flushest.bamboo.framework.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2017/11/12 0012.
 */
public class URLParser {
    private static final Logger logger = LoggerFactory.getLogger(URLParser.class);

    private String url;

    public static String getProtocol(String url) {
        Assert.notHasText(url,"url must be not null");
        int protocolEndIndex = url.indexOf("://");
        Assert.judge(protocolEndIndex == -1,"can not find protocol from url:"+url);
        return url.substring(0,protocolEndIndex).toLowerCase();
    }

    public URLParser(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
