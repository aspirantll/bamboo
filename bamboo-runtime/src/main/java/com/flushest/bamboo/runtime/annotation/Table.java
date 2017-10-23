package com.flushest.bamboo.runtime.annotation;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2017/10/22 0022.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Table {

    String tablePrefix();

    String simpleName() default "";
}
