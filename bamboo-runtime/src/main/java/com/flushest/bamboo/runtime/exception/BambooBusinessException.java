package com.flushest.bamboo.runtime.exception;

/**
 * Created by Administrator on 2017/10/14 0014.
 */
public class BambooBusinessException extends RuntimeException {
    public BambooBusinessException() {
        super();
    }

    public BambooBusinessException(String msg) {
        super(msg);
    }

    public BambooBusinessException(String msg,Throwable cause) {
        super(msg,cause);
    }

    public BambooBusinessException(Throwable cause) {
        super(cause);
    }
}