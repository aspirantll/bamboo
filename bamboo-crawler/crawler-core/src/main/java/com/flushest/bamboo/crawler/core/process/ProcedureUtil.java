package com.flushest.bamboo.crawler.core.process;

import com.flushest.bamboo.crawler.core.ThreadLocalManager;
import com.flushest.bamboo.crawler.core.constant.FieldName;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Created by Administrator on 2018/1/20 0020.
 */
public class ProcedureUtil {

    public static void replaceDynamicPage(HtmlPage oldPage, HtmlPage newPage) {
        WebWindow window = oldPage.getEnclosingWindow();
        window.setEnclosedPage(newPage);
        newPage.setEnclosingWindow(window);
        ThreadLocalManager.contextThreadLocalManager.get().put(FieldName.CURRENT_URL, newPage.getUrl().toString());
    }
}
