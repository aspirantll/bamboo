package com.flushest.bamboo.runtime.util;

/**
 * Created by Administrator on 2017/10/21 0021.
 */
public class Assert extends org.springframework.util.Assert {
    public static void notHasText(String string,String message) {
        if(!StringUtil.hasText(string)) {
            throw new IllegalStateException(message);
        }
    }
}
