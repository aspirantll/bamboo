package com.flushest.bamboo.crawler.core.process.statics;

import com.flushest.bamboo.common.framework.exception.BambooRuntimeException;
import com.flushest.bamboo.crawler.core.ThreadLocalManager;
import com.flushest.bamboo.crawler.core.context.CrawContext;
import com.flushest.bamboo.crawler.core.context.StaticContext;
import com.flushest.bamboo.framework.util.Assert;
import com.flushest.bamboo.framework.util.StringUtil;
import lombok.Setter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

/**
 * Created by Administrator on 2017/12/11 0011.
 */
public class StaticTextProcedure extends StaticProcedure {
    @Setter
    private String separator = "";
    @Setter
    private String fieldName;
    @Setter
    private boolean isAppend;
    @Setter
    private String value;
    @Setter
    private String attrKey;

    @Override
    public void afterProperties() {
        Assert.notHasText(fieldName, "fieldName must be not empty");
        if(StringUtil.isEmpty(value)) {
            super.afterProperties();
        }
    }

    @Override
    public boolean process(StaticContext staticContext) {
        CrawContext crawContext = ThreadLocalManager.contextThreadLocalManager.get();
        if(value != null) {
            crawContext.put(fieldName, value);
            return true;
        }

        Element element = getElement(staticContext);

        String newValue;
        Object value = crawContext.get(fieldName);
        String propertyValue = getText(element);

        if(value == null || !isAppend) {
            newValue = propertyValue + separator;
        }else {
            if(value instanceof String) {
                newValue = value + propertyValue + separator;
            }else {
                throw new BambooRuntimeException(String.format("fieldName[%s]对应value[%s]不支持追加", fieldName, value));
            }
        }


        crawContext.put(fieldName, newValue);
        return true;
    }

    protected String getText(Element element) {
        if("text".equals(attrKey)) {
            return Jsoup.clean(element.html(), "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
        }

        if("html".equals(attrKey)) {
            return element.html();
        }
        return element.attr(attrKey);
    }
}
