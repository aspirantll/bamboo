package com.flushest.bamboo.crawler.core.frontier;

import com.flushest.bamboo.framework.resource.WebURL;

/**
 * Created by Administrator on 2018/1/10 0010.
 */
public class WebUrlResourceManager extends AbstractResourceManager<WebURL> {

    @Override
    public boolean offer(String key, String url) {
        WebURL webURL = new WebURL(url);
        return offer(key, webURL);
    }
}
