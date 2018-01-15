package com.flushest.bamboo.crawler.core.frontier;

/**
 * Created by Administrator on 2018/1/10 0010.
 */
public interface ResourceManager<T> {
    T accept(String key) throws InterruptedException;

    boolean offer(String key, T t);

    boolean offer(String key,String resource);
}
