package com.flushest.bamboo.crawler.core.parser;

import com.flushest.bamboo.crawler.core.chain.Task;

import java.util.List;

/**
 * Created by Administrator on 2018/6/12 0012.
 */
public interface TaskParser {
    List<Task> parse();
}
