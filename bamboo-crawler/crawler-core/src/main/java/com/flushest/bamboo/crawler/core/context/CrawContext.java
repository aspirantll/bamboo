package com.flushest.bamboo.crawler.core.context;

import com.flushest.bamboo.crawler.core.chain.Task;
import lombok.Getter;

import java.util.*;

/**
 * Created by Administrator on 2017/12/11 0011.
 */
@Getter
public class CrawContext {
    private CrawlConfig config;

    private Map<String, Object> contextMap;

    private Task task;

    public CrawContext(CrawlConfig config, Task task) {
        this.config = config;
        this.task = task;
        contextMap = new HashMap<>();
    }

    public void put(String key, Object value) {
        contextMap.put(key, value);
    }

    public Object get(String key) {
        return contextMap.get(key);
    }
}
