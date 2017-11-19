package com.flushest.bamboo.crawler.core.process;

/**
 * Created by Administrator on 2017/11/16 0016.
 */
public interface Procedure<T> {
    int process(T item);
}
