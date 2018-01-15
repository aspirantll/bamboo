package com.flushest.bamboo.framework.util;

import com.flushest.bamboo.framework.annotation.Priority;

import java.util.Comparator;

/**
 * Created by Administrator on 2018/1/14 0014.
 */
public class PriorityComparator implements Comparator<Class<?>> {

    private static PriorityComparator singleInstance;

    public static PriorityComparator getInstance() {
        if(singleInstance == null) {
            synchronized (PriorityComparator.class) {
                if(singleInstance == null) {
                    singleInstance = new PriorityComparator();
                }
            }
        }
        return singleInstance;
    }

    public static boolean priorThan(Class<?> c1, Class<?> c2) {
        PriorityComparator comparator = getInstance();
        return comparator.compare(c1, c2) > 0;
    }

    private PriorityComparator(){

    }

    @Override
    public int compare(Class<?> c1, Class<?> c2) {
        return getPriority(c1) - getPriority(c2);
    }

    private int getPriority(Class<?> clazz) {
        Priority priority = clazz.getAnnotation(Priority.class);

        if(priority != null) {
            return priority.value();
        }else {
            return Integer.MIN_VALUE;
        }
    }
}
