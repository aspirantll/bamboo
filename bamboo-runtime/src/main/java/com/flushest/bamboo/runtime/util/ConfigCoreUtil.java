package com.flushest.bamboo.runtime.util;

import com.flushest.bamboo.runtime.service.IConfigurationService;
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

    private static IConfigurationService configurationService;

    @Autowired
    public void setConfigurationService(IConfigurationService configurationService) {
        ConfigCoreUtil.configurationService = configurationService;
    }

    public static String getApplicationName() {
        return configurationService.get(APPLICATION_NAME);
    }

    public static String getApplicationOwner() {
        return configurationService.get(APPLICATION_OWNER);
    }

    public static String getProtocolName() {
        return configurationService.get(PROTOCOL_NAME);
    }

    public static int getProtocolPort() {
        return configurationService.get(PROTOCOL_PORT,Integer.class);
    }

    public static String getRegistryAddress() {
        return configurationService.get(REGISTRY_ADDRESS);
    }
}
