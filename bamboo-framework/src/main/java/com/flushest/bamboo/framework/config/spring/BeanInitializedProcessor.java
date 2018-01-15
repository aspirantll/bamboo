package com.flushest.bamboo.framework.config.spring;

import com.flushest.bamboo.framework.resource.ResourceResolver;
import com.flushest.bamboo.framework.resource.ResourceResolverUtil;
import com.flushest.bamboo.framework.util.SpringUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/12/9 0009.
 * Bean初始化时进行操作
 */
@Component
public class BeanInitializedProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        initialResourceResolverUtil(bean);
        return bean;
    }

    /**
     * 初始化资源解析工具
     */
    private void initialResourceResolverUtil(Object bean) {
        if(bean instanceof ResourceResolver) {
            ResourceResolverUtil.registerResolver((ResourceResolver) bean);
        }
    }
}
