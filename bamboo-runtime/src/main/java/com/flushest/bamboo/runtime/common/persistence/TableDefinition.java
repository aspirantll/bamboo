package com.flushest.bamboo.runtime.common.persistence;

import com.flushest.bamboo.runtime.util.Assert;
import com.flushest.bamboo.runtime.util.ClassUtil;
import com.flushest.bamboo.runtime.util.StringUtil;

import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/10/22 0022.
 */
public class TableDefinition {
    private String prefix;
    private String simpleName;
    private Class<?> targetClass;

    private List<ColumnDefinition> columnDefinitions;

    public TableDefinition(String prefix, Class<?> targetClass, List<ColumnDefinition> columnDefinitions) {
        this(prefix,null,targetClass,columnDefinitions);
    }

    public TableDefinition(String prefix, String simpleName, Class<?> targetClass, List<ColumnDefinition> columnDefinitions) {
        Assert.notNull(targetClass,"targetClass must be not null");
        Assert.notHasText(prefix,"prefix must be not empty");

        this.targetClass = targetClass;
        this.prefix = prefix.trim();

        if(StringUtil.hasText(simpleName)) {
            this.simpleName = simpleName.trim();
        }else {
            this.simpleName = targetClass.getSimpleName();
        }

        if(columnDefinitions==null) {
            this.columnDefinitions = Collections.emptyList();
        }else {
            this.columnDefinitions = columnDefinitions;
        }
    }

    public String getPrefix() {
        return prefix;
    }
    public String getSimpleName() {
        return simpleName;
    }

    public String getTableName() {
        return ClassUtil.convertClassNameToTableName(prefix,targetClass);
    }
}
