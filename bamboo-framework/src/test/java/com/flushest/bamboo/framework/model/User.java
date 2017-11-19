package com.flushest.bamboo.framework.model;

import com.flushest.bamboo.framework.annotation.Column;
import com.flushest.bamboo.framework.annotation.Id;
import com.flushest.bamboo.framework.annotation.Table;

/**
 * Created by Administrator on 2017/10/29 0029.
 */
@Table(tablePrefix = "t")
public class User {
    @Id
    @Column
    private String userName;
    @Column
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
