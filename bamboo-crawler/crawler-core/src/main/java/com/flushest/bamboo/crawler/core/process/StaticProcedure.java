package com.flushest.bamboo.crawler.core.process;

import com.flushest.bamboo.crawler.core.Context;
import com.flushest.bamboo.crawler.core.Page;
import org.jsoup.nodes.Element;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/11/19 0019.
 */
public abstract class StaticProcedure implements Procedure<Context> {

    protected String selector;
    private StaticElementSelector elementSelector;

    protected StaticProcedure(String selector) {
        this.selector = selector;
        elementSelector = new StaticElementSelector(selector);
    }

    protected Element getElement(Context context) {
        return elementSelector.getElement(context);
    }

    private class StaticElementSelector extends ElementSelector<Element,Context> {

        public StaticElementSelector(String selector) {
            super(selector);
        }

        @Override
        public List<Element> getElements(Context context) {
            Element element = context.getElementBySelector(selectorParser);
            return Arrays.asList(element);
        }
    }
}
