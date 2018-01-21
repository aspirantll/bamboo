package com.flushest.bamboo.crawler.core.frontier;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/1/10 0010.
 */
public interface ResourceManager<T> {
    T accept(String key) throws InterruptedException;

    T accept(String key, long timeout, TimeUnit unit) throws InterruptedException;

    boolean offer(String key, T t);

    boolean offer(String key,String resource);
}
