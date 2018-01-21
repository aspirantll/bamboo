package com.flushest.bamboo.crawler.core.worker;

import com.flushest.bamboo.crawler.core.chain.Task;
import com.flushest.bamboo.framework.listener.Event;
import com.flushest.bamboo.framework.listener.Listener;
import com.flushest.bamboo.framework.thread.AbstractTerminableThread;

/**
 * Created by Administrator on 2018/1/20 0020.
 */
public class TaskMonitor extends AbstractTerminableThread implements Listener {

    private Task task;

    public TaskMonitor(Task task) {
        this.task = task;
        this.terminationToken = task.getTerminationToken();
    }


    @Override
    protected void doRun() throws Exception {

    }

    @Override
    public void onEvent(Event event) {

    }
}
