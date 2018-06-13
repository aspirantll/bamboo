package com.flushest.bamboo.framework.util;

import org.springframework.beans.SimpleTypeConverter;

/**
 * Created by Administrator on 2017/12/9 0009.
 */
public class TypeConverter {
    private static SimpleTypeConverter typeConverter = new SimpleTypeConverter();

    public static <S,T> T convert(S source, Class<T> targetClass) {
        if(source == null) return null;
        if(source.getClass().equals(targetClass)  || source.getClass().isAssignableFrom(targetClass)) {
            return (T) source;
        }
        Assert.notNull(targetClass,"targetClass must not be null");
        return typeConverter.convertIfNecessary(source,targetClass);
    }
}
