package com.flushest.bamboo.crawler.core.enums;

import com.flushest.bamboo.crawler.core.url.URLParser;
import org.apache.http.protocol.HTTP;

/**
 * Created by Administrator on 2017/11/12 0012.
 */
public enum Protocol {
    UNKNOWN,
    HTTP,
    HTTPS;

    public static Protocol protocol(String url) {
       for(Protocol protocol : values()) {
           if(protocol.name().toLowerCase().equals(URLParser.getProtocol(url))) {
               return protocol;
           }
       }
       return UNKNOWN;
    }
}
