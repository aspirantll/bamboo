package com.flushest.bamboo.crawler.core.storage;


import com.flushest.bamboo.framework.annotation.SPI;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/1/10 0010.
 */
@SPI
public interface Storage<T> {
    T get() throws InterruptedException;
    T get(long timeout, TimeUnit unit) throws InterruptedException;
    boolean put(T t);
}
