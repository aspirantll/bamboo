package com.flushest.bamboo.crawler.core.process;

import com.flushest.bamboo.crawler.core.Context;
import com.flushest.bamboo.crawler.core.Page;
import com.flushest.bamboo.framework.util.Assert;
import org.jsoup.nodes.Element;

/**
 * Created by Administrator on 2017/12/11 0011.
 */
public class TextProcedure extends StaticProcedure {
    private String fieldName;
    protected TextProcedure(String fieldName,String selector) {
        super(selector);
        Assert.notHasText(fieldName,"fieldName must be not null");
        this.fieldName = fieldName;
    }

    @Override
    public boolean process(Context context) {
        Element element = getElement(context);
        context.setField(fieldName,element.text());
        return true;
    }
}
