package com.flushest.bamboo.framework.config;

import com.flushest.bamboo.framework.util.TypeConverter;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * Created by Administrator on 2017/10/14 0014.
 */
public class Configurations {
    @Autowired
    private Environment env;

    public <T> T get(String name, Class<T> valueClass) {
        String value = env.getProperty(name);
        return TypeConverter.convert(value,valueClass);
    }

    public String get(String name) {
        return env.getProperty(name);
    }
}
