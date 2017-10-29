package com.flushest.bamboo.runtime.common.persistence;

import com.flushest.bamboo.runtime.exception.BambooRuntimeException;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2017/10/19 0019.
 */
public class SqlSessionFactoryProxy {
    private static final Logger logger = LoggerFactory.getLogger(SqlSessionFactoryProxy.class);

    private static Map<String,SqlSessionFactory> tablePrefixAndSqlSessionFactoryMap = new ConcurrentHashMap<>();

    private static CCJSqlParserManager parserManager = new CCJSqlParserManager();
    private static TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();

    public static void registerSqlSessionFactory(String tablePrefix,SqlSessionFactory sqlSessionFactory) {
        tablePrefixAndSqlSessionFactoryMap.put(tablePrefix,sqlSessionFactory);
    }

    public static SqlSessionFactory getSqlSessionFactory(String statementId, Object parameterObject) throws JSQLParserException {
        for(Map.Entry<String,SqlSessionFactory> entry : tablePrefixAndSqlSessionFactoryMap.entrySet()) {
            String tablePrefix = entry.getKey();
            SqlSessionFactory sqlSessionFactory = entry.getValue();

            String sql = sqlSessionFactory.getConfiguration().getMappedStatement(statementId).getBoundSql(parameterObject).getSql();

            Statement statement = parserManager.parse(new StringReader(sql));
            List<String> tableNames = tablesNamesFinder.getTableList(statement);

            if(tableNames!=null&&!tableNames.isEmpty()&&tableNames.get(0).toLowerCase().startsWith(tablePrefix.toLowerCase())) {
                return sqlSessionFactory;
            }
        }
        throw new BambooRuntimeException(String.format("can not find any sqlSessionFactory for statement[%s]",statementId));
    }

}