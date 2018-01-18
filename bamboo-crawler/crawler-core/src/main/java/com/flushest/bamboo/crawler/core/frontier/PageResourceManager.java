package com.flushest.bamboo.crawler.core.frontier;

import com.flushest.bamboo.crawler.core.context.Page;

/**
 * Created by Administrator on 2018/1/10 0010.
 */
public class PageResourceManager extends AbstractResourceManager<Page> {

    @Override
    public boolean offer(String key,String html) {
        Page page = new Page(html);
        return offer(key, page);
    }
}
