package com.flushest.bamboo.crawler.core;

import com.flushest.bamboo.crawler.core.context.CrawContext;

/**
 * Created by Administrator on 2018/1/16 0016.
 */
public class ThreadLocalManager<T> {

    public static ThreadLocalManager<CrawContext> contextThreadLocalManager = new ThreadLocalManager<>();

    private ThreadLocal<T> threadLocal = new ThreadLocal<>();

    public void set(T value) {
        threadLocal.set(value);
    }

    public  T get() {
        return threadLocal.get();
    }

    public void clear() {
        threadLocal.remove();
    }
}
