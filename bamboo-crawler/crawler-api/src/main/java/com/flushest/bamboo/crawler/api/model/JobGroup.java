package com.flushest.bamboo.crawler.api.model;

import com.alibaba.fastjson.JSONObject;
import com.flushest.bamboo.framework.annotation.Column;
import com.flushest.bamboo.framework.annotation.Id;
import com.flushest.bamboo.framework.annotation.Table;
import com.flushest.bamboo.framework.persistence.enums.GenerateStrategy;

import java.util.Date;

/**
 * Created by Administrator on 2017/11/8 0008.
 * 任务分组
 */
@Table(tablePrefix = "t_cra")
public class JobGroup {

    @Id(strategy = GenerateStrategy.UUID)
    @Column
    private String groupId;//分组id

    @Column
    private String groupName;//分组名

    @Column
    private String describe;//分组描述

    @Column
    private String state;//状态

    @Column
    private Date createTime;//创建时间

    @Column
    private Date updateTime;//更新时间

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
