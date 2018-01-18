package com.flushest.bamboo.crawler.core.worker;

import com.flushest.bamboo.crawler.core.ThreadLocalManager;
import com.flushest.bamboo.crawler.core.context.CrawContext;
import com.flushest.bamboo.crawler.core.context.CrawlConfig;
import com.flushest.bamboo.crawler.core.chain.Chain;
import com.flushest.bamboo.crawler.core.chain.Task;
import com.flushest.bamboo.crawler.core.fetcher.WindowFetcher;
import com.flushest.bamboo.crawler.core.frontier.ResourceManager;
import com.flushest.bamboo.crawler.core.frontier.ResourceManagerFactory;
import com.flushest.bamboo.crawler.core.process.Procedure;
import com.flushest.bamboo.framework.resource.WebURL;
import com.flushest.bamboo.framework.thread.AbstractTerminableThread;
import com.gargoylesoftware.htmlunit.WebWindow;

/**
 * Created by Administrator on 2018/1/11 0011.
 */
public class DynamicWorker extends AbstractTerminableThread{

    private ResourceManager<WebURL> resourceManager;

    private WindowFetcher windowFetcher;

    private Task task;

    public DynamicWorker(Task task) {
        this.task = task;
        this.terminationToken = task.getTerminationToken();
        this.windowFetcher = new WindowFetcher(task.getCrawlConfig());
        this.resourceManager = ResourceManagerFactory.getResourceManager(WebURL.class);
    }

    @Override
    protected void doRun() throws Exception {
        ThreadLocalManager.contextThreadLocalManager.set(new CrawContext(windowFetcher.getConfig(), task));;

        WebURL url = resourceManager.accept(task.getTaskId());
        WebWindow webWindow = windowFetcher.fetch(url);
        boolean isLeft;

        Chain<Procedure> dynamicProcedureChain = task.getDynamicChain();
        do{
            Procedure<WebWindow> procedure = dynamicProcedureChain.current();
            isLeft = procedure.process(webWindow);
        }while (dynamicProcedureChain.hasMore(isLeft));

        ThreadLocalManager.contextThreadLocalManager.clear();
    }

    @Override
    protected void doCleanup(Exception cause) {
        task.finishedCount(0);
    }

}
