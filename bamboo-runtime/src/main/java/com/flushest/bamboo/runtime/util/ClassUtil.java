package com.flushest.bamboo.runtime.util;

import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/14 0014.
 */
public class ClassUtil extends ClassUtils{
    /**
     * 获取泛型class
     * @param clazz 类
     * @param index 泛型在列表中位置
     * @return
     */
    public static <T> Class<T> getGenericClass(Class clazz,Class superClass,int index) {
        ResolvableType[] types = getGenericTypes(clazz,superClass);
        if (index>=types.length) {
            throw new IllegalArgumentException("the value of argument [index] is out of bounds in method:ClassUtil.getGenericClass().");
        }
        return (Class<T>) types[index].resolve();
    }

    /**
     * 获取泛型数组
     * @param clazz
     * @return
     */
    public static ResolvableType[] getGenericTypes(Class clazz,Class superClass) {
        Assert.notNull(clazz,"the argument [clazz] can not be null in method:ClassUtil.getGenericClass().");
        return ResolvableType.forClass(clazz).as(superClass).getGenerics();
    }

    /**
     * 根据驼峰命名规则将类名转换为数据库表名
     * @param clazz
     * @return
     */
    public static String convertClassNameToTableName(String prefix, Class<?> clazz) {
        String name = clazz.getSimpleName();
        List<String> words = StringUtil.splitNameAccordingCamelCase(name);
        if(StringUtil.hasText(prefix)) {
            words.add(0,prefix);
        }
        return String.join("_",words.toArray(new String[0]));
    }

    /**
     * 根据驼峰命名规则将属性名转换为数据库字段名
     * @param field
     * @return
     */
    public static String convertFieldNameToColumnName(Field field) {
        String name = field.getName();
        List<String> words = StringUtil.splitNameAccordingCamelCase(name);
        return String.join("_",words.toArray(new String[0]));
    }

    /**
     * 转换类名到Mapper名
     * @param clazz
     * @return
     */
    public static String convertClassNameToMapperName(Class<?> clazz) {
        return StringUtil.lowerCaseInitial(clazz.getSimpleName())+".mapper";
    }
}
