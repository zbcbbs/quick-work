package com.dongzz.quick.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.List;
import java.util.Map;

/**
 * 基于 阿里 fastjson 封装的 json工具类
 */
public class FastJsonUtil {

    /**
     * 对象转化为json
     *
     * @param object 对象
     * @return
     */
    public static String bean2Json(Object object) {
        return JSON.toJSONString(object);
    }

    /**
     * json转化为对象
     *
     * @param jsonString json字符串
     * @param clazz      类型
     * @param <T>
     * @return
     */
    public static <T> T json2Bean(String jsonString, Class<T> clazz) {
        return JSON.parseObject(jsonString, clazz);
    }

    /**
     * json转化为对象集合
     *
     * @param jsonString json字符串
     * @param clazz      类型
     * @param <T>
     * @return
     */
    public static <T> List<T> json2List(String jsonString, Class<T> clazz) {
        return JSON.parseArray(jsonString, clazz);
    }

    /**
     * json转化为复杂的List<Map<String，Object>>
     *
     * @param jsonString json字符串
     * @return
     */
    public static List<Map<String, Object>> json2ListMap(String jsonString) {
        return JSON.parseObject(jsonString, new TypeReference<List<Map<String, Object>>>() {
        });
    }

}
