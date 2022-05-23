package com.dongzz.quick.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础 工具类
 * 存储一些杂乱的工具方法
 */
public class Util {

    /**
     * 字符串分割为集合
     *
     * @param str       字符串
     * @param separator 分隔符
     * @return
     */
    public static List<String> strSplitToList(String str, String separator) {
        List<String> result = new ArrayList<String>();
        if (StringUtils.isNotBlank(str)) {
            if (str.contains(separator)) {
                String[] split = str.split(separator);
                for (String n : split) {
                    result.add(n);
                }
            } else {
                result.add(str);
            }
        }
        return result;
    }

    public static void main(String[] args) {
//        List<String> list = strSplitToList("1,2,3,4", ",");
//        for (String str : list) {
//            System.out.println(str);
//        }

    }

}
