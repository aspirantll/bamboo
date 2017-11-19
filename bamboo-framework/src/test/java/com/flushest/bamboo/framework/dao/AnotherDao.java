package com.flushest.bamboo.framework.dao;

import com.flushest.bamboo.framework.persistence.EntityDao;
import com.flushest.bamboo.framework.model.Another;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2017/11/1 0001.
 */
@Component
public class AnotherDao extends EntityDao<Another> {
    @Transactional(rollbackFor = Exception.class)
    public void transaction() {
        Another another = new Another();
        another.setId("111");
        insert(another);
    }
}
