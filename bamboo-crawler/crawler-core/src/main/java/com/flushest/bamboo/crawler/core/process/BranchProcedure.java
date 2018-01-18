package com.flushest.bamboo.crawler.core.process;

import com.flushest.bamboo.crawler.core.ThreadLocalManager;
import com.flushest.bamboo.framework.util.Assert;

/**
 * Created by Administrator on 2018/1/18 0018.
 */
public class BranchProcedure implements Procedure {
    private String field;
    private String operation;
    private String threshold;

    public BranchProcedure(String field, String operation, String threshold) {
        Assert.notNull(field, "field must be not null");
        Assert.notNull(operation, "operation must be not null");
        Assert.notNull(threshold, "threshold must be not null");

        this.field = field;
        this.operation = operation;
        this.threshold = threshold;

    }

    @Override
    public boolean process(Object item) {
        Object value = ThreadLocalManager.contextThreadLocalManager.get().get(field);
        if(value instanceof String) {
            String strValue = (String) value;
            int compareResult = strValue.compareTo(threshold);
            switch (operation) {
                case ">":
                    return compareResult > 0;
                case "<":
                    return compareResult < 0;
                case "=":
                    return compareResult == 0;
                case "<=":
                    return compareResult <= 0;
                case ">=":
                    return compareResult >= 0;
                    default:
                        throw new UnsupportedOperationException(String.format("不支持的operation:%s", operation));
            }
        }

        return false;
    }
}
