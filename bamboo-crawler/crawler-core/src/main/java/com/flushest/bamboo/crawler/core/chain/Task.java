package com.flushest.bamboo.crawler.core.chain;

import com.flushest.bamboo.crawler.core.context.CrawContext;
import com.flushest.bamboo.crawler.core.context.CrawlConfig;
import com.flushest.bamboo.crawler.core.process.Procedure;
import com.flushest.bamboo.crawler.core.worker.DynamicWorker;
import com.flushest.bamboo.crawler.core.worker.StaticWorker;
import com.flushest.bamboo.framework.listener.EventSource;
import com.flushest.bamboo.framework.persistence.definitions.TableDefinition;
import com.flushest.bamboo.framework.thread.TerminationToken;
import com.gargoylesoftware.htmlunit.WebWindow;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2018/1/15 0015.
 */
@Getter
public class Task {

    private String taskId;

    private Chain<Procedure> dynamicChain;

    private Chain<Procedure> staticChain;

    private TerminationToken terminationToken;

    private CrawlConfig crawlConfig;

    private List<DynamicWorker> dynamicWorkers;

    private List<StaticWorker> staticWorkers;

    private AtomicInteger activeDynamicWorkerNum;

    private AtomicInteger activeStaticWorkerNum;


    protected boolean running;

    public Task(String taskId,CrawlConfig crawlConfig , Chain<Procedure> dynamicChain, Chain<Procedure> staticChain) {
        this.taskId = taskId;
        this.dynamicChain = dynamicChain;
        this.staticChain = staticChain;
        this.crawlConfig = crawlConfig;
        terminationToken = new TerminationToken();
        dynamicWorkers = new ArrayList<>();
        staticWorkers = new ArrayList<>();
    }

    public void start() {
        initWorkers();
        running = true;
    }

    public void shutdown() {
        dynamicWorkers.get(0).terminate();
    }

    public void finishedCount(int type) {
        if(type == 0) {
            activeDynamicWorkerNum.decrementAndGet();
        }else {
            activeStaticWorkerNum.decrementAndGet();
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
