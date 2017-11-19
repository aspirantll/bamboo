package com.flushest.bamboo.framework.util;

import com.flushest.bamboo.framework.util.convert.TypeConverter;
import com.flushest.bamboo.framework.util.convert.exception.UnsupportedConvertTypeException;
import org.springframework.util.Assert;

/**
 * Created by Administrator on 2017/10/14 0014.
 */
public class ConvertTypeUtil {
    /**
     * 转换类型
     * @param source 必传
     * @param targetClass 必传
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S,T> T convert(S source,Class<T> targetClass) {
        Assert.notNull(source,"source can not be null in method convert(source,target)");
        Assert.notNull(targetClass,"targetClass can not be null in method convert(source,target)");

        TypeConverter<S,T> typeConverter = SpringUtil.getBean(TypeConverter.class,source.getClass(),targetClass);
        if (typeConverter ==null) {
           throw new UnsupportedConvertTypeException(source.getClass().getName(),targetClass.getName());
        }
        return typeConverter.convert(source);
    }
}
