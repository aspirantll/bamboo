package com.flushest.bamboo.crawler.core.process.dynamics;

import com.flushest.bamboo.crawler.core.ThreadLocalManager;
import com.flushest.bamboo.crawler.core.process.ElementSelector;
import com.flushest.bamboo.framework.util.Assert;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.Setter;

/**
 * Created by Administrator on 2017/12/11 0011.
 */
public class DynamicTextProcedure extends DynamicProcedure {
    @Setter
    private String fieldName;

    @Override
    public void afterProperties() {
        super.afterProperties();
        Assert.notHasText(fieldName,"fieldName must be not null");
    }

    @Override
    public boolean execute(HtmlPage page) {
        DomElement element = getElement(page);
        ThreadLocalManager.contextThreadLocalManager.get().put(fieldName, element.asText());
        return true;
    }
}
