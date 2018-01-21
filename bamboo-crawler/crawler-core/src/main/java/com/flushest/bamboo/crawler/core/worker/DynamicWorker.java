package com.flushest.bamboo.crawler.core.worker;

import com.flushest.bamboo.crawler.core.ThreadLocalManager;
import com.flushest.bamboo.crawler.core.context.CrawContext;
import com.flushest.bamboo.crawler.core.context.CrawlConfig;
import com.flushest.bamboo.crawler.core.chain.Chain;
import com.flushest.bamboo.crawler.core.chain.Task;
import com.flushest.bamboo.crawler.core.context.DynamicContext;
import com.flushest.bamboo.crawler.core.fetcher.WindowFetcher;
import com.flushest.bamboo.crawler.core.frontier.ResourceManager;
import com.flushest.bamboo.crawler.core.frontier.ResourceManagerFactory;
import com.flushest.bamboo.crawler.core.process.Procedure;
import com.flushest.bamboo.framework.listener.EventPublisher;
import com.flushest.bamboo.framework.resource.WebURL;
import com.flushest.bamboo.framework.thread.AbstractTerminableThread;

/**
 * Created by Administrator on 2018/1/11 0011.
 */
public class DynamicWorker extends AbstractTerminableThread implements EventPublisher {

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
    public void doRun() throws Exception {
        ThreadLocalManager.contextThreadLocalManager.set(new CrawContext(windowFetcher.getConfig(), task));

        WebURL url = getUrl();

        if (url == null) {
            this.terminate();
            return;
        }

        DynamicContext dynamicContext = new DynamicContext(windowFetcher.fetch(url));
        try {
            boolean isLeft;

            Chain<Procedure> dynamicProcedureChain = task.getDynamicChain();
            dynamicProcedureChain.reset();
            do{
                Procedure<DynamicContext> procedure = dynamicProcedureChain.current();
                isLeft = procedure.process(dynamicContext);
                dynamicContext.setWebWindow(windowFetcher.refresh());
            }while (dynamicProcedureChain.hasMore(isLeft) && dynamicProcedureChain.next(isLeft)!=null);
        }finally {
            ThreadLocalManager.contextThreadLocalManager.clear();
        }


    }

    private WebURL getUrl() throws InterruptedException {
        CrawlConfig config = task.getCrawlConfig();
        if(config.getMaxWaitTime() != null) {
            return resourceManager.accept(task.getTaskId(), config.getMaxWaitTime(), config.getWaitTimeUnit());
        }else {
            return resourceManager.accept(task.getTaskId());
        }
    }

    @Override
    protected void doCleanup(Exception cause) {
        windowFetcher.close();
    }

}
