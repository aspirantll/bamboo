package com.flushest.bamboo.crawler.core.process;

import com.flushest.bamboo.common.framework.exception.BambooBusinessException;
import com.flushest.bamboo.framework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/5 0005.
 */
public abstract class ElementSelector<T,P> {

    private String selector;

    protected StrictLevel strictLevel;

    protected SelectorParser selectorParser;

    public ElementSelector(String selector) {
        this(selector,StrictLevel.NONE);
    }

    public ElementSelector(String selector,StrictLevel strictLevel) {
        this.strictLevel = strictLevel;
        this.selector = selector;
        selectorParser = new SelectorParser(selector);
    }

    public abstract List<T> getElements(P page);

    public T getElement(P page) {
        List<T> elements = getElements(page);
        if(elements.isEmpty()) {
            throw new BambooBusinessException("指定选择器查找不到元素,selector="+selector+"\npage="+page);
        }
        switch (strictLevel) {
            case NONE:
                break;
            case UNIQUE:
                if(elements.size()>1) {
                    throw new BambooBusinessException("指定选择器获取到的元素不唯一,selector="+selector+"\npage="+page);
                }
                break;
        }
        return elements.get(0);
    }

    public class SelectorParser {
        private String xPath;
        private Map<String,String> attrs;

        /**
         * @param selector for example, "/html/body/a[id=id,href=]"
         */
        private SelectorParser(String selector) {
            Assert.notHasText(selector,"选择器不能为空...");
            attrs = new HashMap<String, String>();
            int splitPos = selector.indexOf('[');
            if(splitPos>0) {
                xPath = selector.substring(0,splitPos).trim();
                int endPos = selector.indexOf(']');
                if(endPos==-1) {
                    endPos = selector.length();
                }
                String attrString = selector.substring(splitPos+1,endPos);
                String[] attrPairArray = attrString.split(",");
                for(String attrPair : attrPairArray) {
                    String[] attrAndValue = attrPair.split("=");
                    if(attrAndValue.length!=2) {
                        throw new BambooBusinessException("选择器配置格式有误，请检查"+selector);
                    }
                    attrs.put(attrAndValue[0],attrAndValue[1]);
                }
            }else {
                xPath = selector.trim();
            }
        }

        public String getxPath() {
            return xPath;
        }

        public Map<String, String> getAttrs() {
            return attrs;
        }
    }

    public enum StrictLevel {
        NONE,
        UNIQUE;
    }
}
