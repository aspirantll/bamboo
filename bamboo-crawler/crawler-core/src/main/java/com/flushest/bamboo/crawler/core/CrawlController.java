package com.flushest.bamboo.crawler.core;

import com.flushest.bamboo.common.framework.exception.BambooRuntimeException;
import com.flushest.bamboo.crawler.core.chain.Task;
import com.flushest.bamboo.crawler.core.context.CrawlConfig;
import com.flushest.bamboo.framework.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Administrator on 2017/12/9 0009.
 * 爬取控制器
 */
public class CrawlController {

    protected ConcurrentMap<String, Task> tasks = new ConcurrentHashMap<>();




    public CrawlController() {

    }

    public void addTask(Task task) {
        Assert.notNull(task, "task must be not null");
        tasks.putIfAbsent(task.getTaskId(), task);
    }

    public void start(Task task) {
        addTask(task);
        task.start();
    }

    public void start(String taskId) {
        Task task = tasks.get(taskId);
        if(task == null) {
            throw new BambooRuntimeException(String.format("找不到task[%s]", taskId));
        }
        task.start();
    }
}
