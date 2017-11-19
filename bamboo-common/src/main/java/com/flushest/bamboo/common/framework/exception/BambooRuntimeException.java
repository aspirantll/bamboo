package com.flushest.bamboo.common.framework.exception;

/**
 * Created by Administrator on 2017/10/14 0014.
 */
public class BambooRuntimeException  extends RuntimeException {

    public BambooRuntimeException() {
        super();
    }

    public BambooRuntimeException(String msg) {
        super(msg);
    }

    public BambooRuntimeException(String msg,Throwable cause) {
        super(msg,cause);
    }

    public BambooRuntimeException(Throwable cause) {
        super(cause);
    }
}
