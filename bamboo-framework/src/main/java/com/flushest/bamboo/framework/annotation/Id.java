package com.flushest.bamboo.framework.annotation;

import com.flushest.bamboo.framework.persistence.enums.GenerateStrategy;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2017/10/28 0028.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Id {
    GenerateStrategy strategy() default GenerateStrategy.ASSIGNED;
}
