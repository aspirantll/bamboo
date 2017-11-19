package com.flushest.bamboo.framework.persistence.definitions;

import com.flushest.bamboo.framework.util.Assert;
import com.flushest.bamboo.framework.util.ClassUtil;
import com.flushest.bamboo.framework.util.StringUtil;
import org.apache.ibatis.type.JdbcType;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/22 0022.
 */
public class ColumnDefinition {

    private static final Map<Class<?>,JdbcType> classAndJdbcTypeMap = new HashMap<>();
    static {
        classAndJdbcTypeMap.put(String.class,JdbcType.VARCHAR);
        classAndJdbcTypeMap.put(Integer.TYPE,JdbcType.INTEGER);
        classAndJdbcTypeMap.put(Long.TYPE,JdbcType.BIGINT);
        classAndJdbcTypeMap.put(Float.TYPE,JdbcType.FLOAT);
        classAndJdbcTypeMap.put(Double.TYPE,JdbcType.NUMERIC);
        classAndJdbcTypeMap.put(Date.class,JdbcType.DATE);
    }


    private String name;
    private JdbcType jdbcType;
    private Field field;


    public ColumnDefinition(String name, JdbcType jdbcType, Field field) {
        Assert.notNull(field,"field must be not null");

        this.name = StringUtil.hasText(name)?name.trim(): ClassUtil.convertFieldNameToColumnName(field);
        this.jdbcType = jdbcType.equals(JdbcType.OTHER)?classAndJdbcTypeMap.get(field.getType()):jdbcType;
        this.field = field;
    }

    public String getName() {
        return name;
    }

    public JdbcType getJdbcType() {
        return jdbcType;
    }

    public Field getField() {
        return field;
    }
}
