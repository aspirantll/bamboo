package com.flushest.bamboo.crawler.core.process.statics;

import com.flushest.bamboo.crawler.core.ThreadLocalManager;
import com.flushest.bamboo.crawler.core.chain.Task;
import com.flushest.bamboo.crawler.core.constant.FieldName;
import com.flushest.bamboo.crawler.core.context.CrawlConfig;
import com.flushest.bamboo.crawler.core.context.StaticContext;
import com.flushest.bamboo.crawler.core.context.TextFile;
import com.flushest.bamboo.crawler.core.frontier.ResourceManager;
import com.flushest.bamboo.crawler.core.frontier.ResourceManagerFactory;
import lombok.Setter;

import java.io.File;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/18 0018.
 */
public class SinkFileProcedure extends StaticProcedure {

    private ResourceManager<TextFile> textFileResourceManager;
    @Setter
    private String destFileName;
    @Setter
    private String contentField;

    public SinkFileProcedure() {
        textFileResourceManager = ResourceManagerFactory.getResourceManager(TextFile.class);
    }

    public void afterProperties() {

    }


    @Override
    public boolean process(StaticContext item) {
        Task task = ThreadLocalManager.contextThreadLocalManager.get().getTask();
        CrawlConfig config = ThreadLocalManager.contextThreadLocalManager.get().getConfig();

        Map<String,Object> map = ThreadLocalManager.contextThreadLocalManager.get().getContextMap();
        String fileName = destFileName;
        if(fileName.contains("${timestamp}")) {
            fileName = fileName.replaceAll("\\$\\{timestamp\\}", "" + task.getStartTime());
        }
        return textFileResourceManager.offer(task.getId(), TextFile.builder()
                .fileName(config.getDirectory()+ File.separator + task.getId() +File.separator + fileName)
                .content((String)map.get(contentField)).build());
    }
}
