package com.flushest.bamboo.framework.persistence;

import org.springframework.context.annotation.Conditional;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.DefaultTransactionStatus;

import javax.sql.DataSource;
import java.util.Collection;

/**
 * Created by Administrator on 2017/11/1 0001.
 */
@Component("transactionManager")
public class DataSourceTransactionManagerExtension extends DataSourceTransactionManager {

    @Override
    public void afterPropertiesSet() {
        Collection<DataSource> dataSources = SqlSessionFactoryProxy.getDataSourceMap().values();
        if(dataSources.isEmpty()) {
            logger.info("没有配置数据源，不为事务管理器指定数据源...");
            return;
        }
        setDataSource(dataSources.iterator().next());
        logger.info("事务管理器初始化完成...");
        super.afterPropertiesSet();
    }

    @Override
    protected void doCleanupAfterCompletion(Object transaction) {
        super.doCleanupAfterCompletion(transaction);
        TransactionInfoUtil.clearStatus();
    }

    @Override
    protected void doRollback(DefaultTransactionStatus status) {
        super.doRollback(status);
        TransactionInfoUtil.clearStatus();
    }

    @Override
    protected void doCommit(DefaultTransactionStatus status) {
        super.doCommit(status);
        TransactionInfoUtil.clearStatus();
    }
}
