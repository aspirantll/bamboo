package com.flushest.bamboo.runtime.common.persistence.definitions;

import com.flushest.bamboo.runtime.common.persistence.enums.GenerateStrategy;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2017/10/28 0028.
 */
public class IdDefinition {
    private Field field;

    private GenerateStrategy strategy;

    private String columnName;

    public IdDefinition() {

    }

    public IdDefinition(Field field, GenerateStrategy strategy) {
        this.field = field;
        this.strategy = strategy;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public GenerateStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(GenerateStrategy strategy) {
        this.strategy = strategy;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public boolean isEmpty() {
        return strategy==null||field==null;
    }
}
