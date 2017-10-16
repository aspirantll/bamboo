package com.flushest.bamboo.runtime.util.convert.exception;

import com.flushest.bamboo.runtime.exception.BambooRuntimeException;

/**
 * Created by Administrator on 2017/10/14 0014.
 */
public class ConvertTypeException extends BambooRuntimeException{
    public ConvertTypeException(Throwable cause) {
        super("type convert occurred exception",cause);
    }
}
