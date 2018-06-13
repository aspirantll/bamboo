package com.flushest.bamboo.crawler.core.context;

import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/11/12 0012.
 */
@Setter
public class CrawlConfig {

    //代理主机
    private String proxyHost;
    //代理端口
    private Integer proxyPort;
    //延迟时间
    private int politenessDelay = 200;
    //是否加载JS引擎
    private boolean loadJsEngine = true;
    //是否加载css
    private boolean loadCssEngine = true;
    //文件目录
    private String directory;
    //动态工作者数量
    private Integer dynamicWorkerNum;
    //静态工作者数量
    private Integer staticWorkerNum;
    //静态解析是否迭代当前节点
    private boolean isIterable;
    //等待时间
    private Long maxWaitTime;
    //替换webClient时间
    private Long clientInterval;
    //等待时间单位
    private TimeUnit waitTimeUnit;
    //初始化seedUrl
    private List<String> seeds = new ArrayList<>();

    public String getProxyHost() {
        return proxyHost;
    }

    public Integer getProxyPort() {
        return proxyPort;
    }

    public int getPolitenessDelay() {
        return politenessDelay;
    }

    public boolean isLoadJsEngine() {
        return loadJsEngine;
    }

    public String getDirectory() {
        return directory;
    }

    public Integer getDynamicWorkerNum() {
        return dynamicWorkerNum==null||dynamicWorkerNum<0? 1 : dynamicWorkerNum;
    }

    public Integer getStaticWorkerNum() {
        return staticWorkerNum==null||staticWorkerNum<0? 1 : staticWorkerNum;
    }

    public Long getMaxWaitTime() {
        return maxWaitTime;
    }

    public Long getClientInterval() {
        return clientInterval;
    }

    public TimeUnit getWaitTimeUnit() {
        return waitTimeUnit==null? TimeUnit.MILLISECONDS:waitTimeUnit;
    }

    public boolean isIterable() {
        return isIterable;
    }

    public boolean isLoadCssEngine() {
        return loadCssEngine;
    }

    public List<String> getSeeds() {
        return seeds;
    }

    public void addSeeds(String... seeds) {
        for(String seed : seeds) {
            this.seeds.add(seed);
        }
    }
    public void setSeeds(String seeds) {
        for(String seed : seeds.split(",")) {
            this.seeds.add(seed);
        }
    }

    public boolean validate() {
        return true;
    }
}
