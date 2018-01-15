package com.flushest.bamboo.framework.thread;

import java.lang.ref.WeakReference;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2018/1/15 0015.
 * 线程停止标志
 */
public class TerminationToken {
    //使用volatile关键字保证内存可见性
    protected  volatile boolean toShutdown = false;
    public final AtomicInteger reservations = new AtomicInteger(0);

    //用于多个可停止线程，共享一个TerminationToken实例
    private Queue<WeakReference<Terminable>> coordinatedThreads;

    public TerminationToken() {
        coordinatedThreads = new ConcurrentLinkedQueue<>();
    }

    public boolean isToShutdown() {
        return toShutdown;
    }

    protected void setToShutdown() {
        this.toShutdown = true;
    }

    protected void register(Terminable thread) {
        coordinatedThreads.add(new WeakReference<Terminable>(thread));
    }

    /**
     * 通知TerminationToken实例:共享该实例的所有可停止线程中的一个线程停止了，
     * 以便停止其他未被停止线程
     * @param thread 已停止线程
     */
    protected void notifyThreadTermination(Terminable thread) {
        WeakReference<Terminable> wrThread;
        Terminable otherThread;
        while (null != (wrThread = coordinatedThreads.poll())) {
            otherThread = wrThread.get();
            if (null != otherThread && otherThread != thread) {
                otherThread.terminate();
            }
        }
    }
}
