package com.flushest.bamboo.framework.model;

import com.flushest.bamboo.framework.annotation.Column;
import com.flushest.bamboo.framework.annotation.Id;
import com.flushest.bamboo.framework.annotation.Table;

/**
 * Created by Administrator on 2017/11/1 0001.
 */
@Table(tablePrefix = "a")
public class Another {
    @Id
    @Column
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
