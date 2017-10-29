package com.flushest.bamboo.runtime.annotation;

import com.flushest.bamboo.runtime.common.persistence.enums.GenerateStrategy;

/**
 * Created by Administrator on 2017/10/28 0028.
 */
public @interface Id {
    GenerateStrategy strategy() default GenerateStrategy.ASSIGNED;
}
