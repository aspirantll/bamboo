package com.flushest.bamboo.crawler.core.storage;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Administrator on 2018/1/10 0010.
 */
public class BlockingStorage<T> implements Storage<T> {
    private final BlockingQueue<T> queue = new LinkedBlockingQueue<T>();


    @Override
    public T get() throws InterruptedException {
        return queue.take();
    }

    @Override
    public boolean put(T t) {
        return queue.offer(t);
    }
}
