package com.flushest.bamboo.crawler.core.fetcher;

import com.flushest.bamboo.common.framework.exception.BambooRuntimeException;
import com.flushest.bamboo.crawler.core.CrawlConfig;
import com.flushest.bamboo.framework.resource.WebURL;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

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
        URL url = null;
        try {
            url = new URL(webURL.getUrl());
        } catch (MalformedURLException e) {
            throw new BambooRuntimeException("failed to create URL for url:"+webURL,e);
        }
        return webClient.openWindow(url,webURL.getUrl());
    }
}
