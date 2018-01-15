package com.flushest.bamboo.framework.annotation;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2018/1/10 0010.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SPI {
    String name() default "";
}
