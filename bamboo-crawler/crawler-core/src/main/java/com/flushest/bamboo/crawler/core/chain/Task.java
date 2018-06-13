package com.flushest.bamboo.crawler.core.chain;

import com.flushest.bamboo.crawler.core.context.CrawlConfig;
import com.flushest.bamboo.crawler.core.frontier.ResourceManagerFactory;
import com.flushest.bamboo.crawler.core.process.Procedure;
import com.flushest.bamboo.crawler.core.worker.DynamicWorker;
import com.flushest.bamboo.crawler.core.worker.StaticWorker;
import com.flushest.bamboo.crawler.core.worker.TaskMonitor;
import com.flushest.bamboo.framework.resource.WebURL;
import com.flushest.bamboo.framework.thread.TerminationToken;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2018/1/15 0015.
 */
@Getter
@Setter
public class Task {

    private String id;

    private String name;

    private String desc;

    private Chain<Procedure> dynamicChain;

    private Chain<Procedure> staticChain;

    private TerminationToken terminationToken;

    private CrawlConfig crawlConfig;

    private List<DynamicWorker> dynamicWorkers;

    private List<StaticWorker> staticWorkers;

    private AtomicInteger activeDynamicWorkerNum;

    private AtomicInteger activeStaticWorkerNum;

    private TaskMonitor taskMonitor;

    private long startTime;

    protected boolean running;

    public Task() {
        terminationToken = new TerminationToken();
        dynamicWorkers = new ArrayList<>();
        staticWorkers = new ArrayList<>();
    }



    public void start() {
        startTime = System.currentTimeMillis();
        taskMonitor = new TaskMonitor(this);
        taskMonitor.start();
        initWebUrls();
        initWorkers();
        running = true;
    }

    public void  initWebUrls() {
        List<String> seeds = crawlConfig.getSeeds();
        if(seeds != null) {
            for(String seed : seeds) {
                WebURL webURL = new WebURL(seed);
                ResourceManagerFactory.getResourceManager(WebURL.class).offer(id, webURL);
            }
        }
    }


    public void shutdown() {
        dynamicWorkers.get(0).terminate();
    }

    public void finishedCount(int type) {
        if(type == 0) {
            getActiveDynamicWorkerNum().decrementAndGet();
        }else {
            getActiveDynamicWorkerNum().decrementAndGet();
        }
    }

    public boolean isFinished() {
        return activeDynamicWorkerNum.get()==0 && activeDynamicWorkerNum.get() == activeStaticWorkerNum.get();
    }

    public void initWorkers() {

        dynamicWorkers.clear();
        int dynamicWorkerNum = crawlConfig.getDynamicWorkerNum();

        for(int i=0; i<dynamicWorkerNum; i++) {
            DynamicWorker dynamicWorker = new DynamicWorker(this);
            dynamicWorkers.add(dynamicWorker);
            dynamicWorker.start();
        }
        activeDynamicWorkerNum = new AtomicInteger(dynamicWorkerNum);

        int staticWorkerNum = crawlConfig.getStaticWorkerNum();
        for(int i=0; i<staticWorkerNum; i++) {
            StaticWorker staticWorker = new StaticWorker(this);
            staticWorkers.add(staticWorker);
            staticWorker.start();
        }

        activeStaticWorkerNum = new AtomicInteger(staticWorkerNum);
    }

}
