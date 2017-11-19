package com.flushest.bamboo.framework.util.convert.impl;

import com.flushest.bamboo.framework.util.convert.TypeConverter;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/10/14 0014.
 */
@Component
public class StringToIntegerConverter extends AbstractStringTypeConverter<Integer> implements TypeConverter<String,Integer> {


    @Override
    protected Integer trueConvert(String source) {
        return Integer.parseInt(source);
    }
}
