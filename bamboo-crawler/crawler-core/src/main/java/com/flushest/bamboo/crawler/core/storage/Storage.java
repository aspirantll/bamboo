package com.flushest.bamboo.crawler.core.storage;


import com.flushest.bamboo.framework.annotation.SPI;

/**
 * Created by Administrator on 2018/1/10 0010.
 */
@SPI
public interface Storage<T> {
    T get() throws InterruptedException;
    boolean put(T t);
}
