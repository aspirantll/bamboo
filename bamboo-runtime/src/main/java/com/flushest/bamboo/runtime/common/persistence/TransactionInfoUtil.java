package com.flushest.bamboo.runtime.common.persistence;

import org.springframework.core.NamedThreadLocal;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.jdbc.datasource.JdbcTransactionObjectSupport;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/1 0001.
 */
public class TransactionInfoUtil {
    private static ThreadLocal<Object> status = new NamedThreadLocal<>("transaction-status");

    public static void changeDataSource(DataSource dataSource) throws SQLException {
        if(hasTransaction()&&!isInTransaction()) {
            JdbcTransactionObjectSupport transactionObjectSupport = (JdbcTransactionObjectSupport) ((DefaultTransactionStatus)getStatus()).getTransaction();
            ConnectionHolder originHolder = transactionObjectSupport.getConnectionHolder();

            ConnectionHolder connectionHolder = (ConnectionHolder) TransactionSynchronizationManager.getResource(dataSource);
            if(connectionHolder==null) {
                connectionHolder = new ConnectionHolder(dataSource.getConnection(),false);
                TransactionSynchronizationManager.bindResource(dataSource,connectionHolder);
            }
            if(!originHolder.equals(connectionHolder)) {
                copyPropertiesForConnectionHolder(originHolder,connectionHolder);
                transactionObjectSupport.setConnectionHolder(connectionHolder);
            }

            setStatus();
        }
    }

    public static void clearStatus() {
        status.set(null);
    }

    private static boolean hasTransaction() {
        try {
            getStatus();
        }catch (NoTransactionException e) {
            return false;
        }
        return true;
    }

    private static TransactionStatus getStatus() {
        return TransactionAspectSupport.currentTransactionStatus();
    }

    private static void copyPropertiesForConnectionHolder(ConnectionHolder source,ConnectionHolder target) throws SQLException {
        if(!source.equals(target)) {
            target.setSynchronizedWithTransaction(source.isSynchronizedWithTransaction());

            Connection sourceConnection = source.getConnection();
            Connection targetConnection = target.getConnection();

            targetConnection.setAutoCommit(sourceConnection.getAutoCommit());
            targetConnection.setReadOnly(sourceConnection.isReadOnly());
            targetConnection.setTransactionIsolation(sourceConnection.getTransactionIsolation());
        }
    }

    private static void setStatus() {
        status.set(new Object());
    }

    private static boolean isInTransaction() {
        return status.get()!=null;
    }
}
