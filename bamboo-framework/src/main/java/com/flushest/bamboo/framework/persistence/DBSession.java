package com.flushest.bamboo.framework.persistence;

import com.flushest.bamboo.common.framework.exception.BambooRuntimeException;
import com.flushest.bamboo.framework.resource.ClassPathResourceResolver;
import net.sf.jsqlparser.JSQLParserException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2017/10/29 0029.
 */
@Component
@DependsOn("classPathResourceResolver")
public class DBSession {
    private static final Logger logger = LoggerFactory.getLogger(DBSession.class);

    private DBConfig dbConfig;

    public DBSession() {
        dbConfig = new DBConfig();
    }

    public SqlSession getSqlSession(String statementId, Object parameterObject) {
        return getSqlSession(statementId,parameterObject,true);
    }

    public SqlSession getSqlSession(String statementId, Object parameterObject, boolean autoCommit) {
        try {
            SqlSessionFactory sqlSessionFactory = SqlSessionFactoryProxy.getSqlSessionFactory(statementId,parameterObject);
            return sqlSessionFactory.openSession(autoCommit);
        } catch (JSQLParserException e) {
            logger.error(String.format("parse sql failed for statement[%s]",statementId));
            throw new BambooRuntimeException("parse sql failed",e);
        } catch (SQLException e) {
            logger.error(String.format("occurred exception at changing datasource for transaction"),e);
            throw new BambooRuntimeException("occurred exception at changing datasource for transaction",e);
        }
    }

    public <T> T selectOne(String statementId) {
        return selectOne(statementId,null);
    }

    public <T> T selectOne(String statementId, Object parameterObject) {
        return getSqlSession(statementId,parameterObject).selectOne(statementId,parameterObject);
    }

    public <E> List<E> selectList(String statementId) {
        return selectList(statementId,null);
    }

    public <E> List<E> selectList(String statementId, Object parameterObject) {
        return getSqlSession(statementId,parameterObject).selectList(statementId,parameterObject);
    }

    public int insert(String statementId) {
        return insert(statementId,null);
    }

    public int insert(String statementId, Object parameterObject) {
        return getSqlSession(statementId,parameterObject).insert(statementId,parameterObject);
    }

    public int update(String statementId) {
        return update(statementId,null);
    }

    public int update(String statementId, Object parameterObject) {
        return getSqlSession(statementId,parameterObject).update(statementId,parameterObject);
    }

    public int delete(String statementId) {
        return delete(statementId,null);
    }

    public int delete(String statementId, Object parameterObject) {
        return getSqlSession(statementId,parameterObject).delete(statementId,parameterObject);
    }
}
