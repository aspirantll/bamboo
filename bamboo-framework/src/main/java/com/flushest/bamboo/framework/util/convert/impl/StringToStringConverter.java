package com.flushest.bamboo.framework.util.convert.impl;

import com.flushest.bamboo.framework.util.convert.TypeConverter;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/10/14 0014.
 */
@Component
public class StringToStringConverter extends AbstractStringTypeConverter<String> implements TypeConverter<String,String> {

    @Override
    protected String trueConvert(String source) {
        return source;
    }
}
