package com.flushest.bamboo.crawler.core.process;

import com.flushest.bamboo.crawler.core.ThreadLocalManager;
import com.flushest.bamboo.crawler.core.chain.Task;
import com.flushest.bamboo.crawler.core.constant.FieldName;
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

    public SinkFileProduce() {
        textFileResourceManager = ResourceManagerFactory.getResourceManager(TextFile.class);
    }

    @Override
    public boolean process(StaticContext item) {
        Task task = ThreadLocalManager.contextThreadLocalManager.get().getTask();
        CrawlConfig config = ThreadLocalManager.contextThreadLocalManager.get().getConfig();

        Map<String,Object> map = ThreadLocalManager.contextThreadLocalManager.get().getContextMap();
        String fileName = (String) map.get(FieldName.FILE_NAME);
        if(fileName.contains("${timestamp}")) {
            fileName.replaceAll("\\$\\{timestamp\\}", "" + task.getStartTime());
        }
        return textFileResourceManager.offer(task.getTaskId(), TextFile.builder()
                .fileName(config.getDirectory()+ File.separator + task.getTaskId() +File.separator + fileName)
                .content((String)map.get(FieldName.CONTENT)).build());
    }
}
