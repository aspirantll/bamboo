package com.flushest.bamboo.framework;

import com.flushest.bamboo.framework.dao.AnotherDao;
import com.flushest.bamboo.framework.dao.UserDao;
import com.flushest.bamboo.framework.initcfg.StartUpCoreConfig;
import com.flushest.bamboo.framework.model.User;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by Administrator on 2017/10/28 0028.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {StartUpCoreConfig.class})
@WebAppConfiguration
@Ignore
public class DBTest {

    @Autowired
    private UserDao userDao;
    @Autowired
    private AnotherDao anotherDao;

    @Test
    @Ignore
    public void insertTest() {
        User user = new User();
        user.setUserName("bamboo");
        user.setPassword("bamboo");
        userDao.insert(user);
    }

    @Test
    public void QDUTest() {
        User user = new User();
        user.setUserName("bamboo");
        user.setPassword("123456");
        userDao.update(user);
        System.out.print(userDao.query("bamboo"));
    }

    @Test
    public void trasactionTest() {
        userDao.transaction();
        anotherDao.transaction();
    }
}
