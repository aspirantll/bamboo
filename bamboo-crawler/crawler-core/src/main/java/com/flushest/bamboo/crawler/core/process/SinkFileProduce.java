package com.flushest.bamboo.crawler.core.process;

import com.flushest.bamboo.crawler.core.ThreadLocalManager;
import com.flushest.bamboo.crawler.core.chain.Task;
import com.flushest.bamboo.crawler.core.context.CrawlConfig;
import com.flushest.bamboo.crawler.core.context.StaticContext;
import com.flushest.bamboo.crawler.core.context.TextFile;
import com.flushest.bamboo.crawler.core.frontier.ResourceManager;
import com.flushest.bamboo.crawler.core.frontier.ResourceManagerFactory;

import java.io.File;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/18 0018.
 */
public class SinkFileProduce extends StaticProcedure {

    private ResourceManager<TextFile> textFileResourceManager;

    protected SinkFileProduce() {
        textFileResourceManager = ResourceManagerFactory.getResourceManager(TextFile.class);
    }

    @Override
    public boolean process(StaticContext item) {
        Task task = ThreadLocalManager.contextThreadLocalManager.get().getTask();
        CrawlConfig config = ThreadLocalManager.contextThreadLocalManager.get().getConfig();

        Map<String,String> map = item.getFieldMap();
        return textFileResourceManager.offer(task.getTaskId(), TextFile.builder()
                .fileName(config.getDirectory()+ File.separator + task.getTaskId() +File.separator + map.get("fileName"))
                .content(map.get("content")).build());
    }
}
