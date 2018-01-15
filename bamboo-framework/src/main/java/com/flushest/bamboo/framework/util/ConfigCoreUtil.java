package com.flushest.bamboo.framework.util;

import com.flushest.bamboo.framework.config.Configurations;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2017/10/14 0014.
 */
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

    protected static Configurations configurations;

    public static void setConfigurationService(Configurations configurations) {
        ConfigCoreUtil.configurations = configurations;
    }

    public static String getApplicationName() {
        return configurations.get(APPLICATION_NAME);
    }

    public static String getApplicationOwner() {
        return configurations.get(APPLICATION_OWNER);
    }

    public static String getProtocolName() {
        return configurations.get(PROTOCOL_NAME);
    }

    public static int getProtocolPort() {
        return configurations.get(PROTOCOL_PORT,Integer.class);
    }

    public static String getRegistryAddress() {
        return configurations.get(REGISTRY_ADDRESS);
    }
}
