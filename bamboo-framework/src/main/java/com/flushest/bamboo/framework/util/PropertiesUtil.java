package com.flushest.bamboo.framework.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Administrator on 2018/1/10 0010.
 */
public class PropertiesUtil {
    public static Map<String, Object> getPropertiesFromInputStream(Map<String, Object> map, InputStream is) throws IOException {
        Assert.notNull(map, "map must be not null");
        Assert.notNull(is, "inputstream must be not null");
        Properties p = new Properties();
        p.load(is);

        for(String propertyName : p.stringPropertyNames()) {
            String propertyValue = p.getProperty(propertyName);
            if(!map.containsKey(propertyName)&&StringUtil.hasText(propertyName)) {
                map.put(propertyName, propertyValue.trim());
            }
        }

        is.close();
        return map;
    }
}
