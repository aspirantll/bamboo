package com.flushest.bamboo.runtime.initcfg;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.AnnotationBean;
import com.flushest.bamboo.runtime.service.IConfigurationService;
import com.flushest.bamboo.runtime.service.impl.ConfigurationServiceImpl;
import com.flushest.bamboo.runtime.util.ConfigCoreUtil;
import com.flushest.bamboo.runtime.util.ConstantUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/10/14 0014.
 */
@Component
public class ServiceCoreConfig {
    private static final Logger logger = LoggerFactory.getLogger(ServiceCoreConfig.class);
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

        annotationBean.setPackage(ConstantUtil.BASE_PACKAGE);

        return annotationBean;
    }

}
