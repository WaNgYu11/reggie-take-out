package com.wy.common;

/**
 * @Author: wangyu
 * @Date: 2022/09/15/10:57
 */
public class BaseContext {


    public static ThreadLocal<Long> threadLocal = new ThreadLocal<Long>();


    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }

}
