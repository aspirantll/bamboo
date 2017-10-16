package com.flushest.bamboo.runtime.service.impl;

import com.flushest.bamboo.runtime.service.IConfigurationService;
import com.flushest.bamboo.runtime.util.ConvertTypeUtil;
import com.flushest.bamboo.runtime.util.convert.TypeConverter;
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
public class ConfigurationServiceImpl implements IConfigurationService {

    @Autowired
    private Environment env;

    @Override
    public <T> T get(String name,Class<T> valueClass) {
        String value = env.getProperty(name);
        return ConvertTypeUtil.convert(value,valueClass);
    }

    @Override
    public String get(String name) {
        return env.getProperty(name);
    }
}
