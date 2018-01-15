package com.flushest.bamboo.framework.util;

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
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

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
}
