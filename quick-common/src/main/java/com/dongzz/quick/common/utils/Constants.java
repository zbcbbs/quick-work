package com.dongzz.quick.common.utils;

/**
 * 全局 常量
 */
public class Constants {

    //---------------- Servlet上下文 字典存储标记 ------------
    public static final String DICT_CONTEXT_KEY = "dict.key";

    /**
     * 用于IP定位转换
     */
    public static final String REGION = "内网IP|内网IP";
    /**
     * win 系统
     */
    public static final String WIN = "win";

    /**
     * mac 系统
     */
    public static final String MAC = "mac";

    /**
     * 常用接口
     */
    public static class Url {
        // IP归属地查询
        public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp?ip=%s&json=true";
    }

}
