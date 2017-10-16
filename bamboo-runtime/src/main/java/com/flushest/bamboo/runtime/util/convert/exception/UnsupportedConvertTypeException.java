package com.flushest.bamboo.runtime.util.convert.exception;

import com.flushest.bamboo.runtime.exception.BambooRuntimeException;

/**
 * Created by Administrator on 2017/10/14 0014.
 */
public class UnsupportedConvertTypeException extends BambooRuntimeException {
    public UnsupportedConvertTypeException(String sourceClass, String targetClass) {
        super(String.format(" %s convert to %s is unsupported"));
    }
}
