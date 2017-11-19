package com.flushest.bamboo.crawler.core.fetcher;

import com.flushest.bamboo.crawler.core.CrawlConfig;

/**
 * Created by Administrator on 2017/11/16 0016.
 */
public abstract class AbstractFetcher<T> implements Fetcher<T> {
    private CrawlConfig crawlConfig;

    public AbstractFetcher(CrawlConfig crawlConfig) {
        this.crawlConfig = crawlConfig;
    }

    @Override
    public CrawlConfig getConfig() {
        return crawlConfig;
    }
}
