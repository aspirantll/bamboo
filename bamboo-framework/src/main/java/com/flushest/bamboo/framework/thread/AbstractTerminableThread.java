package com.flushest.bamboo.framework.thread;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by Administrator on 2018/1/15 0015.
 * 可终止抽象模板线程
 */
@Slf4j
public abstract class AbstractTerminableThread extends Thread implements Terminable {
    public TerminationToken terminationToken;

    public AbstractTerminableThread() {
        this(new TerminationToken());
    }

    public AbstractTerminableThread(TerminationToken terminationToken) {
        super();
        this.terminationToken = terminationToken;
        terminationToken.register(this);
    }

    /**
     * 子类实现其线程逻辑
     * @throws Exception
     */
    protected abstract void doRun() throws Exception;

    /**
     * 子类实现线程停止后清理工作
     * @param cause
     */
    protected void doCleanup(Exception cause) {
        //什么也不做

    }

    /**
     * 子类实现线程停止逻辑
     */
    protected void doTerminate() {
        //什么也不做
    }

    @Override
    public void run() {
        Exception ex = null;
        try {
            while (true) {
                //执行线程处理逻辑之前先判断线程停止标志
                if (terminationToken.isToShutdown()
                        && terminationToken.reservations.get() <= 0) {
                    break;
                }
                doRun();
            }
        } catch (Exception e) {
            //使线程能够响应interrupt调用而退出
            ex = e;
            log.error("occurred error during thread running", e);
        } finally {
            try {
                doCleanup(ex);
            } finally {
                terminationToken.notifyThreadTermination(this);
            }
        }
    }

    @Override
    public void interrupt() {
        terminate();
    }

    @Override
    public void terminate() {
        terminationToken.setToShutdown();
        try {
            doTerminate();
        }finally {
            //若无待处理任务，则试图强行终止
            if (terminationToken.reservations.get() <= 0) {
                super.interrupt();
            }
        }
    }

    public void terminate(boolean waitUtilThreadTerminated) {
        terminate();
        if (waitUtilThreadTerminated) {
            try {
                this.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
