package com.flushest.bamboo.runtime.initcfg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/9/28 0028.
 * 启动
 */
@SpringBootApplication(scanBasePackages = {"com.flushest.bamboo"},exclude = {DataSourceAutoConfiguration.class})
public class StartUpConfig {
    private static final Logger logger = LoggerFactory.getLogger(StartUpConfig.class);

    public static void main(String[] args) throws Exception {
        logger.info("starting application ...");
        logger.info(String.format("command line args:%s", Arrays.toString(args)));
        SpringApplication.run(StartUpConfig.class,args);
    }
}
