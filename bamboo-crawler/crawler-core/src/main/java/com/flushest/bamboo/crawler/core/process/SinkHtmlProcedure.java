package com.flushest.bamboo.crawler.core.process;

import com.flushest.bamboo.crawler.core.context.Page;
import com.flushest.bamboo.crawler.core.frontier.ResourceManager;
import com.flushest.bamboo.crawler.core.frontier.ResourceManagerFactory;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.List;

/**
 * Created by Administrator on 2018/1/17 0017.
 */
public class SinkHtmlProcedure extends DynamicProcedure {

    private String taskId;

    protected SinkHtmlProcedure(String selector, String taskId) {
        super(selector, ElementSelector.StrictLevel.NONE);
        this.taskId = taskId;
    }

    @Override
    protected boolean execute(HtmlPage page) {
        List<DomElement> elements = getElements(page);
        ResourceManager<Page> resourceManager = ResourceManagerFactory.getResourceManager(Page.class);
        elements.forEach((element)->{
            resourceManager.offer(taskId, element.asXml());
        });
        return true;
    }
}
