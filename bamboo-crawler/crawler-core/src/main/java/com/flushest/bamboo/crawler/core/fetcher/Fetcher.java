package com.flushest.bamboo.crawler.core.fetcher;

import com.flushest.bamboo.common.Configurable;
import com.flushest.bamboo.crawler.core.CrawlConfig;
import com.flushest.bamboo.framework.resource.WebURL;

/**
 * Created by Administrator on 2017/11/12 0012.
 */
public interface Fetcher<T> extends Configurable<CrawlConfig> {
    T fetch(WebURL webURL);
}
