package com.flushest.bamboo.crawler.core.storage;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

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
    public T get(long timeout, TimeUnit unit) throws InterruptedException {
        return queue.poll(timeout, unit);
    }

    @Override
    public boolean put(T t) {
        return queue.offer(t);
    }
}
