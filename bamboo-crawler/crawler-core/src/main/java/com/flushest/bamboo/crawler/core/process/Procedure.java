package com.flushest.bamboo.crawler.core.process;

import com.flushest.bamboo.framework.annotation.SPI;

/**
 * Created by Administrator on 2017/11/16 0016.
 */
@SPI
public interface Procedure<T> {
    void afterProperties();
    boolean process(T item);
}
