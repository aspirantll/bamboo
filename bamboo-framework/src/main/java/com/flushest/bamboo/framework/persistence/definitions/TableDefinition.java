package com.flushest.bamboo.framework.persistence.definitions;

import com.flushest.bamboo.framework.util.Assert;
import com.flushest.bamboo.framework.util.ClassUtil;
import com.flushest.bamboo.framework.util.StringUtil;

import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/10/22 0022.
 */
public class TableDefinition {
    private String prefix;
    private String simpleName;
    private Class<?> targetClass;

    private IdDefinition idDefinition;

    private List<ColumnDefinition> columnDefinitions;

    public TableDefinition(String prefix, Class<?> targetClass, List<ColumnDefinition> columnDefinitions, IdDefinition idDefinition) {
        this(prefix,null,targetClass,columnDefinitions,idDefinition);
    }

    public TableDefinition(String prefix, String simpleName, Class<?> targetClass, List<ColumnDefinition> columnDefinitions, IdDefinition idDefinition) {
        Assert.notNull(targetClass,"targetClass must be not null");
        Assert.notHasText(prefix,"prefix must be not empty");


        this.targetClass = targetClass;
        this.prefix = prefix.trim();

        if(StringUtil.hasText(simpleName)) {
            this.simpleName = simpleName.trim();
        }else {
            this.simpleName = StringUtil.lowerCaseInitial(targetClass.getSimpleName());
        }

        if(columnDefinitions==null) {
            this.columnDefinitions = Collections.emptyList();
        }else {
            this.columnDefinitions = columnDefinitions;
        }

        this.idDefinition = idDefinition;
    }

    public String getPrefix() {
        return prefix;
    }
    public String getSimpleName() {
        return simpleName;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }


    public List<ColumnDefinition> getColumnDefinitions() {
        return columnDefinitions;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    public IdDefinition getIdDefinition() {
        return idDefinition;
    }

    public void setIdDefinition(IdDefinition idDefinition) {
        this.idDefinition = idDefinition;
    }

    public void setColumnDefinitions(List<ColumnDefinition> columnDefinitions) {
        this.columnDefinitions = columnDefinitions;
    }

    private String getSimpleClassName() {
        return StringUtil.lowerCaseInitial(targetClass.getSimpleName());
    }

    public String getMapperName() {
        return simpleName+".mapper";
    }

    public String getParamMapName() {
        return getMapperName()+"."+getSimpleClassName()+"Param";
    }

    public String getResultMapName() {
        return getMapperName()+"."+getSimpleClassName()+"Result";
    }

    public String getTableName() {
        return ClassUtil.convertClassNameToTableName(prefix,targetClass);
    }
}
