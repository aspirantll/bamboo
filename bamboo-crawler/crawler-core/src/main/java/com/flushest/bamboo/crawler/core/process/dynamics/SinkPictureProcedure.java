package com.flushest.bamboo.crawler.core.process.dynamics;

import com.flushest.bamboo.common.framework.exception.BambooRuntimeException;
import com.flushest.bamboo.crawler.core.ThreadLocalManager;
import com.flushest.bamboo.crawler.core.context.CrawlConfig;
import com.flushest.bamboo.crawler.core.context.TextFile;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.Setter;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2019/3/5 0005.
 */
public class SinkPictureProcedure extends DynamicProcedure {
    @Setter
    private String taskId;
    private int seq = 0;

    @Override
    protected boolean execute(HtmlPage page) {
        List<DomElement> elements = getElements(page);
        CrawlConfig config = ThreadLocalManager.contextThreadLocalManager.get().getConfig();
        elements.forEach((e)->{
            HtmlImage img = (HtmlImage) e;
            try {
                img.saveAs(TextFile.builder().fileName(config.getDirectory()+"\\"+taskId +"-"+(seq++)+".jpg").build().getFile());
            } catch (IOException e1) {
                throw new BambooRuntimeException("图片存储异常:",e1);
            }
        });
        return true;
    }
}
