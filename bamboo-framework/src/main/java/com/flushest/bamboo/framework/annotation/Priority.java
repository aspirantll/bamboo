package com.flushest.bamboo.framework.annotation;

/**
 * Created by Administrator on 2018/1/14 0014.
 */
public @interface Priority {
    int value() default Integer.MIN_VALUE;
}
