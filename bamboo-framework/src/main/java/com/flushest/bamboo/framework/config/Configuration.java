package com.flushest.bamboo.framework.config;

import com.flushest.bamboo.framework.util.ConvertTypeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/10/14 0014.
 */
@Component
@PropertySource(value = "classpath:dubbo.properties",ignoreResourceNotFound = true)
@PropertySource(value = "classpath:bamboo.properties",ignoreResourceNotFound = true)
public class Configuration {

    @Autowired
    private Environment env;

    public <T> T get(String name,Class<T> valueClass) {
        String value = env.getProperty(name);
        return ConvertTypeUtil.convert(value,valueClass);
    }

    public String get(String name) {
        return env.getProperty(name);
    }
}
