package com.flushest.bamboo.crawler.core.fetcher;

import com.flushest.bamboo.common.framework.exception.BambooRuntimeException;
import com.flushest.bamboo.crawler.core.ThreadLocalManager;
import com.flushest.bamboo.crawler.core.constant.FieldName;
import com.flushest.bamboo.crawler.core.context.CrawlConfig;
import com.flushest.bamboo.framework.resource.WebURL;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2017/11/12 0012.
 */
public class WindowFetcher extends AbstractFetcher<WebWindow> {
    private WebClient webClient;
    private Long createTime;


    public WindowFetcher(CrawlConfig crawlConfig) {
        super(crawlConfig);
        doRefresh();
    }

    private void doRefresh() {
        CrawlConfig crawlConfig = getConfig();
        if(crawlConfig.getProxyHost()!=null && crawlConfig.getProxyPort()!=null) {
            webClient = new WebClient(BrowserVersion.FIREFOX_52,crawlConfig.getProxyHost(),crawlConfig.getProxyPort());
        }else {
            webClient = new WebClient(BrowserVersion.FIREFOX_52);
        }

        webClient.getOptions().setJavaScriptEnabled(crawlConfig.isLoadJsEngine());
        webClient.getOptions().setCssEnabled(crawlConfig.isLoadCssEngine());
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        createTime = System.currentTimeMillis();
    }

    public WebWindow refresh() {
        Long interval = getConfig().getClientInterval();
        if(interval != null && System.currentTimeMillis()-createTime >= interval) {
            close();
            doRefresh();
            String url = (String) ThreadLocalManager.contextThreadLocalManager.get().get(FieldName.CURRENT_URL);
            return fetch(new WebURL(url));
        }
        return webClient.getCurrentWindow();
    }

    public void close() {
        webClient.getCurrentWindow().getJobManager().removeAllJobs();
        webClient.close();
        System.gc();
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
