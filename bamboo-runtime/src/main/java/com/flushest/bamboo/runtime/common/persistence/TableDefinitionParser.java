package com.flushest.bamboo.runtime.common.persistence;

import com.flushest.bamboo.runtime.common.persistence.definitions.ColumnDefinition;
import com.flushest.bamboo.runtime.common.persistence.definitions.IdDefinition;
import com.flushest.bamboo.runtime.common.persistence.definitions.TableDefinition;
import com.flushest.bamboo.runtime.util.Assert;
import com.flushest.bamboo.runtime.util.StringUtil;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/10/28 0028.
 */
public class TableDefinitionParser {
    private Configuration configuration;
    private TableDefinition tableDefinition;

    private Class<?> tableClass;
    private String tableName;
    private String simpleClassName;
    private String mapperName;
    private List<ColumnDefinition> columnDefinitions;
    private IdDefinition idDefinition;

    public TableDefinitionParser(Configuration configuration,TableDefinition tableDefinition) {
        Assert.notNull(configuration,"configuration must be not null");
        Assert.notNull(tableDefinition,"tableDefinition must be not null");

        this.configuration = configuration;
        this.tableDefinition = tableDefinition;
    }

    public void parse() {
        prepare();
        parseParameterMap();
        parseResultMap();
        parseInsertMappedStatement();
    }

    private void prepare() {
        tableClass = tableDefinition.getTargetClass();
        tableName = tableDefinition.getTableName();
        simpleClassName = StringUtil.lowerCaseInitial(tableClass.getSimpleName());
        mapperName = simpleClassName+".mapper";
        columnDefinitions = tableDefinition.getColumnDefinitions();
        idDefinition = tableDefinition.getIdDefinition();
    }

    private void parseParameterMap() {

        List<ParameterMapping> parameterMappings = new ArrayList<>();
        for(ColumnDefinition columnDefinition : columnDefinitions) {
            Field field = columnDefinition.getField();

            ParameterMapping.Builder parameterMappingBuilder = new ParameterMapping.Builder(configuration,field.getName(),field.getType());
            parameterMappings.add(parameterMappingBuilder.build());
        }

        ParameterMap parameterMap = new ParameterMap.Builder(configuration,tableDefinition.getParamMapName(),tableClass, parameterMappings).build();
        configuration.addParameterMap(parameterMap);
    }

    private void parseResultMap() {
        List<ResultMapping> resultMappings = new ArrayList<>();
        for(ColumnDefinition columnDefinition : columnDefinitions) {
            Field field = columnDefinition.getField();

            ResultMapping.Builder resultMappingBuilder = new ResultMapping.Builder(configuration,field.getName(),columnDefinition.getName(),field.getType());
            resultMappingBuilder.jdbcType(columnDefinition.getJdbcType());

            resultMappings.add(resultMappingBuilder.build());
        }

        ResultMap resultMap = new ResultMap.Builder(configuration,tableDefinition.getResultMapName(),tableClass,resultMappings).build();
        configuration.addResultMap(resultMap);
    }

    private void parseInsertMappedStatement() {
        String insertSql = generateInsertSql();
        SqlSource sqlSource = new StaticSqlSource(configuration,insertSql,configuration.getParameterMap(tableDefinition.getParamMapName()).getParameterMappings());

        MappedStatement.Builder insertMsBuilder = new MappedStatement.Builder(configuration,tableDefinition.getMapperName()+".insert",sqlSource,SqlCommandType.INSERT);

        if(idDefinition!=null&&!idDefinition.isEmpty()) {
            String keySql = generateKeySql();
            if(StringUtil.hasText(keySql)) {
                String id = mapperName+".selectKey";
                SqlSource keySqlSource = new StaticSqlSource(configuration,keySql);

                MappedStatement.Builder keyMsBuilder = new MappedStatement.Builder(configuration,id,keySqlSource,SqlCommandType.SELECT);
                keyMsBuilder.resultMaps(Arrays.asList(new ResultMap[]{configuration.getResultMap("string")}));
                keyMsBuilder.keyProperty(idDefinition.getField().getName());

                SelectKeyGenerator keyGenerator = new SelectKeyGenerator(keyMsBuilder.build(),true);
                insertMsBuilder.keyGenerator(keyGenerator);
            }
        }

        configuration.addMappedStatement(insertMsBuilder.build());

    }

    private String generateInsertSql() {
        StringBuffer cols = new StringBuffer();
        StringBuffer values = new StringBuffer();

        for(ColumnDefinition columnDefinition : columnDefinitions) {
            cols.append(columnDefinition.getName()).append(",");
            values.append("?,");
        }

        cols.deleteCharAt(cols.length()-1);
        values.deleteCharAt(values.length()-1);

        StringBuffer sqlBuffer = new StringBuffer();

        sqlBuffer.append("insert into ")
                .append(tableName)
                .append("(")
                .append(cols)
                .append(") ")
                .append("values(")
                .append(values)
                .append(")");

        return sqlBuffer.toString();
    }

    private String generateKeySql() {
        String keySql;
        switch (idDefinition.getStrategy()) {
            case ASSIGNED:
                keySql="";
                break;
            case UUID:
                keySql = "select replace(uuid(),'-','') from dual";
                break;
            case SEQUENCE:
                keySql = "select s_"+tableDefinition.getTableName()+".nextval from dual";
                break;
            default:
                keySql="";
                break;
        }
        return keySql;
    }
}
