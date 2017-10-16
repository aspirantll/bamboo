package com.flushest.bamboo.runtime.util;

import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/14 0014.
 */
public class ClassUtil {
    /**
     * 获取泛型class
     * @param clazz 类
     * @param index 泛型在列表中位置
     * @return
     */
    public static <T> Class<T> getGenericClass(Class clazz,int index) {
        ResolvableType[] types = getGenericTypes(clazz);
        if (index>=types.length) {
            throw new IllegalArgumentException("the value of argument [index] is out of bounds in method:ClassUtil.getGenericClass().");
        }
        return (Class<T>) types[index].getType();
    }

    /**
     * 获取泛型数组
     * @param clazz
     * @return
     */
    public static ResolvableType[] getGenericTypes(Class clazz) {
        Assert.notNull(clazz,"the argument [clazz] can not be null in method:ClassUtil.getGenericClass().");
        return ResolvableType.forType(clazz).getGenerics();
    }
}
