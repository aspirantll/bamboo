package com.flushest.bamboo.framework.util.convert.impl;

import com.flushest.bamboo.framework.util.convert.TypeConverter;
import com.flushest.bamboo.framework.util.convert.exception.ConvertTypeException;

/**
 * Created by Administrator on 2017/10/14 0014.
 */
public abstract class AbstractTypeConverter<S,T> implements TypeConverter<S,T> {
    @Override
    public T convert(S source) {
        try {
            return covertNoWrappedException(source);
        }catch (Exception e) {
            throw new ConvertTypeException(e);
        }
    }

    protected abstract T covertNoWrappedException(S source);
}
