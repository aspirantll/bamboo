package com.flushest.bamboo.crawler.core.process;

import com.flushest.bamboo.crawler.core.ThreadLocalManager;
import com.flushest.bamboo.framework.util.Assert;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Created by Administrator on 2017/12/11 0011.
 */
public class DynamicTextProcedure extends DynamicProcedure {
    private String fieldName;
    public DynamicTextProcedure(String fieldName, String selector) {
        super(selector, ElementSelector.StrictLevel.UNIQUE);
        Assert.notHasText(fieldName,"fieldName must be not null");
        this.fieldName = fieldName;
    }

    @Override
    public boolean execute(HtmlPage page) {
        DomElement element = getElement(page);
        ThreadLocalManager.contextThreadLocalManager.get().put(fieldName, element.asText());
        return true;
    }
}
