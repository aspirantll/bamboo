package com.flushest.bamboo.common.crawler.exception;

import com.flushest.bamboo.common.framework.exception.BambooRuntimeException;

/**
 * Created by Administrator on 2017/11/16 0016.
 */
public class IllegalUrlException extends BambooRuntimeException {
    public IllegalUrlException(String url) {
        super("the url is illegal:"+url);
    }
}
