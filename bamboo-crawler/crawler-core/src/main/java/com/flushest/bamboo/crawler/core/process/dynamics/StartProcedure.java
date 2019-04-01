package com.flushest.bamboo.crawler.core.process.dynamics;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Created by Administrator on 2019/2/7 0007.
 */
public class StartProcedure extends DynamicProcedure {

    public StartProcedure(){
        selector = "/";
    }
    @Override
    protected boolean execute(HtmlPage page) {
        return true;
    }
}
