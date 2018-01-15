package com.flushest.bamboo.framework.initcfg;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.AnnotationBean;
import com.flushest.bamboo.common.Constant;
import com.flushest.bamboo.framework.config.Configurations;
import com.flushest.bamboo.framework.util.ConfigCoreUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * Created by Administrator on 2017/10/14 0014.
 */
@Configuration
@PropertySource(value = "classpath:dubbo.properties",ignoreResourceNotFound = false)
@PropertySource(value = "classpath:bamboo.properties",ignoreResourceNotFound = true)
public class ServiceCoreConfig {
    private static final Logger logger = LoggerFactory.getLogger(ServiceCoreConfig.class);

    @Bean
    public Configurations configurations() {
        Configurations configurations = new Configurations();
        ConfigCoreUtil.setConfigurationService(configurations);
        return configurations;
    }

    @Bean
    public ApplicationConfig applicationConfig() {
        ApplicationConfig applicationConfig = new ApplicationConfig();

        applicationConfig.setName(ConfigCoreUtil.getApplicationName());
        applicationConfig.setOwner(ConfigCoreUtil.getApplicationOwner());

        return applicationConfig;
    }

    @Bean
    public RegistryConfig registryConfig() {
        RegistryConfig registryConfig = new RegistryConfig();

        registryConfig.setAddress(ConfigCoreUtil.getRegistryAddress());
        registryConfig.setCheck(true);

        return registryConfig;
    }

    @Bean
    public ProtocolConfig protocolConfig() {
        ProtocolConfig protocolConfig = new ProtocolConfig();

        protocolConfig.setName(ConfigCoreUtil.getProtocolName());
        protocolConfig.setPort(ConfigCoreUtil.getProtocolPort());

        return protocolConfig;
    }

    @Bean
    public AnnotationBean annotationBean() {
        AnnotationBean annotationBean = new AnnotationBean();

        annotationBean.setPackage(Constant.BASE_PACKAGE);

        return annotationBean;
    }

}
