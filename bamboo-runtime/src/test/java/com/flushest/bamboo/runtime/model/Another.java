package com.flushest.bamboo.runtime.model;

import com.flushest.bamboo.runtime.annotation.Column;
import com.flushest.bamboo.runtime.annotation.Id;
import com.flushest.bamboo.runtime.annotation.Table;

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
