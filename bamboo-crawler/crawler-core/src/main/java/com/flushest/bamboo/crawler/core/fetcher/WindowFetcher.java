package com.flushest.bamboo.crawler.core.fetcher;

import com.flushest.bamboo.crawler.core.CrawlConfig;
import com.flushest.bamboo.crawler.core.url.WebURL;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;

/**
 * Created by Administrator on 2017/11/12 0012.
 */
public class WindowFetcher extends AbstractFetcher<WebWindow> {

    private WebClient webClient;


    public WindowFetcher(CrawlConfig crawlConfig) {
        super(crawlConfig);
        webClient = new WebClient(BrowserVersion.FIREFOX_52,crawlConfig.getProxyHost(),crawlConfig.getProxyPort());
    }



    @Override
    public WebWindow fetch(WebURL webURL) {
        return webClient.openWindow(webURL.createURL(),webURL.getUrl());
    }
}
