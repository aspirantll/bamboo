package com.flushest.bamboo.crawler.core.frontier;

import com.flushest.bamboo.common.framework.exception.BambooRuntimeException;
import com.flushest.bamboo.framework.util.ClassUtil;
import com.flushest.bamboo.framework.util.PriorityComparator;
import org.springframework.core.io.Resource;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Administrator on 2018/1/14 0014.
 */
public class ResourceManagerFactory {

    private static final ConcurrentMap<Class<?>, ResourceManager> resourceManagers = new ConcurrentHashMap<>();

    private static Map<Class<?>, Class<?>> cachedClasses;

    private static final String scanPackage = "com.flushest.bamboo";
    public static <T> ResourceManager<T> getResourceManager(Class<T> clazz) {
        ResourceManager resourceManager = resourceManagers.get(clazz);
        if(resourceManager == null) {
            resourceManagers.putIfAbsent(clazz, createResourceManager(clazz));
            resourceManager = resourceManagers.get(clazz);
        }

        return resourceManager;
    }

    private static ResourceManager createResourceManager(Class<?> clazz) {
        loadClasses();
        Class<?> type = cachedClasses.get(clazz);
        if(type == null) {
            throw new BambooRuntimeException(String.format("unsupported genericClass[%s]", clazz.getName()));
        }
        try {
            return (ResourceManager)type.newInstance();
        } catch (IllegalAccessException|InstantiationException e) {
            throw new BambooRuntimeException(String.format("failed to newInstance for class [%s]", type.getName()), e);
        }
    }

    private static void loadClasses() {
        if(cachedClasses == null) {
            synchronized (ResourceManagerFactory.class) {
                if(cachedClasses == null) {
                    cachedClasses = new HashMap<>();
                    try {
                        Resource[] resources = ClassUtil.scanPackage(scanPackage);
                        for(Resource resource : resources) {
                            MetadataReader metadataReader = ClassUtil.getMetadataReader(resource);
                            ClassMetadata classMetadata = metadataReader.getClassMetadata();
                            if(!classMetadata.isInterface()&&!classMetadata.isAbstract()) {
                                Class<?> clazz = ClassUtil.forName(classMetadata.getClassName(),ResourceManagerFactory.class.getClassLoader());
                                if(ResourceManager.class.isAssignableFrom(clazz)) {
                                    Class<?> genericClass = ClassUtil.getGenericClass(clazz, ResourceManager.class, 0);
                                    Class<?> existClass = cachedClasses.get(genericClass);
                                    if(existClass == null || PriorityComparator.priorThan(clazz, existClass)) {
                                        cachedClasses.put(genericClass, clazz);
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        throw new BambooRuntimeException(String.format("failed to scan package[%s]", scanPackage), e);
                    } catch (ClassNotFoundException e) {
                        throw new BambooRuntimeException(String.format("failed to find class"), e);
                    }
                }
            }
        }
    }
}
