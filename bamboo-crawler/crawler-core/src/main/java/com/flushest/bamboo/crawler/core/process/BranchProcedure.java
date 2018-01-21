package com.flushest.bamboo.crawler.core.process;

import com.flushest.bamboo.common.framework.exception.BambooRuntimeException;
import com.flushest.bamboo.crawler.core.ThreadLocalManager;
import com.flushest.bamboo.framework.util.Assert;

import java.util.regex.Pattern;


/**
 * Created by Administrator on 2018/1/18 0018.
 */
public class BranchProcedure implements Procedure {

    private String field;
    private String operation;
    private String threshold;

    private ElementSelector elementSelector;

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
        return assertResult();
    }

    private boolean assertResult() {
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
                case "!=":
                    return compareResult != 0;
                case "like":
                    Pattern pattern = Pattern.compile(threshold.replace("%", ".*"));
                    return pattern.matcher(strValue).find();
                default:
                    throw new UnsupportedOperationException(String.format("不支持的operation:%s", operation));
            }
        }

        throw new BambooRuntimeException(String.format("不支持的比较类型:%s", value));
    }
}
