package com.flushest.bamboo.crawler.core.process.statics;

import com.flushest.bamboo.crawler.core.context.CrawContext;
import com.flushest.bamboo.crawler.core.context.StaticContext;
import com.flushest.bamboo.crawler.core.process.ElementSelector;
import com.flushest.bamboo.crawler.core.process.Procedure;
import lombok.Setter;
import org.jsoup.nodes.Element;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/11/19 0019.
 */
public abstract class StaticProcedure implements Procedure<StaticContext> {

    @Setter
    protected String selector;
    private StaticElementSelector elementSelector;

    public void afterProperties() {
        elementSelector = new StaticElementSelector(selector);
    }

    protected Element getElement(StaticContext staticContext) {
        return elementSelector.getElement(staticContext);
    }

    private class StaticElementSelector extends ElementSelector<Element,StaticContext> {

        public StaticElementSelector(String selector) {
            super(selector);
        }

        @Override
        public List<Element> getElements(StaticContext staticContext) {
            Element element = staticContext.getElementBySelector(selectorParser);
            return Arrays.asList(element);
        }
    }
}
