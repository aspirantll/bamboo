package com.flushest.bamboo.crawler.core.parser;

import com.flushest.bamboo.common.crawler.exception.ErrorParseException;
import com.flushest.bamboo.crawler.core.chain.Task;
import com.flushest.bamboo.framework.util.Assert;
import com.flushest.bamboo.framework.util.ClassUtil;
import org.jsoup.Jsoup;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/6 0006.
 */
public class ClasspathXmlParser implements TaskParser{
    private String xmlPath;

    public ClasspathXmlParser(String xmlPath) {
        Assert.notHasText(xmlPath, "xmlPath must be not empty");
        this.xmlPath = xmlPath;
    }

    public List<Task> parse() {
        String realPath;
        if(xmlPath.startsWith("classpath:")) {
            realPath = xmlPath.substring(10);
        }else if (xmlPath.startsWith("classpath*:")){
            realPath = xmlPath.substring(11);
        }else {
            realPath = xmlPath;
        }

        List<Task> tasks = new ArrayList<>();
        try {
            Resource[]  resources = ClassUtil.scanFile(realPath);
            for(Resource resource : resources) {
                TaskXmlParser parser = new TaskXmlParser(resource.getInputStream());
                tasks.addAll(parser.parse());
            }
        } catch (IOException e) {
            throw new ErrorParseException("failed to parse xml:" + xmlPath, e);
        }
        return tasks;
    }
}
