package com.flushest.bamboo.framework.util;

import com.flushest.bamboo.common.framework.exception.BambooRuntimeException;
import com.flushest.bamboo.framework.resource.ResourceResolverUtil;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2017/10/14 0014.
 */
public class ClassUtil extends ClassUtils{

    private static final MetadataReaderFactory metadataReaderFactory = new SimpleMetadataReaderFactory();

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

    public static <T> Class<T> getGenericClass(Object obj, Class superClass, int index) {
        return getGenericClass(obj.getClass(), superClass, index);
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

    /**
     * 扫描文件资源
     * @param path
     * @return
     */
    public static Resource[] scanFile(String path) throws IOException {
        List<Resource> resources = new ArrayList<>();

        Enumeration<URL> urls = ClassUtil.class.getClassLoader().getResources(path);
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            resources.add(new UrlResource(url));
        }

        return resources.toArray(new Resource[resources.size()]);
    }

    /**
     * 扫描包路径
     * @param packages
     * @return
     * @throws IOException
     */
    public static Resource[] scanPackage(String... packages) throws IOException {

        List<Resource> resourceList = new ArrayList<>();
        for(String scanPackage : packages) {
            Resource[] resources  = ResourceResolverUtil.getResource("classpath*:"+ClassUtil.convertClassNameToResourcePath(scanPackage)+"/**/*.class");
            resourceList.addAll(Arrays.asList(resources));
        }
        return resourceList.toArray(new Resource[resourceList.size()]);
    }

    /**
     * 获取元数据读取器
     * @param className
     * @return
     * @throws IOException
     */
    public static MetadataReader getMetadataReader(String className) throws IOException {
        return metadataReaderFactory.getMetadataReader(className);
    }

    public static MetadataReader getMetadataReader(Resource resource) throws IOException {
        return metadataReaderFactory.getMetadataReader(resource);
    }

    public static <T> T assemblyClass(T targetObj, Map<String, Object> propertiesMap) {
        Class targetClass = targetObj.getClass();
        for (Map.Entry<String, Object> entry : propertiesMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            String setMethodName = "set" + StringUtil.upperCaseInitial(key);
            try {
                List<Method> methods = findMethodsByName(targetClass, setMethodName, 1);
                if (methods.isEmpty()) {
                    continue;
                }
                methods.get(0).invoke(targetObj, TypeConverter.convert(value, methods.get(0).getParameterTypes()[0]));
            } catch (IllegalAccessException e) {
                throw new BambooRuntimeException("failed to assembly class[" + targetClass.getName() + "], propertiesMap:" + propertiesMap, e);
            } catch (InvocationTargetException e) {
                throw new BambooRuntimeException("failed to assembly class[" + targetClass.getName() + "], propertiesMap:" + propertiesMap, e);
            }
        }
        return targetObj;
    }

    public static void invoke(Object object, String methodName, Object... args) {
        try {
            Method method = object.getClass().getMethod(methodName);
            method.invoke(object, args);
        } catch (NoSuchMethodException e) {
            throw new BambooRuntimeException("no such method[" + methodName + "] in class[" + object.getClass().getName() + "]", e);
        } catch (IllegalAccessException e) {
            throw new BambooRuntimeException("cannot access method[" + methodName + "] in class[" + object.getClass().getName() + "]", e);
        } catch (InvocationTargetException e) {
            throw new BambooRuntimeException("cannot invoke method[" + methodName + "] in class[" + object.getClass().getName() + "]", e);
        }
    }

    public static List<Method> findMethodsByName(Class clazz, String methodName) {
        List<Method> result = new ArrayList<>();
        for(Method method : clazz.getMethods()) {
            if(method.getName().equals(methodName)) {
                result.add(method);
            }
        }
        return result;
    }

    public static List<Method> findMethodsByName(Class clazz, String methodName, int paramCount) {
        return findMethodsByName(clazz, methodName)
                .stream()
                .filter(m -> m.getParameterTypes().length == paramCount)
                .collect(Collectors.toList());
    }
    }
