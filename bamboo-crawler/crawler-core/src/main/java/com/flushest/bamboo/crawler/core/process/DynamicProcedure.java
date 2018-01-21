package com.flushest.bamboo.crawler.core.process;

import com.flushest.bamboo.common.crawler.exception.UnsupportedPageTypeException;
import com.flushest.bamboo.crawler.core.context.DynamicContext;
import com.flushest.bamboo.framework.util.StringUtil;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2017/11/19 0019.
 */
public abstract class DynamicProcedure implements Procedure<DynamicContext> {

    protected String selector;
    private DynamicElementSelector elementSelector;

    protected DynamicProcedure(String selector, ElementSelector.StrictLevel strictLevel) {
        this.selector = selector;
        elementSelector = new DynamicElementSelector(selector,strictLevel);
    }

    protected abstract boolean execute(HtmlPage page);

    @Override
    public boolean process(DynamicContext context) {
        Page page = context.getWebWindow().getEnclosedPage();
        if(page.isHtmlPage()) {
            return execute((HtmlPage) page);
        }else {
            throw new UnsupportedPageTypeException();
        }
    }

    protected DomElement getElement(HtmlPage page) {
        return elementSelector.getElement(page);
    }

    protected List<DomElement> getElements(HtmlPage page) {
        return elementSelector.getElements(page);
    }

    private class DynamicElementSelector extends ElementSelector<DomElement,HtmlPage> {

        public DynamicElementSelector(String selector) {
            super(selector);
        }

        public DynamicElementSelector(String selector, StrictLevel strictLevel) {
            super(selector,strictLevel);
        }

        @Override
        public List<DomElement> getElements(HtmlPage page) {
            List<DomElement> elements = Collections.EMPTY_LIST;
            String xPath = selectorParser.getxPath();
            final Map<String,String> attr = selectorParser.getAttrs();

            if(StringUtil.hasText(xPath)) {
                if(xPath.contains("/")) {
                    elements = page.getByXPath(xPath);
                }else {
                    elements = page.getElementsByTagName(xPath);
                }
            }else {
                if(attr.containsKey("id")) {//id属性存在
                    elements = page.getElementsById(attr.get("id"));
                }else if(attr.containsKey("name")) {//name属性存在
                    elements = page.getElementsByName(attr.get("name"));
                }
            }

            return elements.stream().filter((element)->{
                String textKey = "text";
                for(Map.Entry<String,String> entry : attr.entrySet()) {
                    String attrName = entry.getKey();
                    String attrValue = entry.getValue();

                    if(attrName.equals(textKey)) {
                        if(attrValue == null || !attrValue.equals(element.asText())) {
                            return false;
                        }
                    }else {
                        if(element.hasAttribute(attrName)&&attrValue.equals(element.getAttribute(attrName))) {
                            continue;
                        }else {
                            return false;
                        }
                    }
                }
                return true;
            }).collect(Collectors.toList());        }
    }
}
