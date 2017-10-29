package com.flushest.bamboo.runtime.model;

import com.flushest.bamboo.runtime.annotation.Column;
import com.flushest.bamboo.runtime.annotation.Table;

/**
 * Created by Administrator on 2017/10/29 0029.
 */
@Table(tablePrefix = "t")
public class User {
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
