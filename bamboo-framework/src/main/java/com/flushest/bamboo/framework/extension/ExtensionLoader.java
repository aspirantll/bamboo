package com.flushest.bamboo.framework.extension;

import com.flushest.bamboo.common.framework.exception.BambooRuntimeException;
import com.flushest.bamboo.framework.annotation.SPI;
import com.flushest.bamboo.framework.util.Assert;
import com.flushest.bamboo.framework.util.ClassUtil;
import com.flushest.bamboo.framework.util.PriorityComparator;
import com.flushest.bamboo.framework.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Administrator on 2018/1/10 0010.
 */
public class ExtensionLoader<T> {
    private static final Logger logger = LoggerFactory.getLogger(ExtensionLoader.class);

    private static ConcurrentMap<Class, ExtensionLoader> cachedExtensionLoaders = new ConcurrentHashMap<>();

    private String BAMBOO_DIRECTORY = "/META-INF/bamboo/";

    private Class<?> type;

    private Map<String,Class> cachedClasses;

    public static <T> ExtensionLoader<T> getExtensionLoader(Class<?> type) {
        Assert.notNull(type, "type must be not null");
        if(!type.isInterface()) {
            throw new IllegalArgumentException(String.format("type[%s] must be a interface", type.getName()));
        }

        if(!type.isAnnotationPresent(SPI.class)) {
            throw new IllegalArgumentException(String.format("type[%s] without the annotation @SPI[%s]", type.getName(), SPI.class.getName()));
        }

        ExtensionLoader extensionLoader = cachedExtensionLoaders.get(type);
        if(extensionLoader == null) {
            extensionLoader = new ExtensionLoader(type);
            cachedExtensionLoaders.put(type, extensionLoader);
        }

        return extensionLoader;
    }

    private ExtensionLoader(Class<?> type) {
        this.type = type;
    }

    public Collection<T> getExtensionCollection() {
        return getExtensions().values();
    }

    public T getExtension(String key) {
        return getExtensions(key).get(key);
    }

    public Map<String, T> getExtensions(String... keys) {
        loadClasses();

        Map<String, T> extensionMap = new HashMap<String, T>();
        if(keys.length == 0) {
            Set<String> keySet = cachedClasses.keySet();
            keys = keySet.toArray(new String[keySet.size()]);
        }

        for(String key : keys) {
            Class<T> clazz = cachedClasses.get(key);
            if(clazz == null) {
                logger.warn(String.format("the key[%s] cannot found in the map of cachedClasses", key));
            }else {
                Throwable throwable = null;
                try {
                    T instance = clazz.newInstance();
                    extensionMap.put(key, instance);
                } catch (IllegalAccessException e) {
                    throwable = e;
                } catch (InstantiationException e) {
                    throwable = e;
                }

                if(throwable != null) {
                    throw new BambooRuntimeException("occurred error in the function: getExtension", throwable);
                }
            }
        }
        return extensionMap;
    }

    public void loadClasses() {
        if(cachedClasses!=null) {
            return;
        }else {
            cachedClasses = new HashMap<>();
        }

        String filePath = BAMBOO_DIRECTORY + type.getName();
        try {
            Map<String, Object> map = new HashMap<>();
            Resource[] resources = ClassUtil.scanFile(filePath);
            for(Resource resource : resources) {
                PropertiesUtil.getPropertiesFromInputStream(map,resource.getInputStream());
            }

            for(Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = (String) entry.getValue();

                try {
                    Class clazz = Class.forName(value);
                    if(!type.isAssignableFrom(clazz)) {
                        throw new BambooRuntimeException(String.format("the class[%s] must implement the interface[%s]", clazz.getName(), type.getName()));
                    }
                    Class existClass = cachedClasses.get(key);
                    if(existClass == null || PriorityComparator.priorThan(clazz, existClass)) {
                        cachedClasses.put(key, clazz);
                    }
                } catch (ClassNotFoundException e) {
                    throw new BambooRuntimeException(String.format("cannot found class[%s]", value),e);
                }
            }
        } catch (IOException e) {
            throw new BambooRuntimeException(String.format("failed to scan file[%s]", filePath), e);
        }
    }

}
