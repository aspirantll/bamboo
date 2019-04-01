package com.flushest.bamboo.crawler.core.storage;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/1/17 0017.
 */
public class DBStorage implements Storage<Map<String, Object>> {
    @Override
    public int length() {
        return 0;
    }

    @Override
    public Map<String, Object> get() throws InterruptedException {
        return null;
    }

    @Override
    public Map<String, Object> get(long timeout, TimeUnit unit) throws InterruptedException {
        return null;
    }

    @Override
    public boolean put(Map<String, Object> stringObjectMap) {
        return false;
    }
}
