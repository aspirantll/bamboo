package com.flushest.bamboo.crawler.core.worker;

import com.flushest.bamboo.crawler.core.frontier.ResourceManager;
import com.flushest.bamboo.crawler.core.frontier.ResourceManagerFactory;
import com.flushest.bamboo.framework.resource.WebURL;
import com.flushest.bamboo.framework.thread.AbstractTerminableThread;

/**
 * Created by Administrator on 2018/1/11 0011.
 */
public class DynamicWorker extends AbstractTerminableThread{

    private ResourceManager<WebURL> resourceManager;

    public DynamicWorker() {
        resourceManager = ResourceManagerFactory.getResourceManager(WebURL.class);
    }

    @Override
    protected void doRun() throws Exception {

    }


}
