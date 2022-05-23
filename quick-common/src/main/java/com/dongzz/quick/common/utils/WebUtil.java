package com.dongzz.quick.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.dongzz.quick.common.exception.ApplicationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Web工具类
 */
public class WebUtil {

    /**
     * 将浏览器参数存储到Map，对GET请求进行中文乱码处理
     *
     * @param request 请求对象
     * @return
     */
    public static Map<String, Object> getParamsMap(HttpServletRequest request) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String method = request.getMethod();
        Enumeration<?> keys = request.getParameterNames();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            if (key != null) {
                if (key instanceof String) {
                    String value = request.getParameter(key.toString());
                    if ("GET".equals(method)) {
                        try {
                            value = new String(value.getBytes("ISO-8859-1"), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                    paramMap.put(key.toString(), value);
                }
            }
        }
        return paramMap;
    }

    /**
     * 向客户端输出 json
     *
     * @param response        响应对象
     * @param responseContent 响应内容 | json
     */
    public static void flushOutJson(HttpServletResponse response, String responseContent) {
        PrintWriter writer = null;
        try {
            response.setContentType("application/json;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            //禁止浏览器缓存当前文档内容
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            writer = response.getWriter();
            if (responseContent == null || "".equals(responseContent) || "null".equals(responseContent)) {
                writer.write("");
            } else {
                writer.write(responseContent);
            }
        } catch (IOException e) {
            throw new ApplicationException("响应数据失败", e);
        } finally {
            //释放资源
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }

    /**
     * 向客户端响应 状态码 和 json
     *
     * @param response 响应对象
     * @param status   状态码
     * @param data     数据 | 会转化为json
     */
    public static void flushOutJson(HttpServletResponse response, int status, Object data) {
        PrintWriter out = null;
        try {
            // 跨域设置
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "*");
            //响应json
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(status);

            // 响应
            out = response.getWriter();
            out.write(JSONObject.toJSONString(data));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            if (null != out) {
                out.flush();
                out.close();
            }
        }
    }

    /**
     * 向客户端输出 文本
     *
     * @param response        响应对象
     * @param responseContent 响应内容 | 文本
     */
    public static void flushOutText(HttpServletResponse response, String responseContent) {
        PrintWriter writer = null;
        try {
            response.setContentType("text/html;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            //禁止浏览器缓存当前文档内容
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            writer = response.getWriter();
            if (responseContent == null || "".equals(responseContent) || "null".equals(responseContent)) {
                writer.write("");
            } else {
                writer.write(responseContent);
            }
        } catch (IOException e) {
            throw new ApplicationException("响应数据失败", e);
        } finally {
            //释放资源
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }

    /**
     * 判断http请求是否为ajax请求
     *
     * @param request
     * @return
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        boolean isAjax = false;
        String xReq = request.getHeader("x-requested-with");
        if (StringUtils.isNotBlank(xReq) && "XMLHttpRequest".equalsIgnoreCase(xReq)) {
            // 是ajax异步请求
            isAjax = true;
        }
        return isAjax;
    }

    /**
     * 获取 HttpServletRequest
     * 在代码的任意位置均可获取
     *
     * @return
     */
    public static HttpServletRequest getHttpRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 检查ip是否合法
     *
     * @param ip
     * @return
     */
    public static boolean ipValid(String ip) {
        String regex0 = "(2[0-4]\\d)" + "|(25[0-5])";
        String regex1 = "1\\d{2}";
        String regex2 = "[1-9]\\d";
        String regex3 = "\\d";
        String regex = "(" + regex0 + ")|(" + regex1 + ")|(" + regex2 + ")|(" + regex3 + ")";
        regex = "(" + regex + ").(" + regex + ").(" + regex + ").(" + regex + ")";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(ip);
        return m.matches();
    }

    /**
     * 获取请求的客户端ip
     *
     * @param request
     * @return
     */
    public static String getIp(final HttpServletRequest request) {

        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { //"***.***.***.***".length() = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }
}
