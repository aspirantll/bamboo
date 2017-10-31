package com.flushest.bamboo.runtime.common;

import com.flushest.bamboo.runtime.common.persistence.DBSession;
import com.flushest.bamboo.runtime.util.ClassUtil;
import com.flushest.bamboo.runtime.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/10/31 0031.
 */
public class EntityDao<T> {
    private String mapperName;

    @Autowired
    protected DBSession dbSession;

    public EntityDao() {
        Class<T> entityClass = ClassUtil.getGenericClass(getClass(),EntityDao.class,0);
        mapperName = ClassUtil.convertClassNameToMapperName(entityClass);
    }

    public int insert(T entity) {
        return dbSession.insert(mapperName+".insert",entity);
    }

    public int delete(Serializable id) {
        return dbSession.delete(mapperName+".delete",id);
    }

    public int update(T entity) {
        return dbSession.update(mapperName+".update",entity);
    }

    public T query(Serializable id) {
        return dbSession.selectOne(mapperName+".query",id);
    }
}
