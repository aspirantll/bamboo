package com.flushest.bamboo.runtime.util.convert.impl;

import com.flushest.bamboo.runtime.util.convert.TypeConverter;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/10/14 0014.
 */
@Component
public class StringToBooleanConverter extends AbstractStringTypeConverter<Boolean> implements TypeConverter<String,Boolean> {

    @Override
    protected Boolean trueConvert(String source) {
        return Boolean.valueOf(source);
    }
}
