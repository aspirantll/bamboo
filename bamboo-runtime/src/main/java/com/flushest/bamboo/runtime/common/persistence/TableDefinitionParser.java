package com.flushest.bamboo.runtime.common.persistence;

import com.flushest.bamboo.runtime.common.persistence.definitions.ColumnDefinition;
import com.flushest.bamboo.runtime.common.persistence.definitions.IdDefinition;
import com.flushest.bamboo.runtime.common.persistence.definitions.TableDefinition;
import com.flushest.bamboo.runtime.util.Assert;
import com.flushest.bamboo.runtime.util.StringUtil;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
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
        parseDeleteMappedStatement();
        parseUpdateMappedStatement();
        parseQueryMappedStatement();
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

    private void parseDeleteMappedStatement() {
        if(!idDefinition.isEmpty()) {//不存在指定id便不添加删除statement
            List<ParameterMapping> parameterMappings = new ArrayList<>();
            Field idField = idDefinition.getField();
            parameterMappings.add(new ParameterMapping.Builder(configuration,idField.getName(),idField.getType()).build());

            String deleteSql = generateDeleteSql();
            StaticSqlSource sqlSource = new StaticSqlSource(configuration,deleteSql,parameterMappings);

            MappedStatement.Builder deleteMsBuilder = new MappedStatement.Builder(configuration,tableDefinition.getMapperName()+".delete",sqlSource,SqlCommandType.DELETE);

            configuration.addMappedStatement(deleteMsBuilder.build());
        }
    }

    private String generateDeleteSql() {
        StringBuffer deleteSql = new StringBuffer();
        deleteSql.append("delete from ")
                .append(tableName)
                .append(" where ")
                .append(idDefinition.getColumnName())
                .append("=?");
        return deleteSql.toString();
    }

    private void parseUpdateMappedStatement() {
        if(!idDefinition.isEmpty()) {//不存在指定id便不添加更新statement
            String updateXml = generateUpdateSql();
            XPathParser pathParser = new XPathParser(updateXml);
            LanguageDriver languageDriver = new XMLLanguageDriver();
            SqlSource sqlSource = languageDriver.createSqlSource(configuration,pathParser.evalNode("/update"),tableClass);
            MappedStatement.Builder updateMsBuilder = new MappedStatement.Builder(configuration,tableDefinition.getMapperName()+".update",sqlSource,SqlCommandType.UPDATE);
            configuration.addMappedStatement(updateMsBuilder.build());
        }

    }

    private String generateUpdateSql() {
        StringBuffer updateSql = new StringBuffer();
        updateSql.append("<update id=\"update\" parameterMap=\"")
                .append(tableDefinition.getParamMapName())
                .append("\">update ")
                .append(tableName)
                .append(" <set> ");

        for (ColumnDefinition columnDefinition : columnDefinitions) {
            Field field = columnDefinition.getField();

            if(idDefinition.getField().equals(field)) continue;

            updateSql.append("<if test=\"")
                    .append(field.getName())
                    .append("!=null\">")
                    .append(columnDefinition.getName())
                    .append("=#{")
                    .append(field.getName())
                    .append(",jdbcType=")
                    .append(columnDefinition.getJdbcType().name())
                    .append("},")
                    .append("</if> ");
        }

        updateSql.append("</set>");

        Field idField = idDefinition.getField();

        updateSql.append(" where ")
                .append(idDefinition.getColumnName())
                .append("=#{")
                .append(idField.getName())
                .append("}");

        updateSql.append("</update>");

        return updateSql.toString();
    }

    private void parseQueryMappedStatement() {
        if(!idDefinition.isEmpty()) {//不存在指定id便不添加查询statement
            List<ParameterMapping> parameterMappings = new ArrayList<>();
            Field idField = idDefinition.getField();
            parameterMappings.add(new ParameterMapping.Builder(configuration,idField.getName(),idField.getType()).build());

            String querySql = generateQuerySql();
            StaticSqlSource sqlSource = new StaticSqlSource(configuration,querySql,parameterMappings);

            MappedStatement.Builder queryMsBuilder = new MappedStatement.Builder(configuration,tableDefinition.getMapperName()+".query",sqlSource,SqlCommandType.DELETE);
            queryMsBuilder.resultMaps(Arrays.asList(new ResultMap[]{configuration.getResultMap(tableDefinition.getResultMapName())}));

            configuration.addMappedStatement(queryMsBuilder.build());
        }
    }

    private String generateQuerySql() {
        StringBuffer querySql = new StringBuffer();
        querySql.append("select * from ")
                .append(tableName)
                .append(" where ")
                .append(idDefinition.getColumnName())
                .append("=?");
        return querySql.toString();
    }

}
