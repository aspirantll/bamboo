package com.flushest.bamboo.runtime.annotation;

import org.apache.ibatis.type.JdbcType;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2017/10/22 0022.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {
    String name() default "";

    JdbcType jdbcType() default JdbcType.OTHER;
}
