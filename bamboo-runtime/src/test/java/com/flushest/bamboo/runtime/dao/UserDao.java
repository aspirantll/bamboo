package com.flushest.bamboo.runtime.dao;

import com.flushest.bamboo.runtime.common.EntityDao;
import com.flushest.bamboo.runtime.model.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2017/10/31 0031.
 */
@Component
public class UserDao extends EntityDao<User> {

    @Transactional(rollbackFor = Exception.class)
    public void transaction() {
        User user = new User();
        user.setUserName("bamboo");
        user.setPassword("bamboo");
        insert(user);
        user.setPassword("123456");
        update(user);
        System.out.print(query("bamboo"));
    }
}
