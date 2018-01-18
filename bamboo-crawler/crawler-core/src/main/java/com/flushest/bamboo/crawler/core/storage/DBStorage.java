package com.flushest.bamboo.crawler.core.storage;

import java.util.Map;

/**
 * Created by Administrator on 2018/1/17 0017.
 */
public class DBStorage implements Storage<Map<String, Object>> {
    @Override
    public Map<String, Object> get() throws InterruptedException {
        return null;
    }

    @Override
    public boolean put(Map<String, Object> stringObjectMap) {
        return false;
    }
}
