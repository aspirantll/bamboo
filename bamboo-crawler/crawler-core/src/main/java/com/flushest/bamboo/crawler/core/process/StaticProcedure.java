package com.flushest.bamboo.crawler.core.process;

import com.flushest.bamboo.crawler.core.Page;
import org.jsoup.nodes.Document;

/**
 * Created by Administrator on 2017/11/19 0019.
 */
public abstract class StaticProcedure implements Procedure<Page> {

    protected abstract boolean execute(Document document);

    @Override
    public boolean process(Page page) {
        return execute(page.parseByJSoup());
    }
}
