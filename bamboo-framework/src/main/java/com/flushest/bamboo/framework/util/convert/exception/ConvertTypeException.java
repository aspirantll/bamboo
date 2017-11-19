package com.flushest.bamboo.framework.util.convert.exception;

import com.flushest.bamboo.common.framework.exception.BambooRuntimeException;

/**
 * Created by Administrator on 2017/10/14 0014.
 */
public class ConvertTypeException extends BambooRuntimeException{
    public ConvertTypeException(Throwable cause) {
        super("type convert occurred exception",cause);
    }
}
