package com.flushest.bamboo.runtime.util;

import com.flushest.bamboo.runtime.common.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/10/14 0014.
 */
@Component
public class ConfigCoreUtil {
    /**
     * dubbo application config
     */
    private static final String APPLICATION_NAME = "dubbo.application.name";
    private static final String APPLICATION_OWNER = "dubbo.application.owner";

    /**
     * dubbo registry config
     */
    private static final String REGISTRY_ADDRESS = "dubbo.registry.address";
    private static final String PROTOCOL_NAME = "dubbo.protocol.name";
    private static final String PROTOCOL_PORT = "dubbo.protocol.port";

    private static Configuration configuration;

    @Autowired
    public void setConfigurationService(Configuration configuration) {
        ConfigCoreUtil.configuration = configuration;
    }

    public static String getApplicationName() {
        return configuration.get(APPLICATION_NAME);
    }

    public static String getApplicationOwner() {
        return configuration.get(APPLICATION_OWNER);
    }

    public static String getProtocolName() {
        return configuration.get(PROTOCOL_NAME);
    }

    public static int getProtocolPort() {
        return configuration.get(PROTOCOL_PORT,Integer.class);
    }

    public static String getRegistryAddress() {
        return configuration.get(REGISTRY_ADDRESS);
    }
}
