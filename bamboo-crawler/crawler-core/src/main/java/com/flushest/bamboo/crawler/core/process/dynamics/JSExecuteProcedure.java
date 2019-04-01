package com.flushest.bamboo.crawler.core.process.dynamics;

import com.flushest.bamboo.crawler.core.process.ProcedureUtil;
import com.flushest.bamboo.framework.util.Assert;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Administrator on 2019/3/5 0005.
 */
@Slf4j
public class JSExecuteProcedure extends DynamicProcedure{
    @Setter
    private String statement;
    @Override
    public void afterProperties() {
        Assert.notHasText(statement,"JS语句不能为空");
    }
    @Override
    protected boolean execute(HtmlPage page) {
        ScriptResult s = page.executeJavaScript(statement);//执行js方法
        HtmlPage newPage=(HtmlPage) s.getNewPage();//获得执行后的新page对象
        ProcedureUtil.replaceDynamicPage(page, newPage);
        return true;
    }
}
