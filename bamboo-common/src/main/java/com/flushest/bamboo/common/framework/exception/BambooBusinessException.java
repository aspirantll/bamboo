package com.flushest.bamboo.common.framework.exception;

/**
 * Created by Administrator on 2017/10/14 0014.
 */
public class BambooBusinessException extends RuntimeException {

    private String errorCode;//错误码

    public BambooBusinessException(String errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public BambooBusinessException(String errorCode,String msg) {
        super(msg);
        this.errorCode = errorCode;
    }

    public BambooBusinessException(String errorCode,String msg,Throwable cause) {
        super(msg,cause);
        this.errorCode = errorCode;
    }


}