package com.flushest.bamboo.crawler.core.process;

import com.flushest.bamboo.common.framework.exception.BambooBusinessException;
import com.gargoylesoftware.htmlunit.html.*;

/**
 * Created by Administrator on 2017/12/7 0007.
 */
public class InputProcedure extends DynamicProcedure {

    private String inputValue;

    protected InputProcedure(String selector, String inputValue) {
        super(selector, ElementSelector.StrictLevel.UNIQUE);
        this.inputValue = inputValue;
    }

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
