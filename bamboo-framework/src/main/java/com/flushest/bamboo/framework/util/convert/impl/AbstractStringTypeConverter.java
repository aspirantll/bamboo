package com.flushest.bamboo.framework.util.convert.impl;

import org.springframework.util.StringUtils;

/**
 * Created by Administrator on 2017/10/14 0014.
 */
public abstract class AbstractStringTypeConverter<T> extends AbstractTypeConverter<String,T> {
    @Override
    protected T covertNoWrappedException(String source) {
        if(!StringUtils.hasText(source)) {
            return null;
        }
        return trueConvert(source.trim());
    }

    protected abstract T trueConvert(String source);
}
