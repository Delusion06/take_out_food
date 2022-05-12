package com.ex.echo.common;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Exception
 * @Date: 2022/5/5
 * @Description: 通用返回结果，服务端响应的数据最终都会封装成此对象
 */
@Data
public class JsonModel<T> implements Serializable {
    private static final long serialVersionUID = 4857912379424519420L;

    /**
     * 编码,1成功,0和其它数字为失败
     */
    private Integer code;

    /**
     * 错误信息
     */
    private String msg;

    /**
     * 数据
     */
    private T data;

    /**
     * 动态数据
     */
    private Map map = new HashMap();

    public static <T> JsonModel<T> success(T object) {
        JsonModel<T> r = new JsonModel<T>();
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> JsonModel<T> error(String msg) {
        JsonModel r = new JsonModel();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public static <T> JsonModel<T> errorT(String msg){
        JsonModel r = new JsonModel();
        r.msg = msg;
        r.code = -1;
        return r;
    }

    public JsonModel<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}
