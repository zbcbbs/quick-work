package com.dongzz.quick.common.plugin.vuetables;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.Map.Entry;

/**
 * 分页查询 参数解析器 实现 spring mvc 参数解析器
 */
public class VueTableArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * 设置要解析的参数类型
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> clazz = parameter.getParameterType();
        return clazz.isAssignableFrom(VueTableRequest.class);
    }

    /**
     * 解析参数，将 HttpServletRequest 转化为 VueTableRequest
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        VueTableRequest tableRequest = new VueTableRequest();
        Map<String, String[]> param = request.getParameterMap();

        // 分页参数封装
        if (param.containsKey("page")) {
            tableRequest.setOffset(Integer.parseInt(request.getParameter("page")));
        }
        if (param.containsKey("size")) {
            tableRequest.setLimit(Integer.parseInt(request.getParameter("size")));
        }

        Map<String, Object> map = new HashMap<>();
        tableRequest.setParams(map);
        for (Entry<String, String[]> entry : param.entrySet()) {
            String k = entry.getKey();
            String[] v = entry.getValue();
            if (v.length == 1) {
                map.put(k, v[0]);
            } else {
                map.put(k, Arrays.asList(v));
            }
        }

        setOrderBy(tableRequest, map); // 排序
        setTime(tableRequest, map); // 时间
        removeParam(tableRequest); // 删除无用参数
        return tableRequest;
    }

    /**
     * 去除无用的参数
     *
     * @param tableRequest
     */
    private void removeParam(VueTableRequest tableRequest) {
        Map<String, Object> map = tableRequest.getParams();
        if (!CollectionUtils.isEmpty(map)) {
            Map<String, Object> param = new HashMap<>();
            for (Entry<String, Object> entry : map.entrySet()) {
                String k = entry.getKey();
                if (!"sort".equals(k) && !"time".equals(k)) { // 移除排序 已经独立处理
                    param.put(k, entry.getValue());
                }
            }
            tableRequest.setParams(param);
        }
    }

    /**
     * 处理排序
     *
     * @param tableRequest
     * @param map
     */
    private void setOrderBy(VueTableRequest tableRequest, Map<String, Object> map) {
        StringBuilder orderBy = new StringBuilder();
        Object sort = map.get("sort");
        if (Objects.nonNull(sort)) {
            if (sort instanceof String) { // 单字段排序
                String orderStr = (String) sort;
                String o = orderStr.replace(",", " ");
                orderBy.append(o);
            }
            if (sort instanceof List) { // 多字段排序
                List<String> orderList = (List<String>) sort;
                for (String orderStr : orderList) {
                    String o = orderStr.replace(",", " ");
                    orderBy.append(o).append(",");
                }
            }

            if (orderBy.length() > 0) {
                if (orderBy.toString().endsWith(",")) {
                    tableRequest.getParams().put("orderBy", " order by " + StringUtils.substringBeforeLast(orderBy.toString(), ","));
                } else {
                    tableRequest.getParams().put("orderBy", " order by " + orderBy.toString());
                }

            }
        }
    }

    /**
     * 处理时间段
     *
     * @param tableRequest
     * @param map
     */
    private void setTime(VueTableRequest tableRequest, Map<String, Object> map) {
        Object time = map.get("time");
        if (Objects.nonNull(time)) {
            List<String> timeArr = (List<String>) time;
            tableRequest.getParams().put("startdate", timeArr.get(0));
            tableRequest.getParams().put("enddate", timeArr.get(1));
        }
    }

}
