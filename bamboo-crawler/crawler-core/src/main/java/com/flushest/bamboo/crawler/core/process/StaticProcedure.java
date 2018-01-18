package com.flushest.bamboo.crawler.core.process;

import com.flushest.bamboo.crawler.core.context.CrawContext;
import com.flushest.bamboo.crawler.core.context.StaticContext;
import org.jsoup.nodes.Element;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/11/19 0019.
 */
public abstract class StaticProcedure implements Procedure<StaticContext> {

    protected String selector;
    private StaticElementSelector elementSelector;

    protected StaticProcedure() {

    }

    protected StaticProcedure(String selector) {
        this.selector = selector;
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
