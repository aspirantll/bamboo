package com.flushest.bamboo.framework.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Administrator on 2017/10/14 0014.
 */
@Component
public class SpringUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> beanClass) {
        return applicationContext.getBean(beanClass);
    }

    public static <T> T getBean(String beanName) {
        return (T) applicationContext.getBean(beanName);
    }

    public static <T> T getBean(Class<T> beanClass,Class<?>... genericClasses) {
        // 获取BeanFactory
        GenericApplicationContext genericApplicationContext = (GenericApplicationContext) applicationContext;
        ListableBeanFactory beanFactory = genericApplicationContext.getBeanFactory();
        // 获取类型为beanClass的BeanName数组
        String[] beanNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory,beanClass);
        for (String beanName:beanNames) {
            Object beanObj = getBean(beanName);
            ResolvableType beanType = ResolvableType.forClass(beanObj.getClass());
            ResolvableType requiredType = ResolvableType.forClassWithGenerics(beanClass,genericClasses);

            if (requiredType.isAssignableFrom(beanType)) {
                return (T) beanObj;
            }
        }
        return null;
    }

    public static <T> List<T> getBeans(Class<T> beanClass) {
        // 获取BeanFactory
        GenericApplicationContext genericApplicationContext = (GenericApplicationContext) applicationContext;
        ListableBeanFactory beanFactory = genericApplicationContext.getBeanFactory();
        // 获取类型为beanClass的BeanName数组
        String[] beanNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory,beanClass);
        return Stream.of(beanNames)
                .map(beanName -> (T)getBean(beanName))
                .collect(Collectors.toList());
    }
}
