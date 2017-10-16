package com.flushest.bamboo.runtime.util.convert.impl;

import com.flushest.bamboo.runtime.util.convert.TypeConverter;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/10/14 0014.
 */
@Component
public class StringToFloatConverter extends AbstractStringTypeConverter<Float> implements TypeConverter<String,Float> {

    @Override
    protected Float trueConvert(String source) {
        return Float.parseFloat(source);
    }
}
