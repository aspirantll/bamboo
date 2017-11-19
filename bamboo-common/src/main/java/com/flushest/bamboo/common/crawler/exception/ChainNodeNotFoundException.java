package com.flushest.bamboo.common.crawler.exception;

import com.flushest.bamboo.common.framework.exception.BambooRuntimeException;

/**
 * Created by Administrator on 2017/11/18 0018.
 */
public class ChainNodeNotFoundException extends BambooRuntimeException {
    public ChainNodeNotFoundException() {
        super("chain node cannot found, please check whether currentNode has child nodeds");
    }
}
