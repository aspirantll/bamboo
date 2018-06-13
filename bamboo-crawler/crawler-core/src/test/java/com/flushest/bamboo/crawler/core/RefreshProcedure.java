package com.flushest.bamboo.crawler.core;

import com.flushest.bamboo.crawler.core.constant.FieldName;
import com.flushest.bamboo.crawler.core.context.CrawContext;
import com.flushest.bamboo.crawler.core.process.*;
import com.flushest.bamboo.crawler.core.process.dynamics.DynamicProcedure;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Created by Administrator on 2018/1/27 0027.
 */
public class RefreshProcedure extends DynamicProcedure {
    public RefreshProcedure() {
        this.setSelector("/");
    }

    @Override
    protected boolean execute(HtmlPage page) {
        CrawContext crawContext = ThreadLocalManager.contextThreadLocalManager.get();
        String currentUrl = (String) crawContext.get(FieldName.CURRENT_URL);
        try {
            boolean replace = false;
            HtmlPage newPage = page;
            while (newPage.asText().contains(currentUrl)) {
                newPage.getWebClient().getOptions().setJavaScriptEnabled(true);
                newPage = (HtmlPage) newPage.refresh();
                replace = true;
            }
            if(replace) {
                newPage.getWebClient().getOptions().setJavaScriptEnabled(false);
                ProcedureUtil.replaceDynamicPage(page, newPage);
            }
        } catch (Exception e) {

        }
        return true;
    }
}
