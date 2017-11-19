package com.flushest.bamboo.framework.util;

/**
 * Created by Administrator on 2017/10/21 0021.
 */
public class Assert extends org.springframework.util.Assert {
    public static void notHasText(String string,String message) {
        judge(!StringUtil.hasText(string),message);
    }

    public static void judge(boolean result,String message) {
        if(result) {
            throw new IllegalStateException(message);
        }
    }
}
