package com.flushest.bamboo.crawler.core;

/**
 * Created by Administrator on 2017/11/12 0012.
 */
public class CrawlConfig {

    //代理主机
    private String proxyHost;
    //代理端口
    private int proxyPort;
    //延迟时间
    private int politenessDelay = 200;
    //是否加载JS引擎
    private boolean loadJsEngine = true;


    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public int getPolitenessDelay() {
        return politenessDelay;
    }

    public void setPolitenessDelay(int politenessDelay) {
        this.politenessDelay = politenessDelay;
    }
}
