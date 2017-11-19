package com.flushest.bamboo.crawler.api.enums;

/**
 * Created by Administrator on 2017/11/8 0008.
 * 分组状态
 */
public enum JobGroupStatus {
    NORMAL("0"),//正常
    PAUSE("1");//暂停使用

    private String value;

    JobGroupStatus(String value) {
        this.value =value;
    }

    public String value() {
        return value;
    }
}
