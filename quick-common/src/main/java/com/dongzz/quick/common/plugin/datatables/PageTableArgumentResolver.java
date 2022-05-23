package com.dongzz.quick.common.plugin.datatables;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 分页查询 参数解析器 实现 spring mvc 参数解析器
 */
public class PageTableArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * 设置要解析的参数类型
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> clazz = parameter.getParameterType();
        return clazz.isAssignableFrom(PageTableRequest.class);
    }

    /**
     * 解析参数，将 HttpServletRequest 转化为 PageTableRequest
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        PageTableRequest tableRequest = new PageTableRequest();
        Map<String, String[]> param = request.getParameterMap();

        //封装 分页参数
        if (param.containsKey("start")) {
            tableRequest.setOffset(Integer.parseInt(request.getParameter("start")));
        }
        if (param.containsKey("length")) {
            tableRequest.setLimit(Integer.parseInt(request.getParameter("length")));
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
        removeParam(tableRequest); // 过滤非必要参数
        return tableRequest;
    }

    /**
     * 去除请求参数中的无用参数
     *
     * @param tableRequest
     */
    private void removeParam(PageTableRequest tableRequest) {
        Map<String, Object> map = tableRequest.getParams();
        if (!CollectionUtils.isEmpty(map)) {
            Map<String, Object> param = new HashMap<String, Object>();
            for (Entry<String, Object> entry : map.entrySet()) {
                String k = entry.getKey();
                if (k.indexOf("[") < 0 && k.indexOf("]") < 0 && !"_".equals(k)) {
                    param.put(k, entry.getValue());
                }
            }
            tableRequest.setParams(param);
        }
    }

    /**
     * 从请求数据中解析排序参数
     *
     * @param tableRequest
     * @param map          请求参数
     */
    private void setOrderBy(PageTableRequest tableRequest, Map<String, Object> map) {
        StringBuilder orderBy = new StringBuilder();
        int size = map.size();
        for (int i = 0; i < size; i++) {
            String index = (String) map.get("order[" + i + "][column]");
            //排序列不存在了，结束循环
            if (StringUtils.isEmpty(index)) {
                break;
            }
            String column = (String) map.get("columns[" + index + "][data]");
            if (StringUtils.isBlank(column)) {
                continue;
            }
            String sort = (String) map.get("order[" + i + "][dir]");

            orderBy.append(column).append(" ").append(sort).append(", ");
        }

        if (orderBy.length() > 0) {
            tableRequest.getParams().put("orderBy",
                    " order by " + StringUtils.substringBeforeLast(orderBy.toString(), ","));
        }
    }

}
