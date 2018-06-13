package com.flushest.bamboo.common.crawler.exception;

import com.flushest.bamboo.common.framework.exception.BambooRuntimeException;

/**
 * Created by Administrator on 2018/6/6 0006.
 */
public class ErrorParseException  extends BambooRuntimeException {
    public ErrorParseException(String msg, Throwable e) {
        super(msg, e);
    }

    public ErrorParseException(String msg) {
        super(msg);
    }
}
