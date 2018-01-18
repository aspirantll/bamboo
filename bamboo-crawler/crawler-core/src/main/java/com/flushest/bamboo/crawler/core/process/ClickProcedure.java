package com.flushest.bamboo.crawler.core.process;

import com.flushest.bamboo.common.framework.exception.BambooBusinessException;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Administrator on 2017/12/5 0005.
 * 点击事件节点
 */
@Slf4j
public class ClickProcedure extends DynamicProcedure {

    public ClickProcedure(String selector) {
        super(selector, ElementSelector.StrictLevel.UNIQUE);
    }

    @Override
    protected boolean execute(HtmlPage page) {
        DomElement clickElement = getElement(page);
        try {
            clickElement.click();
        } catch (IOException e) {
            log.error("触发点击事件异常,element="+clickElement,e);
            throw new BambooBusinessException("点击事件触发异常...");
        }
        return true;
    }
}
