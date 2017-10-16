package com.flushest.bamboo.runtime.service;

/**
 * Created by Administrator on 2017/10/14 0014.
 */
public interface IConfigurationService {
    /**
     * 获取配置项
     * @param name
     * @param valueClass 需要转换成的类型
     * @param <T>
     * @return
     */
    <T> T get(String name, Class<T> valueClass);

    String get(String name);
}
