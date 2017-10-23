package com.flushest.bamboo.runtime.common.persistence;

import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2017/10/19 0019.
 */
@Component
public class SqlSessionFactoryProxy implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(SqlSessionFactoryProxy.class);

    private static Map<String,SqlSessionFactory> tablePrefixAndSqlSessionFactoryMap = new ConcurrentHashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public static void registerSqlSessionFactory(String tablePrefix,SqlSessionFactory sqlSessionFactory) {
        tablePrefixAndSqlSessionFactoryMap.put(tablePrefix,sqlSessionFactory);
    }
}