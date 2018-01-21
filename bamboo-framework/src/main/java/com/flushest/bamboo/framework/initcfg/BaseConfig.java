package com.flushest.bamboo.framework.initcfg;

import com.flushest.bamboo.framework.resource.ClassPathResourceResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Administrator on 2018/1/20 0020.
 */
@Configuration
public class BaseConfig {
    @Bean
    public ClassPathResourceResolver classPathResourceResolver() {
        return new ClassPathResourceResolver();
    }
}
