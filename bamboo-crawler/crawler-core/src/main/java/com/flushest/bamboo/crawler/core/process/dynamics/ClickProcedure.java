package com.flushest.bamboo.crawler.core.process.dynamics;

import com.flushest.bamboo.common.framework.exception.BambooBusinessException;
import com.flushest.bamboo.crawler.core.process.ProcedureUtil;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.extern.slf4j.Slf4j;


import java.io.IOException;

/**
 * Created by Administrator on 2017/12/5 0005.
 * 点击事件节点
 */
@Slf4j
public class ClickProcedure extends DynamicProcedure {

    @Override
    protected boolean execute(HtmlPage page) {
        DomElement clickElement = getElement(page);
        try {
            HtmlPage newPage;
            if(clickElement instanceof HtmlAnchor) {
                HtmlAnchor ha = (HtmlAnchor) clickElement;
                newPage = (HtmlPage) ha.openLinkInNewWindow();
            }else {
                newPage = clickElement.click();
            }
            ProcedureUtil.replaceDynamicPage(page, newPage);
        } catch (IOException e) {
            log.error("触发点击事件异常,element="+clickElement,e);
            throw new BambooBusinessException("点击事件触发异常...");
        }
        return true;
    }
}
