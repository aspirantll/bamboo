package com.flushest.bamboo.runtime;

import com.flushest.bamboo.runtime.common.persistence.DBSession;
import com.flushest.bamboo.runtime.initcfg.StartUpConfig;
import com.flushest.bamboo.runtime.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2017/10/28 0028.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {StartUpConfig.class})
@WebAppConfiguration
public class DBTest {

    @Autowired
    private DBSession dbSession;

    @Test
    public void insertTest() {
        User user = new User();
        user.setUserName("bamboo");
        user.setPassword("bamboo");
        dbSession.insert("user.mapper.insert",user);
    }

    @Test
    public void QDUTest() {
        User user = new User();
        user.setUserName("bamboo");
        user.setPassword("123456");
        dbSession.update("user.mapper.update",user);
        System.out.print(dbSession.selectList("user.mapper.query","bamboo"));
        dbSession.delete("user.mapper.delete","bamboo");
    }
}
