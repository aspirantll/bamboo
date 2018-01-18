package com.flushest.bamboo.crawler.core.context;

import lombok.Builder;
import lombok.Data;

/**
 * Created by Administrator on 2017/11/12 0012.
 */
@Data
@Builder
public class CrawlConfig {

    //代理主机
    private String proxyHost;
    //代理端口
    private int proxyPort;
    //延迟时间
    private int politenessDelay = 200;
    //是否加载JS引擎
    private boolean loadJsEngine = true;
    //文件目录
    private String directory;
    //动态工作者数量
    private Integer dynamicWorkerNum = 1;
    //静态工作者数量
    private Integer staticWorkerNum = 1;


    public boolean validate() {
        return true;
    }
}
