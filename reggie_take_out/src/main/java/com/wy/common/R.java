package com.wy.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wangyu
 * @Date: 2022/09/14/16:46
 */
@Data
public class R<T> {

    private Integer code; // 1:成功 0和其他数字:失败
    private String msg; // 错误信息
    private T data;// 数据
    private Map map = new HashMap();

    public static <T> R<T> success(T object) {
        R<T> r = new R<T>();
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> R<T> error(String msg) {
        R r = new R();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }



}
