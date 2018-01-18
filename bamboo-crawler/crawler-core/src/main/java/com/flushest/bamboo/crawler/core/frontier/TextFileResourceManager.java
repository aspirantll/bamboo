package com.flushest.bamboo.crawler.core.frontier;

import com.flushest.bamboo.crawler.core.context.TextFile;

/**
 * Created by Administrator on 2018/1/18 0018.
 */
public class TextFileResourceManager extends AbstractResourceManager<TextFile> {
    @Override
    public boolean offer(String key, String resource) {
        throw new UnsupportedOperationException("unsupported operation : offer");
    }
}
