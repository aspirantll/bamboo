package com.flushest.bamboo.framework.resource;

import com.flushest.bamboo.common.framework.exception.BambooRuntimeException;
import com.flushest.bamboo.framework.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/9 0009.
 */
public class ResourceResolverUtil {
    private static final Logger logger = LoggerFactory.getLogger(ResourceResolverUtil.class);

    private static final Map<String,ResourceResolver> protocolAndResolverMap = new HashMap<>();

    public static void registerResolver(ResourceResolver resourceResolver) {
        String[] protocols = resourceResolver.getProtocols();

        for( String protocol : protocols) {
            if(protocolAndResolverMap.containsKey(protocol)) {
                throw new BambooRuntimeException(String.format("协议:%s 解析器已经被注册，请检查类%s",protocol,resourceResolver.getClass().getName()));
            }
            protocolAndResolverMap.put(protocol,resourceResolver);
            logger.info(String.format("协议:%s 解析器注册成功，解析器->%s",protocol,resourceResolver.getClass().getName()));
        }
    }

    public static void registerResolvers(List<ResourceResolver> resourceResolvers) {
        Assert.notNull(resourceResolvers,"非法参数:resourceResolver，不能传null");
        resourceResolvers.forEach((resourceResolver -> registerResolver(resourceResolver)));
    }

    public static <T> T getResource(WebURL url) throws IOException {
        String protocol = url.getProtocol();
        if(!protocolAndResolverMap.containsKey(protocol)) {
            throw new BambooRuntimeException(String.format("协议:%s 没有注册，请检查url:%s",protocol,url.getUrl()));
        }
        ResourceResolver resolver = protocolAndResolverMap.get(url.getProtocol());
        return (T) resolver.resolve(url);
    }

    public static <T> T getResource(String url) throws IOException {
        return getResource(new WebURL(url));
    }
}
