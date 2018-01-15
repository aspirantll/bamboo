package com.flushest.bamboo.crawler.core.frontier;

import com.flushest.bamboo.crawler.core.Page;

/**
 * Created by Administrator on 2018/1/10 0010.
 */
public class PageResourceManager extends AbstractResourceManager<Page> {
    @Override
    public boolean offer(String html) {
        Page page = new Page(html);
        return offer(page);
    }
}
