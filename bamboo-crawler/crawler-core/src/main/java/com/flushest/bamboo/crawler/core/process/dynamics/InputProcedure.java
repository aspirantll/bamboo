package com.flushest.bamboo.crawler.core.process.dynamics;

import com.flushest.bamboo.common.framework.exception.BambooBusinessException;
import com.flushest.bamboo.crawler.core.process.ElementSelector;
import com.gargoylesoftware.htmlunit.html.*;
import lombok.Setter;

/**
 * Created by Administrator on 2017/12/7 0007.
 */
public class InputProcedure extends DynamicProcedure {

    @Setter
    private String inputValue;

    @Override
    protected boolean execute(HtmlPage page) {
        DomElement element = getElement(page);
        if(element instanceof HtmlInput) {
            HtmlInput input = (HtmlInput)element;
            input.setValueAttribute(inputValue);
        }else if(element instanceof HtmlTextArea){
            HtmlTextArea textArea = (HtmlTextArea)element;
            textArea.setText(inputValue);
        }else if(element instanceof HtmlSelect) {
            HtmlSelect select = (HtmlSelect) element;
            select.setSelectedAttribute(inputValue,true);
        }else {
            throw new BambooBusinessException("暂不支持此种类型元素Input");
        }
        return true;
    }
}
