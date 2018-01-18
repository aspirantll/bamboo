package com.flushest.bamboo.crawler.core.worker;

import com.flushest.bamboo.crawler.core.context.Page;
import com.flushest.bamboo.crawler.core.ThreadLocalManager;
import com.flushest.bamboo.crawler.core.chain.Chain;
import com.flushest.bamboo.crawler.core.chain.Task;
import com.flushest.bamboo.crawler.core.context.CrawContext;
import com.flushest.bamboo.crawler.core.context.StaticContext;
import com.flushest.bamboo.crawler.core.frontier.ResourceManager;
import com.flushest.bamboo.crawler.core.frontier.ResourceManagerFactory;
import com.flushest.bamboo.crawler.core.process.Procedure;
import com.flushest.bamboo.framework.thread.AbstractTerminableThread;

/**
 * Created by Administrator on 2018/1/15 0015.
 */
public class StaticWorker extends AbstractTerminableThread {

    private ResourceManager<Page> resourceManager;

    private Task task;

    public StaticWorker(Task task) {
        this.task = task;
        this.terminationToken = task.getTerminationToken();
        this.resourceManager = ResourceManagerFactory.getResourceManager(Page.class);
    }

    @Override
    protected void doRun() throws Exception {
        ThreadLocalManager.contextThreadLocalManager.set(new CrawContext(task.getCrawlConfig(), task));;

        Page page = resourceManager.accept(task.getTaskId());
        StaticContext staticContext = new StaticContext(page);

        boolean isLeft;

        Chain<Procedure> dynamicProcedureChain = task.getDynamicChain();
        do{
            Procedure<StaticContext> procedure = dynamicProcedureChain.current();
            isLeft = procedure.process(staticContext);
        }while (dynamicProcedureChain.hasMore(isLeft));

        ThreadLocalManager.contextThreadLocalManager.clear();
    }

    @Override
    protected void doCleanup(Exception cause) {
        task.finishedCount(1);
    }

}
