package com.flushest.bamboo.framework.initcfg;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.AnnotationBean;
import com.flushest.bamboo.common.Constant;
import com.flushest.bamboo.framework.config.Configurations;
import com.flushest.bamboo.framework.resource.ClassPathResourceResolver;
import com.flushest.bamboo.framework.util.ConfigCoreUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/9/28 0028.
 * 启动
 */
@SpringBootApplication(scanBasePackages = {"com.flushest.bamboo"},exclude = {DataSourceAutoConfiguration.class})
@PropertySource(value = "classpath:dubbo.properties",ignoreResourceNotFound = false)
@PropertySource(value = "classpath:bamboo.properties",ignoreResourceNotFound = true)
@Slf4j
public class StartUpCoreConfig {

    public static void main(String[] args) throws Exception {
        log.info("starting application ...");
        log.info(String.format("command line args:%s", Arrays.toString(args)));
        SpringApplication.run(StartUpCoreConfig.class,args);
    }

    @Bean
    public ClassPathResourceResolver classPathResourceResolver() {
        return new ClassPathResourceResolver();
    }

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
