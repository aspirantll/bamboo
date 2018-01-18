package com.flushest.bamboo.framework.listener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Created by Administrator on 2018/1/18 0018.
 */
@Getter
@AllArgsConstructor
@Builder
public class Event {
    private int eventType;
    private Object object;


}
