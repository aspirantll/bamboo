package com.flushest.bamboo.crawler.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2017/12/9 0009.
 * 爬取控制器
 */
public class CrawlController {

    private static final Logger logger = LoggerFactory.getLogger(CrawlController.class);

    protected CrawlConfig crawlConfig;

    protected boolean finished;

    protected boolean shutdown;



    public CrawlController(CrawlConfig crawlConfig) {
        crawlConfig.validate();
        this.crawlConfig = crawlConfig;
    }

    public void afterProperties() {

    }
}
