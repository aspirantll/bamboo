package com.flushest.bamboo.crawler.core.context;

import com.flushest.bamboo.common.framework.exception.BambooRuntimeException;
import com.flushest.bamboo.crawler.core.process.ElementSelector;
import com.flushest.bamboo.framework.util.Assert;
import com.flushest.bamboo.framework.util.StringUtil;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/1/17 0017.
 */
public class StaticContext {
    private Page page;
    private Element currentElement;
    private boolean isIterable;


    public StaticContext(Page page, boolean isIterable) {
        Assert.notNull(page,"page must be not null");
        this.page = page;
        this.isIterable = isIterable;
        currentElement = page.getDocument();
    }

    public Page getPage() {
        return page;
    }

    public Element getCurrentElement() {
        return currentElement;
    }

    public Element getElementBySelector(ElementSelector.SelectorParser selectorParser) {
        String operateSelector = selectorParser.getOperateSelector();
        String cssSelector = selectorParser.getCssSelector();

        Element nextElement;
        if(StringUtil.hasText(operateSelector)) {
            String method;
            String[] args;

            int splitPos = operateSelector.indexOf('(');
            if(splitPos==-1) {
                method = operateSelector;
                args = null;
            }else {
                method = operateSelector.substring(0,splitPos);
                args = operateSelector.substring(splitPos+1).split(",");
            }

            switch(method) {
                case "parent":
                    nextElement = currentElement.parent();
                    break;
                case "child":
                    if(args.length!=1) {
                        throw new BambooRuntimeException("非法方法调用:"+operateSelector);
                    }
                    try {
                        int index = Integer.parseInt(args[0]);
                        nextElement = currentElement.child(index);
                    }catch (NumberFormatException e) {
                        throw new BambooRuntimeException("非法方法调用:"+operateSelector);
                    }
                    break;
                case "firstElementSibling":
                    nextElement = currentElement.firstElementSibling();
                    break;
                case "previousElementSibling":
                    nextElement = currentElement.previousElementSibling();
                    break;
                case "nextElementSibling":
                    nextElement = currentElement.nextElementSibling();
                    break;
                case "resetCurrentElement":
                    nextElement = resetCurrentElement();
                    break;
                default:
                    throw new BambooRuntimeException("暂不支持的方法调用:" + operateSelector);
            }
        }else if(StringUtil.hasText(cssSelector)) {
            nextElement = currentElement.select(cssSelector).first();
        }else {
            //初始化元素List
            List<Element> elementList = new ArrayList();

            String xPath = selectorParser.getxPath();
            final Map<String,String> attrs = selectorParser.getAttrs();

            if(StringUtil.hasText(xPath)) {
                Elements elements = new Elements();
                elements.add(currentElement);

                String[] tagPaths = xPath.split("/");
                for(String tag : tagPaths) {
                    Elements nextLevel = new Elements();
                    for(Element e : elements) {
                        nextLevel.addAll(e.getElementsByTag(tag));
                    }
                    elements = nextLevel;
                }

                elementList = elements.stream().filter((element)->{
                    for(Map.Entry<String,String> entry : attrs.entrySet()) {
                        if(element.hasAttr(entry.getKey())&&element.attr(entry.getKey()).equals(entry.getValue())) {
                            continue;
                        }else {
                            return false;
                        }
                    }
                    return true;
                }).collect(Collectors.toList());
            }else {
                if(attrs.containsKey("id")) {
                    Element element = currentElement.getElementById(attrs.get("id"));
                    if(element!=null) elementList.add(element);
                }else {
                    Iterator<Map.Entry<String,String>> iterator = attrs.entrySet().iterator();
                    if(iterator.hasNext()) {
                        Map.Entry<String,String> attr = iterator.next();
                        Elements elements = currentElement.getElementsByAttributeValue(attr.getKey(),attr.getValue());
                        iterator.forEachRemaining((entry)->{
                            Iterator<Element> iter = elements.iterator();
                            while(iter.hasNext()) {
                                Element e = iter.next();
                                if(e.hasAttr(entry.getKey())&&e.attr(entry.getKey()).equals(entry.getValue())) {
                                    continue;
                                }else {
                                    iter.remove();
                                }
                            }
                        });
                    }
                }
            }



            if(elementList.isEmpty()) {
                throw new BambooRuntimeException("未找到符合条件的元素：" + selectorParser);
            }
            nextElement = elementList.get(0);
        }
        return iterate(nextElement);
    }

    public Element resetCurrentElement() {
        currentElement = page.getDocument();
        return getCurrentElement();
    }

    private Element iterate(Element next) {
        if(isIterable) {
            currentElement = next;
        }
        return next;
    }
}
