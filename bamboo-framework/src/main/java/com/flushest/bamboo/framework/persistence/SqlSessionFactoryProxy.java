package com.flushest.bamboo.framework.persistence;

import com.flushest.bamboo.common.framework.exception.BambooRuntimeException;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2017/10/19 0019.
 */
public class SqlSessionFactoryProxy {
    private static final Logger logger = LoggerFactory.getLogger(SqlSessionFactoryProxy.class);

    private static Map<String,SqlSessionFactory> tablePrefixAndSqlSessionFactoryMap = new ConcurrentHashMap<>();
    private static Map<String,DataSource> tablePrefixAndDataSourceMap = new ConcurrentHashMap<>();

    private static CCJSqlParserManager parserManager = new CCJSqlParserManager();
    private static TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();

    public static void registerSqlSessionFactory(String tablePrefix,SqlSessionFactory sqlSessionFactory) {
        tablePrefixAndSqlSessionFactoryMap.put(tablePrefix,sqlSessionFactory);
    }

    public static void registerDataSource(String tablePrefix,DataSource dataSource) {
        tablePrefixAndDataSourceMap.put(tablePrefix,dataSource);
    }

    public static Map<String,DataSource> getDataSourceMap() {
        return tablePrefixAndDataSourceMap;
    }

    public static SqlSessionFactory getSqlSessionFactory(String statementId, Object parameterObject) throws JSQLParserException, SQLException {
        for(Map.Entry<String,SqlSessionFactory> entry : tablePrefixAndSqlSessionFactoryMap.entrySet()) {
            String tablePrefix = entry.getKey();
            SqlSessionFactory sqlSessionFactory = entry.getValue();

            String sql = sqlSessionFactory.getConfiguration().getMappedStatement(statementId).getBoundSql(parameterObject).getSql();

            Statement statement = parserManager.parse(new StringReader(sql));
            List<String> tableNames = tablesNamesFinder.getTableList(statement);

            if(tableNames!=null&&!tableNames.isEmpty()&&tableNames.get(0).toLowerCase().startsWith(tablePrefix.toLowerCase())) {
                DataSource dataSource = tablePrefixAndDataSourceMap.get(tablePrefix);
                TransactionInfoUtil.changeDataSource(dataSource);
                return sqlSessionFactory;
            }
        }
        throw new BambooRuntimeException(String.format("can not find any sqlSessionFactory for statement[%s]",statementId));
    }

}