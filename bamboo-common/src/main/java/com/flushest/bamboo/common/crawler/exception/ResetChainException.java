package com.flushest.bamboo.common.crawler.exception;

import com.flushest.bamboo.common.framework.exception.BambooRuntimeException;

/**
 * Created by Administrator on 2017/11/18 0018.
 */
public class ResetChainException extends BambooRuntimeException {
    public ResetChainException() {
        super("reset chain error,please check whether the wholeTree is not null");
    }
}
