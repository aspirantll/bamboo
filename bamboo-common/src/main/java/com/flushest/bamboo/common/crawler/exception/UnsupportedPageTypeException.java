package com.flushest.bamboo.common.crawler.exception;

import com.flushest.bamboo.common.framework.exception.BambooRuntimeException;

/**
 * Created by Administrator on 2017/11/19 0019.
 */
public class UnsupportedPageTypeException extends BambooRuntimeException {
    public UnsupportedPageTypeException() {
        super("the type of page is unsupported");
    }
}
